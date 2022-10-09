package com.yang.lib_common.down.thread

import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL


/**
 * @ClassName FileDownloadMoreThread
 *
 * @Description
 *
 * @Author 1
 *
 * @Date 2021/1/4 14:28
 */
class FileDownloadMoreThread(
    var url: URL,
    var file: File,
    var startPosition: Int,
    var endPosition: Int,
    var threadName: String
) : Runnable {

    companion object {
        private const val TAG = "FileDownloadMoreThread"
        const val BUFF_SIZE = 1024
    }

    lateinit var bufferedInputStream: BufferedInputStream
    private lateinit var randomAccessFile: RandomAccessFile
    private var currentPosition: Int = 0
    var downloadSize: Long = 0
    var finishDown = false
    var byte = ByteArray(BUFF_SIZE)
    

    init {
        currentPosition = startPosition
    }


    override fun run() {
        try {
            val openConnection = url.openConnection() as HttpURLConnection
            openConnection.setRequestProperty("Range", "bytes=$startPosition-$endPosition")
            Log.i(TAG, "run: ${openConnection.responseCode} $startPosition $endPosition")
            if (openConnection.responseCode == 200 || openConnection.responseCode == 206) {
                randomAccessFile = RandomAccessFile(file, "rw")
                randomAccessFile.seek(startPosition.toLong())

                bufferedInputStream =
                    BufferedInputStream(openConnection.inputStream, 1)

                while (currentPosition < endPosition) {
                    val read = bufferedInputStream.read(byte)
                    if (read == -1) {
                        break
                    }
                    randomAccessFile.write(byte, 0, read)
                    currentPosition += read
                    downloadSize += read

                    //Log.i(TAG, "run: $threadName==下载了:$downloadSize")
                }
                finishDown = true
                Log.i(TAG, "run: $threadName==下载完成：$finishDown")
                bufferedInputStream.close()
                randomAccessFile.close()
                openConnection.disconnect()
            }
        } catch (e: Exception) {
            Log.i(TAG, "Exception: ${e.message}   ${e.cause}")
        }
    }





}