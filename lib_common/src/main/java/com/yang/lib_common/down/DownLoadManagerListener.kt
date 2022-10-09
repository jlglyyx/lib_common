package com.yang.lib_common.down


/**
 * @ClassName DownListener
 *
 * @Description
 *
 * @Author 1
 *
 * @Date 2020/12/24 17:52
 */
interface DownLoadManagerListener {

    fun onSuccess(downloadPath:String,time:Long)

    fun onFailed(errorMessage:String)

    fun onPaused()

    fun onCanceled()

    fun onProgress(progress:Float,time: Long,speed:Long)

    fun onChildProgress(name:String,progress:Float,time: Long,speed:Long)
}