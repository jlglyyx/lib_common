@file:JvmName("ToastShowUtil")

package com.yang.lib_common.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import com.yang.lib_common.app.BaseApplication
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


/**
 * @ClassName ToastShowUtil
 *
 * @Description
 *
 * @Author 1
 *
 * @Date 2020/12/2 9:38
 */
private var toast: Toast? = null
private val context: Context = BaseApplication.baseApplication

@SuppressLint("ShowToast")
fun showShort(msg: Any) {
    MainScope().launch {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
            toast?.setText(msg.toString())
        } else {
            toast!!.setText(msg.toString())
            toast!!.duration = Toast.LENGTH_SHORT
        }
        toast?.show()
    }
}

@SuppressLint("ShowToast")
fun showLong(msg: Any) {
    MainScope().launch {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_LONG)
            toast?.setText(msg.toString())
        } else {
            toast!!.setText(msg.toString())
            toast!!.duration = Toast.LENGTH_LONG
        }
        toast?.show()
    }
}