package com.yang.lib_common.down

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.buildARouter
import java.io.File

/**
 * @Author Administrator
 * @ClassName DownLoadManagerService
 * @Description
 * @Date 2021/12/3 16:22
 */
class DownLoadManagerService : Service() {

    companion object {
        private const val TAG = "DownLoadManagerService"

        private const val DOWN_SERVICE_ID = 100

        private const val DOWN_SUCCESS_ID = 101
    }

    private lateinit var downLoadManagerBinder: DownLoadManagerBinder

    override fun onBind(intent: Intent?): IBinder {
        return downLoadManagerBinder
    }


    inner class DownLoadManagerBinder : Binder(), DownLoadManagerListener {

        fun startDown(builder: DownLoadManager.Builder) {
            builder.downLoadManagerListener(this)
            builder.build()
        }

        override fun onSuccess(downloadPath: String, time: Long) {
            Log.i(TAG, "onSuccess: $downloadPath 用时:${time / 1000f}秒")
            stopForeground(true)
            //startForeground(DOWN_SUCCESS_ID, showCompleteNotification(File(downloadPath)))
            showCompleteNotification(File(downloadPath))
        }

        override fun onFailed(errorMessage: String) {

        }

        override fun onPaused() {

        }

        override fun onCanceled() {

        }

        override fun onProgress(progress: Float, time: Long, speed: Long) {
            Log.i(TAG, "onProgress: ${progress * 100}%")
            startForeground(
                DOWN_SERVICE_ID,
                showDownLoadNotification((progress * 100).toInt(), (time/1000).toInt(), speed.toInt())
            )
        }

        override fun onChildProgress(name: String, progress: Float, time: Long, speed: Long) {
            Log.i(TAG, "onChildProgress $name 下载: ${progress * 100}%")
        }
    }

    override fun onCreate() {
        super.onCreate()
        downLoadManagerBinder = DownLoadManagerBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }


    /**
     * 下载进度通知
     */
    private fun showDownLoadNotification(
        progress: Int,
        usedTimeMillis: Int,
        downloadSpeed: Int
    ): Notification {
        return NotificationCompat.Builder(this, AppConstant.NoticeChannel.DOWNLOAD)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    DOWN_SERVICE_ID,
                    Intent(this, buildARouter(AppConstant.RoutePath.MAIN_ACTIVITY)::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setContentTitle("下载速度：$downloadSpeed Kb/s $progress% 用时：$usedTimeMillis/s")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setProgress(100, progress, true)
            .setAutoCancel(true)
            .build()
    }

    /**
     * 下载完成通知
     */
    private fun showCompleteNotification(file: File) {
        val systemService = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val build = NotificationCompat.Builder(this, AppConstant.NoticeChannel.DOWNLOAD)
            .setContentText("下载完成：${file.absolutePath}")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    DOWN_SUCCESS_ID,
                    Intent(Intent.ACTION_GET_CONTENT).setType("*/*"),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .build()
        systemService.notify(DOWN_SUCCESS_ID,build)
    }
}