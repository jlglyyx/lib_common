package com.yang.lib_common.bus.event

import android.text.TextUtils
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.reflect.Field
import java.lang.reflect.Method


/**
 * @Author Administrator
 * @ClassName LiveDataBus
 * @Description
 * @Date 2021/10/29 16:59
 */
class LiveDataBus {

    private var bus: MutableMap<String, BusMutableLiveData<Any>> = mutableMapOf()

    companion object{

        val instance:LiveDataBus by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){
            LiveDataBus()
        }
    }

    fun <T> with(key: String, type: Class<T>): BusMutableLiveData<T> {
        if (!bus.containsKey(key)) {
            bus[key] = BusMutableLiveData()
        }
        return bus[key] as BusMutableLiveData<T>
    }

    fun with(key: String): BusMutableLiveData<Any> {
        return with(key, Any::class.java)
    }



    private inner class ObserverWrapper<T>(var observer: Observer<T>) : Observer<T> {

        override fun onChanged(t: T) {
            if (isCallOnObserve()) {
                return
            }
            observer.onChanged(t)
        }

        private fun isCallOnObserve(): Boolean {
            val stackTrace = Thread.currentThread().stackTrace
            if (stackTrace.isNotEmpty()) {
                for (element in stackTrace) {
                    if (TextUtils.equals(
                            "android.arch.lifecycle.LiveData",
                            element.className
                        ) && TextUtils.equals(
                            "observeForever",
                            element.methodName
                        )
                    ) {
                        return true
                    }
                }
            }
            return false
        }
    }


    inner class BusMutableLiveData<T> : MutableLiveData<T>() {

        private val observerMap: MutableMap<Observer<*>, Observer<*>> = mutableMapOf()

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            try {
                hook(observer)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        override fun observeForever(observer: Observer<in T>) {
            if (!observerMap.containsKey(observer)) {
                observerMap[observer] = ObserverWrapper(observer)
            }
            super.observeForever(observerMap[observer] as Observer<in T>)
        }

        override fun removeObserver(observer: Observer<in T>) {
            val realObserver: Observer<in T> = if (observerMap.containsKey(observer)) {
                observerMap.remove(observer) as Observer<in T>
            } else {
                observer
            }
            super.removeObserver(realObserver)
        }
        /**
         * 更新 mVersion
         */
        private fun hook(@NonNull observer: Observer<in T>) {
            val classLiveData = LiveData::class.java
            val fieldObservers: Field = classLiveData.getDeclaredField("mObservers")
            fieldObservers.isAccessible = true
            val objectObservers: Any = fieldObservers.get(this)
            val classObservers: Class<*> = objectObservers.javaClass
            val methodGet: Method = classObservers.getDeclaredMethod("get", Any::class.java)
            methodGet.isAccessible = true
            val objectWrapperEntry: Any = methodGet.invoke(objectObservers, observer)
            var objectWrapper: Any? = null
            if (objectWrapperEntry is Map.Entry<*, *>) {
                objectWrapper = objectWrapperEntry.value
            }
            if (objectWrapper == null) {
                throw NullPointerException("Wrapper can not be bull!")
            }
            val classObserverWrapper: Class<*>? = objectWrapper.javaClass.superclass
            val fieldLastVersion: Field = classObserverWrapper!!.getDeclaredField("mLastVersion")
            fieldLastVersion.isAccessible = true
            val fieldVersion: Field = classLiveData.getDeclaredField("mVersion")
            fieldVersion.isAccessible = true
            val objectVersion: Any = fieldVersion.get(this)
            fieldLastVersion.set(objectWrapper, objectVersion)
        }
    }

}