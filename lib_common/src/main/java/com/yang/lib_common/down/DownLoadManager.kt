package com.yang.lib_common.down

import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

/**
 * @Author Administrator
 * @ClassName DownLoadManager
 * @Description
 * @Date 2021/12/1 14:26
 */
class DownLoadManager : Thread() {

    companion object {
        private const val TAG = "DownLoadManager"
    }

    private var downLoadManagerListener:DownLoadManagerListener? = null

    private var filePath = ""

    private lateinit var file: File

    private var fileLength = 0L

    private var url = ""

    private var executorService = Executors.newFixedThreadPool(5)

    private var threadSize = 1

    private var threadList = mutableListOf<DownLoadTask>()

    private var downloadPercent = 0f

    private var createNewFile = false

    @Volatile
    private var downloadSize = 0L


    private fun getContentLength() {
        try {
            val url = URL(url)
            file = File(filePath)
            val openConnection = url.openConnection() as HttpURLConnection
            if (openConnection.responseCode == 200 || openConnection.responseCode == 206) {
                val contentLength = openConnection.contentLength.toLong()
                if (file.exists()) {
                    if (createNewFile){
                        file.delete()
                    }else{
                        fileLength = file.length()
                    }
                }
                if (fileLength == contentLength) {
                    return
                }
                val randomAccessFile = RandomAccessFile(file, "rwd")
                randomAccessFile.setLength(contentLength)
                randomAccessFile.close()

                val block = (contentLength - fileLength) / threadSize

                val lastBlock = (contentLength - fileLength) % threadSize

                for (i in 0 until threadSize) {
                    if (i == threadSize - 1) {
                        executorService.submit(
                            DownLoadTask(
                                block * i,
                                block * (i + 1) + lastBlock
                            ).apply {
                                threadList.add(this)
                            })
                    } else {
                        executorService.submit(DownLoadTask(block * i, block * (i + 1)).apply {
                            threadList.add(this)
                        })
                    }
                }
                val measureTimeMillis = measureTimeMillis {
                    var finish = false
                    val startTime = System.currentTimeMillis()
                    while (!finish) {
                        downloadSize = 0
                        threadList.forEach {
                            downloadSize += it.downSize
                        }
                        val fl = downloadSize / contentLength.toFloat()
                        if (downloadPercent != fl) {
                            downloadPercent = fl
                            val useTime =  System.currentTimeMillis() - startTime
                            downLoadManagerListener?.onProgress(downloadPercent,useTime,downloadSize/useTime)
                        }
                        if (downloadSize >= contentLength) {
                            finish = true
                        }
                    }
                }
                downLoadManagerListener?.onSuccess(file.absolutePath, measureTimeMillis)
                threadList.forEach {
                    if (!it.isInterrupted){
                        it.interrupt()
                    }
                }
                if (!this.isInterrupted){
                    interrupt()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            downLoadManagerListener?.onFailed(e.localizedMessage?:"下载失败")
        }
    }

    override fun run() {
        super.run()
        getContentLength()
    }

    inner class DownLoadTask(private var startIndex: Long, private var endIndex: Long) : Thread() {
        var downSize = 0L
        override fun run() {
            super.run()
            startDown(startIndex, endIndex)
        }

        private fun startDown(startIndex: Long, endIndex: Long) {
            val url = URL(url)
            val openConnection = url.openConnection() as HttpURLConnection
            Log.i("TAG", "startDown===: $startIndex  $endIndex")
            openConnection.setRequestProperty("Range", "bytes=$startIndex-$endIndex")
            if (openConnection.responseCode == 200 || openConnection.responseCode == 206) {
                val contentLength = openConnection.contentLength
                val inputStream = openConnection.inputStream
                var currentIndex = startIndex
                val randomAccessFile = RandomAccessFile(file, "rwd")
                val bytes = ByteArray((endIndex - startIndex).toInt())
                val bufferedInputStream = BufferedInputStream(inputStream, bytes.size)
                randomAccessFile.seek(startIndex)
                val measureTimeMillis = measureTimeMillis {
                    val startTime = System.currentTimeMillis()
                    while (currentIndex < endIndex) {
                        val read = bufferedInputStream.read(bytes)
                        if (read == -1) {
                            break
                        }
                        randomAccessFile.write(bytes, 0, read)
                        currentIndex += read
                        downSize += read
                        val useTime = System.currentTimeMillis() - startTime
                        downLoadManagerListener?.onChildProgress(this@DownLoadTask.name, downSize / (endIndex - startIndex).toFloat(),useTime,downSize/useTime)
                    }
                }
                Log.i(TAG, "${this@DownLoadTask.name}===measureTimeMillis: $measureTimeMillis")
                randomAccessFile.close()
                bufferedInputStream.close()
                inputStream.close()
                openConnection.disconnect()
            }
        }
    }

    class Builder {
        private var threadSize = 1

        private var filePath: String = ""

        private var url: String = ""

        private var createNewFile: Boolean = false

        private var downLoadManagerListener:DownLoadManagerListener? = null

        fun threadSize(threadSize: Int): Builder {
            this.threadSize = threadSize
            return this
        }

        fun filePath(filePath: String): Builder {
            this.filePath = filePath
            return this
        }

        fun url(url: String): Builder {
            this.url = url
            return this
        }
        fun createNewFile(createNewFile: Boolean): Builder {
            this.createNewFile = createNewFile
            return this
        }
        fun downLoadManagerListener(downLoadManagerListener: DownLoadManagerListener): Builder {
            this.downLoadManagerListener = downLoadManagerListener
            return this
        }

        fun build() {
            val downLoadManager = DownLoadManager()
            downLoadManager.threadSize = threadSize
            downLoadManager.filePath = filePath
            downLoadManager.url = url
            downLoadManager.createNewFile = createNewFile
            downLoadManager.downLoadManagerListener = downLoadManagerListener
            downLoadManager.start()
        }
    }




}