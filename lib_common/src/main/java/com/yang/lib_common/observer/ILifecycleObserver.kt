package com.yang.lib_common.observer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * @Author Administrator
 * @ClassName ILifecycleObserver
 * @Description
 * @Date 2021/11/10 16:47
 */
interface ILifecycleObserver: LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy()
}