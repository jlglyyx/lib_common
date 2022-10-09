package com.yang.lib_common.util

import android.content.Context
import android.util.Log
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk

/**
 * @Author Administrator
 * @ClassName TTAdManagerUtil
 * @Description
 * @Date 2022/1/12 14:33
 */
class TTAdManagerUtil {

    companion object {
        private const val TAG = "TTAdManagerUtil"

        private var isInit = false

        val instance: TTAdManagerUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            TTAdManagerUtil()
        }

    }
    fun initAd(context: Context) {
        if (isInit) {
            return
        }
        TTAdSdk.init(
            context, TTAdConfig.Builder().appId("5263197")
                .useTextureView(true) //默认使用SurfaceView播放视频广告,当有SurfaceView冲突的场景，可以使用TextureView
                .appName("收集者")
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)//落地页主题
                .allowShowNotify(true) //是否允许sdk展示通知栏提示,若设置为false则会导致通知栏不显示下载进度
                .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合,没有设置的网络下点击下载apk会有二次确认弹窗，弹窗中会披露应用信息
                .supportMultiProcess(false) //是否支持多进程，true支持
                .asyncInit(true) //是否异步初始化sdk,设置为true可以减少SDK初始化耗时。3450版本开始废弃~~ //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build(), object : TTAdSdk.InitCallback {
                override fun success() {
                    Log.i(TAG, "success: 广告sdk初始化成功")
                    isInit = true
                }

                override fun fail(p0: Int, p1: String?) {
                    Log.i(TAG, "success: 广告sdk初始化失败 $p0  $p1")
                    isInit = false
                }
            })
    }

}