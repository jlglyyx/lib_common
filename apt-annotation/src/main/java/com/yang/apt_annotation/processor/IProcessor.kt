package com.yang.apt_annotation.processor

import com.yang.apt_annotation.data.ProxyInfoData
import javax.annotation.processing.ProcessingEnvironment

/**
 * @Author Administrator
 * @ClassName IProcessor
 * @Description
 * @Date 2021/12/21 14:32
 */
interface IProcessor {

    fun createProcessor(className:String, proxyInfoData: ProxyInfoData, processingEnv: ProcessingEnvironment)

}