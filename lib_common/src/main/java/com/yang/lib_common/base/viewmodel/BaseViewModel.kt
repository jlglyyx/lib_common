package com.yang.lib_common.base.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.yang.lib_common.R
import com.yang.lib_common.app.BaseApplication
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.handle.ErrorHandle
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * ui状态控制器
     */
    var uC = UIChangeLiveData()

    /**
     * 广告Manager
     */
    var  mTTAdNative: TTAdNative? = TTAdSdk.getAdManager().createAdNative(application)


    /**
     * showDialog
     */
    fun showDialog(content: String = getString(R.string.string_requesting)) {
        uC.showLoadingEvent.value = content
    }

    /**
     * dismissDialog
     */
    fun dismissDialog() {
        uC.dismissDialogEvent.call()
    }

    /**
     * 关闭刷新
     */
    fun cancelRefresh() {
        uC.refreshEvent.call()
    }

    /**
     * 关闭加载
     */
    fun cancelLoadMore() {
        uC.loadMoreEvent.call()
    }

    /**
     * 关闭刷新和加载
     */
    fun cancelRefreshLoadMore() {
        uC.refreshEvent.call()
        uC.loadMoreEvent.call()
    }

    /**
     * 请求成功
     */
    fun requestSuccess() {
        uC.requestSuccessEvent.call()
    }

    /**
     * 请求成功
     */
    fun requestSuccess(value: Any) {
        uC.requestSuccessEvent.postValue(value)
    }

    /**
     * 请求失败
     */
    fun requestFail() {
        uC.requestFailEvent.call()
    }

    /**
     * 请求失败
     */
    fun requestFail(value: Any) {
        uC.requestFailEvent.postValue(value)
    }

    /**
     * 结束activity
     */
    fun finishActivity() {
        uC.finishActivityEvent.call()
    }

    /**
     * 展示recyclerView 布局类型
     */
    fun showRecyclerViewEvent(type: Int) {
        uC.showRecyclerViewEvent.postValue(type)
    }

    /**
     * 展示recyclerView 加载失败
     */
    fun showRecyclerViewErrorEvent() {
        uC.showRecyclerViewEvent.postValue(AppConstant.LoadingViewEnum.ERROR_VIEW)
    }

    /**
     * 展示recyclerView 加载空数据
     */
    fun showRecyclerViewEmptyEvent() {
        uC.showRecyclerViewEvent.postValue(AppConstant.LoadingViewEnum.EMPTY_VIEW)
    }

    /**
     * 延时
     */
    suspend fun delayTime(timeMillis: Long = 1000) {
        delay(timeMillis)
    }

    /**
     * 延时**后消失dialog
     */
    suspend fun delayMissDialog(timeMillis: Long = 1000) {
        delayTime(timeMillis)
        dismissDialog()
    }

    /**
     * 获取资源文字
     */
    fun getString(@StringRes resId: Int): String {
        return getApplication<BaseApplication>().getString(resId)
    }



    /**
     * 请求
     * @param onRequest 请求
     * @param onSuccess 请求成功
     * @param error 请求失败
     * @param messages dialog 请求开始 请求成功 请求失败提示
     * @param errorDialog 是否展示统一处理失败dialog
     */
    fun <T> launch(
        onRequest: suspend () -> T,
        onSuccess: suspend (t: T) -> Unit = {},
        onError: suspend (t: Throwable) -> Unit = {},
        errorDialog: Boolean = true,
        vararg messages: String = arrayOf()
    ) {
        viewModelScope.launch {
            try {
                requestDialogMessage(0, *messages)
                onSuccess(onRequest())
                requestDialogMessage(1, *messages)
                dismissDialog()
            } catch (t: Throwable) {
                onError(t)
                if (errorDialog) {
                    handleException(t, if (messages.size >= 3) { messages[2] } else { "" })
                }
            }
        }
    }

    /**
     * 改变请求弹框文字
     */
    private suspend fun requestDialogMessage(index: Int, vararg messages: String) {
        val size = messages.size
        if (index >= size) {
            return
        }
        showDialog(messages[index])
        delayTime()
    }

    /**
     * 统一处理请求失败
     */
    private suspend fun handleException(exception: Throwable, content: String = "") {
        val handle = ErrorHandle(exception).handle()
        if (TextUtils.isEmpty(content)) {
            showDialog(handle)
        } else {
            showDialog(content)
        }
        delayTime(1000)
        dismissDialog()
    }


    override fun onCleared() {
        super.onCleared()
        mTTAdNative = null
        viewModelScope.cancel()
    }
}