package com.yang.lib_common.upload

import android.util.Log
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.interceptor.LogInterceptor
import com.yang.lib_common.interceptor.UrlInterceptor
import com.yang.lib_common.room.BaseAppDatabase
import com.yang.lib_common.room.entity.UploadTaskData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Okio
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @Author Administrator
 * @ClassName UploadManage
 * @Description
 * @Date 2021/11/19 9:31
 */
class UploadManage {

    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(UrlInterceptor())
        .addInterceptor(LogInterceptor(AppConstant.Constant.NUM_ONE))
        .connectTimeout(AppConstant.ClientInfo.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(AppConstant.ClientInfo.READ_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(AppConstant.ClientInfo.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
        .connectionPool(ConnectionPool())
        .build()
    private var uploadListeners: MutableList<UploadListener?> = mutableListOf()

    private var callMap: MutableMap<String, Call?> = mutableMapOf()

    companion object {
        private const val TAG = "UploadManage"

        val instance: UploadManage by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            UploadManage()
        }
    }

    /**
     * 创建RequestBody
     */
    private fun createRequestBody(id: String, filePath: String): RequestBody {
        val builder = MultipartBody.Builder()
        val file = File(filePath)
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        builder.addPart(
            MultipartBody.Part.createFormData(
                "file",
                file.name,
                UploadRequestBody(requestBody, id, 0)
            )
        )
        CoroutineScope(Dispatchers.IO).launch {
            BaseAppDatabase.instance.uploadTaskDao()
                .insertData(UploadTaskData(id, 0, filePath, 0, 0, 0))
        }
        return builder.build()
    }

    /**
     * 开始上传
     *
     * 1.开始上传 创建id 存入数据库
     * 2.存储id 对应上传请求
     * 3.实现监听获取进度
     * 4.暂停 即取消 取出id重新创建请求 断点续传
     * 5.取消
     */
    fun startUpload(filePath: String, id: String = UUID.randomUUID().toString().replace("-", "")) {
        val createRequestBody = createRequestBody(id, filePath)
        val build = Request.Builder()
            .post(createRequestBody)
            .url(AppConstant.ClientInfo.BASE_URL + "uploadFile")
            .build()
        val newCall = okHttpClient.newCall(build)
        callMap[id] = newCall
        newCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CoroutineScope(Dispatchers.IO).launch {
                    val queryData = BaseAppDatabase.instance.uploadTaskDao().queryData(id)
                    uploadListeners.forEach {
                        queryData.status = 2
                        BaseAppDatabase.instance.uploadTaskDao().updateData(queryData)
                        it?.onFailed(id)
                    }
                }
                Log.i(TAG, "onFailure: ${e.message}")
            }
            override fun onResponse(call: Call, response: Response) {
                Log.i(TAG, "onResponse: ")
            }
        })
    }

    fun resumeUpload(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val queryData = BaseAppDatabase.instance.uploadTaskDao().queryData(id)
        if (callMap.containsKey(id)||null != queryData ) {
            
                val builder = MultipartBody.Builder()
                val file = File(queryData.filePath)
                val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                builder.addPart(
                    MultipartBody.Part.createFormData(
                        "file",
                        file.name,
                        UploadRequestBody(requestBody, id, queryData.currentSize)
                    )
                )
                val build = Request.Builder()
                    .post(builder.build())
                    .url(AppConstant.ClientInfo.BASE_URL + "uploadFile")
                    .header("Range", "bytes=${queryData.currentSize}-${queryData.countSize}")
                    .build()
                val newCall = okHttpClient.newCall(build)
                callMap[id] = newCall
                newCall.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        uploadListeners.forEach {
                            queryData.status = 2
                            BaseAppDatabase.instance.uploadTaskDao().updateData(queryData)
                            it?.onFailed(id)
                        }
                        Log.i(TAG, "onFailure: ${e.message}")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Log.i(TAG, "onResponse: ")
                    }
                })

            }


        }
    }

    fun cancelUpload(id: String) {
        callMap[id]?.cancel()
    }

    fun addUploadListener(uploadListener: UploadListener) {
        uploadListeners.add(uploadListener)
    }

    fun removeUploadListener(uploadListener: UploadListener) {
        uploadListeners.remove(uploadListener)
    }

    fun clearUploadListener() {
        uploadListeners.clear()
    }


    /**
     * 计算进度
     */
    inner class UploadRequestBody(
        var requestBody: RequestBody,
        var id: String,
        var currentSize: Long
    ) : RequestBody() {

        override fun contentType(): MediaType? {
            return requestBody.contentType()
        }

        override fun writeTo(sink: BufferedSink) {
            var countByte = currentSize
            val queryData = BaseAppDatabase.instance.uploadTaskDao().queryData(id)
            queryData.countSize = requestBody.contentLength()
            val buffer = Okio.buffer(object : ForwardingSink(sink) {
                override fun write(source: Buffer, byteCount: Long) {
                    super.write(source, byteCount)
                    countByte += byteCount
                    val process = (countByte / requestBody.contentLength().toFloat() * 100).toInt()
                    queryData.currentSize = countByte
                    BaseAppDatabase.instance.uploadTaskDao().updateData(queryData)
                    uploadListeners.forEach {
                        it?.onProgress(id, process)
                        queryData.progress = process
                        BaseAppDatabase.instance.uploadTaskDao().updateData(queryData)
                        if (process >= 100) {
                            it?.onSuccess(id)
                            callMap[id]?.cancel()
                            callMap.remove(id)
                            queryData.status = 1
                            BaseAppDatabase.instance.uploadTaskDao().updateData(queryData)
                        }
                    }
                }
            })
            requestBody.writeTo(buffer)
            buffer.flush()
        }

        override fun contentLength(): Long {
            return requestBody.contentLength()
        }
    }
}