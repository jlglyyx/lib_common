@file:JvmName("ARouterUtil")
package com.yang.lib_common.util

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter

/**
 * ARouter跳转
 */
fun buildARouter(path:String): Postcard {
    return ARouter.getInstance().build(path)
}
