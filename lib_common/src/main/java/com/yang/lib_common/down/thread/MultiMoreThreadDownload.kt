package com.yang.lib_common.down.thread

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.showShort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors


/**
 * @ClassName MultiMoreThreadDownload
 *
 * @Description
 *
 * @Author 1
 *
 * @Date 2021/1/4 14:30
 */
class MultiMoreThreadDownload(
    private var mContext: Context,
    private var threadNum: Int = 10,
    private var parentFilePath: String,
    private var filePath: String,
    private var fileUrl: String,
    private var showNotice: Boolean,
    private var downListener: DownListener? = null
) : Thread() {

    private var fileSize: Int = 0
    private var blockSize: Int = 0
    private var fileDownloadMoreThreads = mutableListOf<FileDownloadMoreThread>()
    private var executors = Executors.newFixedThreadPool(5)
    private var downloadSize: Long = 0
    private var downloadPercent: Int = 0
    private var currentPercent: Int = 0
    private var currentTimeMillis: Long = 0
    private var usedTimeMillis: Int = 0
    private var downloadSpeed: Int = 0
    private var fileHasLength: Int = 0
    private var finishDown = false

    interface DownListener {
        fun downSuccess(fileUrl: String)
    }

    companion object {
        private const val TAG = "MultiMoreThreadDownload"
    }

    class Builder(private var mContext: Context) {
        private var threadNum: Int = 10
        private lateinit var parentFilePath: String
        private lateinit var filePath: String
        private lateinit var fileUrl: String
        private var showNotice: Boolean = true
        private var downListener: DownListener? = null

        /**
         * 线程数
         */
        fun threadNum(threadNum: Int): Builder {
            this.threadNum = threadNum
            return this
        }

        /**
         * 下载储存父目录
         */
        fun parentFilePath(parentFilePath: String): Builder {
            this.parentFilePath = parentFilePath
            return this
        }

        /**
         * 下载储存文件名
         */
        fun filePath(filePath: String): Builder {
            this.filePath = filePath
            return this
        }

        /**
         * 下载网络路径
         */
        fun fileUrl(fileUrl: String): Builder {
            this.fileUrl = fileUrl
            return this
        }

        /**
         * 展示通知
         */
        fun showNotice(showNotice: Boolean): Builder {
            this.showNotice = showNotice
            return this
        }

        /**
         * 展示通知
         */
        fun downListener(downListener: DownListener): Builder {
            this.downListener = downListener
            return this
        }

        fun build(): MultiMoreThreadDownload {
            return MultiMoreThreadDownload(
                mContext,
                threadNum,
                parentFilePath,
                filePath,
                fileUrl,
                showNotice,
                downListener
            )
        }

    }


    override fun run() {
        try {
            val parentFile = File(parentFilePath)
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            val file = File(parentFile, filePath)
            val url = URL(fileUrl)
            val openConnection = url.openConnection() as HttpURLConnection
            if (openConnection.responseCode == 200 || openConnection.responseCode == 206) {
                fileSize = openConnection.contentLength
                if (file.exists()) {
                    //file.delete()
                    fileHasLength = file.length().toInt()
                }
//
                if (fileSize == fileHasLength) {
                    GlobalScope.launch(Dispatchers.Main) {
                        downListener?.downSuccess(file.absolutePath)
                    }
                    return
                }

                val randomAccessFile = RandomAccessFile(file, "rwd")

                randomAccessFile.setLength(fileSize.toLong())

                randomAccessFile.close()

                blockSize = fileSize / threadNum

                val i1 = fileSize % threadNum

                Log.i(TAG, "run: $fileSize  $blockSize")

                for (i in 0 until threadNum) {
                    var fileDownloadMoreThread:FileDownloadMoreThread
                    if (i == threadNum -1){
                        fileDownloadMoreThread = FileDownloadMoreThread(
                            url,
                            file,
                            i * blockSize,
                            (i + 1) * blockSize+i1,
                            "线程$i"
                        )
                    }else{
                        fileDownloadMoreThread = FileDownloadMoreThread(
                            url,
                            file,
                            i * blockSize,
                            (i + 1) * blockSize,
                            "线程$i"
                        )
                    }
                    executors.submit(fileDownloadMoreThread)
                    fileDownloadMoreThreads.add(fileDownloadMoreThread)
                }


                var finish = false

                val startTimeMillis = System.currentTimeMillis()

                while (!finish) {

                    finishDown = false
                    finish = true
                    downloadSize = 0
                    fileDownloadMoreThreads.forEach {
                        downloadSize += it.downloadSize
                        finish = it.finishDown
                    }

                    currentTimeMillis = System.currentTimeMillis()

                    usedTimeMillis = ((currentTimeMillis - startTimeMillis) / 1000).toInt()

                    if (usedTimeMillis == 0) {
                        usedTimeMillis = 1
                    }

                    downloadSpeed = ((downloadSize / usedTimeMillis) / 1024).toInt()

                    downloadPercent = (downloadSize * 100).toInt() / fileSize
                    if (currentPercent < downloadPercent) {
                        if (downloadPercent >= 99) {
                            downloadPercent = 100
                            finishDown = true
                        }
                        Log.i(
                            TAG,
                            "run:  下载进度：$downloadPercent%  用时：$usedTimeMillis/s  下载速度：$downloadSpeed Kb/s"
                        )
                        currentPercent = downloadPercent
                        if (showNotice) {
                            showDownLoadNotification(downloadPercent, usedTimeMillis, downloadSpeed)
                        }
                    }

                }

                GlobalScope.launch(Dispatchers.Main) {
                    if (showNotice) {
                        showShort("下载完成：${file.absolutePath}")
                        showCompleteNotification(file)
                    }
                    downListener?.downSuccess(file.absolutePath)
                }

            }

        } catch (e: Exception) {
            GlobalScope.launch(Dispatchers.Main) {
                if (showNotice) {
                    showShort("下载失败：${e.message}")
                }
            }
            Log.i(TAG, "run: ${e.message}")
        }

    }


    /**
     * 下载进度通知
     */
    private fun showDownLoadNotification(progress: Int, usedTimeMillis: Int, downloadSpeed: Int) {
        val notificationManager =
            mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val build = NotificationCompat.Builder(mContext, AppConstant.NoticeChannel.DOWNLOAD)
            .setContentTitle("下载速度：$downloadSpeed Kb/s $progress% 用时：$usedTimeMillis/s")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setProgress(100, progress, true)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, build)
    }

    /**
     * 下载完成通知
     */
    private fun showCompleteNotification(file: File) {
        val notificationManager =
            mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val build = NotificationCompat.Builder(mContext, AppConstant.NoticeChannel.DOWNLOAD)
            .setContentText("下载完成：${file.absolutePath}")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    mContext,
                    0,
                    Intent(Intent.ACTION_OPEN_DOCUMENT)
                        .setType("*/*")
                        .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        .setData(
                            FileProvider.getUriForFile(
                                mContext,
                                "com.yang.collection.fileProvider",
                                file
                            )
                        ),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .build()
        notificationManager.cancel(1)
        notificationManager.notify(2, build)
        downListener?.downSuccess(file.absolutePath)
    }


}