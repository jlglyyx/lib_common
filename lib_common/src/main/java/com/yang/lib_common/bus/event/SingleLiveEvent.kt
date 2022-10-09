package com.yang.lib_common.bus.event

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

open class SingleLiveEvent<T> : MutableLiveData<T>() {

    companion object{
        private const val TAG = "SingleLiveEvent"
    }

    private val mPending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer<T> {
            if (mPending.compareAndSet(true,false)){
                observer.onChanged(it)
            }
        })
    }

    override fun setValue(value: T?) {
        mPending.set(true)
        super.setValue(value)
    }

    fun call(){
        value = null
    }
}