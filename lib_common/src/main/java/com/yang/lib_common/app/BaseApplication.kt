package com.yang.lib_common.app

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.yang.lib_common.BuildConfig
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.handle.CrashHandle
import com.yang.lib_common.helper.getRemoteComponent
import com.yang.lib_common.service.DaemonRemoteService
import com.yang.lib_common.service.DaemonService
import com.yang.lib_common.util.NetworkUtil
import com.yang.lib_common.util.TTAdManagerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager
import kotlin.system.measureTimeMillis


class BaseApplication : Application() ,Application.ActivityLifecycleCallbacks{

    private var isFirstActivity = true

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        baseApplication = this
        getRemoteComponent()
        initCrashReport(baseApplication)
        initARouter(baseApplication)
        initGlide(baseApplication)
        initMMKV(baseApplication)
        initNetworkStatusListener(baseApplication)
        initVideo()
        initWebView()
        initCSJAdvertise()
    }

    companion object {
        private const val TAG = "BaseApplication"

        lateinit var baseApplication: BaseApplication
    }

    private fun initService() {
        startService(Intent(this, DaemonRemoteService::class.java))
        startService(Intent(this, DaemonService::class.java))
    }

    private fun initARouter(application: BaseApplication) {
        val measureTimeMillis = measureTimeMillis {
            CoroutineScope(Dispatchers.Unconfined)
                .launch {
                    if (BuildConfig.DEBUG) {
                        ARouter.openLog()
                        ARouter.openDebug()
                    }
                    ARouter.init(application)
                }
        }
        Log.i(TAG, "initARouter: ==>${measureTimeMillis}")
    }

    private fun initCrashReport(application: BaseApplication) {
        val measureTimeMillis = measureTimeMillis {
            CoroutineScope(Dispatchers.IO).launch {
                delay(3000)
                CrashReport.initCrashReport(application, "4f807733a2", BuildConfig.DEBUG)
                createNotificationChannel()
            }
            Thread.setDefaultUncaughtExceptionHandler(CrashHandle.instance)
        }
        Log.i(TAG, "initCrashReport: ==>${measureTimeMillis}")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    AppConstant.NoticeChannel.DOWNLOAD,
                    "下载通知",
                    NotificationManager.IMPORTANCE_HIGH
                )
            val systemService =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            systemService.createNotificationChannel(notificationChannel)
        }
    }

    private fun initMMKV(application: BaseApplication) {
        val measureTimeMillis = measureTimeMillis {
            CoroutineScope(Dispatchers.IO).launch {
                val initialize = MMKV.initialize(application)
                Log.i("TAG", "initMMKV: $initialize")
            }
        }
        Log.i(TAG, "initMMKV: ==>${measureTimeMillis}")
    }

    private fun initGlide(application: BaseApplication) {
        val measureTimeMillis = measureTimeMillis {
            CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                delay(1000)
                Glide.get(application)
            }
        }
        Log.i(TAG, "initGlide: ==>${measureTimeMillis}")
    }

    private fun initNetworkStatusListener(application: BaseApplication) {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            NetworkUtil.getNetworkStatus()
        )
    }

    private fun initVideo() {
        val measureTimeMillis = measureTimeMillis {
            CoroutineScope(Dispatchers.IO).launch {
                delay(3000)
                PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
            }
        }
        Log.i(TAG, "initVideo: ==>${measureTimeMillis}")
    }


    private fun initWebView() {
        val measureTimeMillis = measureTimeMillis {
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                QbSdk.setDownloadWithoutWifi(true)
                //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
                //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
                val cb: PreInitCallback = object : PreInitCallback {
                    override fun onViewInitFinished(arg0: Boolean) {
                        //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                        Log.i("TAG", "onViewInitFinished: $arg0")
                    }

                    override fun onCoreInitFinished() {}
                }
                //x5内核初始化接口
                //x5内核初始化接口
                QbSdk.initX5Environment(applicationContext, cb)
            }
        }
        Log.i(TAG, "initWebView: ==>${measureTimeMillis}")

    }

    private fun initCSJAdvertise(){
        val measureTimeMillis = measureTimeMillis {
            CoroutineScope(Dispatchers.Main).launch {
                TTAdManagerUtil.instance.initAd(this@BaseApplication)
            }
        }
        Log.i(TAG, "initCSJAdvertise: ==>${measureTimeMillis}")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).onLowMemory()
        }
        Glide.get(this).onTrimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).onLowMemory()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.i(TAG, "onActivityCreated: ")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.i(TAG, "onActivityStarted: ")
        if (isFirstActivity){
            initService()
            isFirstActivity = false
            unregisterActivityLifecycleCallbacks(this)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        Log.i(TAG, "onActivityResumed: ")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.i(TAG, "onActivityPaused: ")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.i(TAG, "onActivityStopped: ")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.i(TAG, "onActivitySaveInstanceState: ")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.i(TAG, "onActivityDestroyed: ")
    }
}