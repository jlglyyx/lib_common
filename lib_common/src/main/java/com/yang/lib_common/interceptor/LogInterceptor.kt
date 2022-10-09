package com.yang.lib_common.interceptor

import android.util.Log
import com.yang.lib_common.constant.AppConstant
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.net.URLDecoder

/**
 * @Author Administrator
 * @ClassName LogInterceptor
 * @Description
 * @Date 2021/12/14 9:07
 */
class LogInterceptor(private var logLevel: Int = AppConstant.Constant.NUM_MINUS_ONE) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
        Log.d(AppConstant.ClientInfo.TAG_LOG, "intercept: ===============request===============")
        //Log.d(AppConstant.ClientInfo.TAG_LOG, "request.baseUrl: ${request.url().toString().substring(0, request.url().toString().lastIndexOf("?"))}\n")
        Log.d(AppConstant.ClientInfo.TAG_LOG, "request.url: ${URLDecoder.decode(request.url().toString(),"UTF-8")}\n")
        Log.d(AppConstant.ClientInfo.TAG_LOG, "request.method: ${request.method()}\n")
        Log.d(AppConstant.ClientInfo.TAG_LOG, "request.headers: ${request.headers()}\n")
        if (logLevel == AppConstant.Constant.NUM_MINUS_ONE){
            Log.d(AppConstant.ClientInfo.TAG_LOG, "request.body: ${getBody(request)}\n")
        }
        Log.d(AppConstant.ClientInfo.TAG_LOG, "intercept: ===============request===============")
        Log.d(
            AppConstant.ClientInfo.TAG_LOG,
            "intercept: ===============Start --- Response==============="
        )
        Log.d(AppConstant.ClientInfo.TAG_LOG, "response.isSuccessful: ${response.isSuccessful}\n")
        Log.d(AppConstant.ClientInfo.TAG_LOG, "response.message: ${response.message()}\n")
        Log.d(AppConstant.ClientInfo.TAG_LOG, "response.headers: ${response.headers()}\n")
        Log.d(AppConstant.ClientInfo.TAG_LOG, "response.code: ${response.code()}\n")
        val content = response.body()?.string()
        val contentType = response.body()?.contentType()
        //Log.d(TAG_LOG, "response.body: ${content?.length} ${content}\n")
        showLogCompletion(content.toString(), 3000)
        Log.d(AppConstant.ClientInfo.TAG_LOG, "response.request.url: ${response.request().url()}\n")
        Log.d(
            AppConstant.ClientInfo.TAG_LOG,
            "intercept: ===============End --- Response==============="
        )
        response = response.newBuilder().body(ResponseBody.create(contentType, content)).build()
        return response
    }

    private fun getBody(request: Request): String {
        try {
            val buffer = Buffer()
            val body = request.body()
//            if (body?.contentLength()!! >=1000000){
//                return "请求体太大了，打印OOM"
//            }
            body?.writeTo(buffer)
            val contentType = body?.contentType()
            val charset = contentType?.charset(Charsets.UTF_8)
            return if (charset != null) {
                buffer.readString(charset)
            } else {
                "无请求体"
            }
        } catch (e: Exception) {
            Log.i(AppConstant.ClientInfo.TAG, "getBody: ${e.message}")
        }
        return ""
    }

    private fun showLogCompletion(log: String, size: Int) {

        if (log.length > size) {
            val substring = log.substring(0, size)
            Log.d(AppConstant.ClientInfo.TAG_LOG, "response.body: ${substring}")
            if (log.length - substring.length > size) {
                val substring1 = log.substring(substring.length, log.length)
                showLogCompletion(substring1, size)
            } else {
                val substring1 = log.substring(substring.length, log.length)
                Log.d(AppConstant.ClientInfo.TAG_LOG, "${substring1}")
            }

        } else {
            Log.d(AppConstant.ClientInfo.TAG_LOG, "response.body: ${log}\n")
        }

    }
}