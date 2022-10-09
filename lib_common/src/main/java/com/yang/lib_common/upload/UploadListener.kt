package com.yang.lib_common.upload

/**
 * @Author Administrator
 * @ClassName Upload
 * @Description
 * @Date 2021/11/19 9:23
 */
interface UploadListener {

    fun onProgress(id: String, progress: Int)

    fun onSuccess(id: String)

    fun onFailed(id: String)
}