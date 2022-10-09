package com.yang.apt_annotation.data

/**
 * @Author Administrator
 * @ClassName InfoData
 * @Description
 * @Date 2022/1/14 11:56
 */
class InjectInfoData {

    var injectModule: String? = null

    var injectClassCls: String? = null

    var daggerComponentClassCls: String? = null

    var injectViewModelFactoryClassCls: String? = null

    var injectViewModelList = mutableMapOf<String,String>()

}