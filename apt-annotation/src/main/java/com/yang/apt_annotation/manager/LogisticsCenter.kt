package com.yang.apt_annotation.manager

/**
 * @Author Administrator
 * @ClassName LogisticsCenter
 * @Description
 * @Date 2022/1/14 10:45
 */
class LogisticsCenter {

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { LogisticsCenter() }

        val injectMap by lazy { mutableMapOf<String, String>() }

    }

    fun <T : Any> injectViewModel(className: T) {
        try {
            if (!injectMap.containsValue(className::class.qualifiedName)) {
                return
            }
            injectMap.forEach {
                if (it.value == className::class.qualifiedName) {
                    val forName = Class.forName(it.key)
                    val injectManager = forName.newInstance() as InjectManager<T>
                    injectManager.inject(className)
                    injectMap.remove(it.key)
                    return@forEach
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}