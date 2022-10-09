@file:JvmName("ActivityStackManager")

package com.yang.lib_common.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.util.*


private val activityStack: Stack<AppCompatActivity> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    Stack<AppCompatActivity>()
}
private val fragmentStack: Stack<Fragment> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    Stack<Fragment>()
}


fun addActivity(activity: AppCompatActivity) {
    activityStack.add(activity)
}

fun removeActivity(activity: AppCompatActivity) {
    activityStack.remove(activity)
}


fun removeAllActivity() {
    activityStack.let {
        for (activity in it) {
            activity.finish()
        }
    }
}


fun addFragment(fragment: Fragment) {
    fragmentStack.add(fragment)
}