package com.yang.apt_processor

import com.google.auto.service.AutoService
import com.yang.apt_annotation.adapter.ProcessorAdapter
import com.yang.apt_annotation.annotain.InjectViewModel
import com.yang.apt_annotation.data.ProxyInfoData
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class Processor : AbstractProcessor() {

    private var proxyInfoMap = mutableMapOf<String, ProxyInfoData>()

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val mutableSetOf = mutableSetOf<String>()
        mutableSetOf.add(InjectViewModel::class.java.canonicalName)
        return mutableSetOf
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return processingEnv.sourceVersion
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        try {
            proxyInfoMap.clear()
            val elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(InjectViewModel::class.java)
            /*获取注解信息*/
            elementsAnnotatedWith.forEach { element ->
                val enclosingElement = element.enclosingElement as TypeElement
                /*注解所在全限定类名*/
                val qualifiedName = enclosingElement.qualifiedName.toString()
                var proxyInfo = proxyInfoMap[qualifiedName]
                if (null == proxyInfo){
                    proxyInfo = ProxyInfoData()
                }
                /*注解注解类 类型*/
                val elementQualifiedName = element.asType()
                /*注解*/
//                val annotation = element.getAnnotation(InjectViewModel::class.java)
//                /*注解值*/
//                proxyInfo.elementValue = annotation.value
                /*注解所在简单类名*/
                proxyInfo.className = enclosingElement.simpleName.toString()
                /*注解名集合*/
                proxyInfo.elementNameMap[element.toString()] = elementQualifiedName.toString()

                proxyInfoMap[qualifiedName] = proxyInfo

            }
            /*生成类文件*/
            proxyInfoMap.forEach { (className, proxyInfo) ->
                    ProcessorAdapter().getProcessor(className).createProcessor(className,proxyInfo,processingEnv)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }
}