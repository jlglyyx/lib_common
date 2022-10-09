package com.yang.lib_common.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log

/**
 * @Author Administrator
 * @ClassName DaemonService
 * @Description
 * @Date 2021/12/13 10:43
 */
class DaemonRemoteService : Service() {

    private val daemonBinder: DaemonBinder by lazy {
         DaemonBinder()
    }

    private val daemonConnection: DaemonService.DaemonConnection by lazy {
        DaemonService().DaemonConnection()
    }
    override fun onBind(intent: Intent?): IBinder {

        return daemonBinder
    }

    class DaemonBinder : Binder() {

    }

    override fun onCreate() {
        super.onCreate()
        bindService(Intent(this@DaemonRemoteService,DaemonService::class.java),daemonConnection,BIND_AUTO_CREATE)
    }


    inner class DaemonRemoteConnection : ServiceConnection{

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("TAG", "onServiceConnected: DaemonService 服务连接")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("TAG", "onServiceConnected: DaemonService 服务关闭")
            bindService(Intent(this@DaemonRemoteService,DaemonService::class.java),daemonConnection,BIND_AUTO_CREATE)
        }

    }
}