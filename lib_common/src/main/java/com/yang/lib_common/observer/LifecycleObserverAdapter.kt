package com.yang.lib_common.observer

/**
 * @Author Administrator
 * @ClassName ILifecycleObserver
 * @Description
 * @Date 2021/11/10 16:40
 */
abstract class LifecycleObserverAdapter: ILifecycleObserver {

    override fun onResume(){}

    override fun onPause(){}

    override fun onDestroy(){}



}