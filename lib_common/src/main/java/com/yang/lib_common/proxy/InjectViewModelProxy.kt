package com.yang.lib_common.proxy

import com.yang.apt_annotation.manager.InjectManager
import org.jetbrains.annotations.NotNull

/**
 * @Author Administrator
 * @ClassName InjectViewModelProxy
 * @Description
 * @Date 2021/12/21 9:17
 */

class InjectViewModelProxy {

    companion object {

        private const val TAG = "InjectViewModelProxy"

        @Suppress("UNCHECKED_CAST")
        fun inject(@NotNull any: Any) {
            try {
                val java = any::class.java.simpleName
                val className = "com.yang.processor." + java + "_InjectViewModel"
                val forName = Class.forName(className)
                val injectManager: InjectManager<Any> = forName.newInstance() as InjectManager<Any>
                injectManager.inject(any)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}