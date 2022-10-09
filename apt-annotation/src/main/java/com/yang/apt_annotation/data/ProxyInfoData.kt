package com.yang.apt_annotation.data

/**
 * @Author Administrator
 * @ClassName ProxyInfo
 * @Description
 * @Date 2021/12/20 15:36
 */
class ProxyInfoData {

    /**
     * Inject("elementValue")
     * var elementName:Int
     */

    /**
     * 注解名 - 注解注解类型集合
     */
    var elementNameMap = mutableMapOf<String,String>()

    /**
     *  当前注解所在的类名
     */
    var className:String? = null

    /**
     * 注解传值
     */
    var elementValue:String = ""

}