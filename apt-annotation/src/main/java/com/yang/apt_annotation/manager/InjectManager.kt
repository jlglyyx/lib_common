package com.yang.apt_annotation.manager

/**
 * @Author Administrator
 * @ClassName InjectManager
 * @Description
 * @Date 2021/12/20 16:36
 */
interface InjectManager<T> {
    fun inject(t:T)
}