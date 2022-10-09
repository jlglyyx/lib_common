package com.yang.apt_annotation.adapter

import com.yang.apt_annotation.processor.IProcessor
import com.yang.apt_annotation.processor.ProcessorCreate

/**
 * @Author Administrator
 * @ClassName ProcessorFactory
 * @Description
 * @Date 2021/12/21 14:27
 */
class ProcessorAdapter {

    companion object {
        const val MODULE_MAIN = "module_main"
        const val MODULE_MINE = "module_mine"
        const val MODULE_VIDEO = "module_video"
        const val MODULE_PICTURE = "module_picture"
        const val MODULE_LOGIN = "module_login"
    }

    fun getProcessor(type: String): IProcessor {
        var mark = "login"
        when {
            type.contains(MODULE_LOGIN) -> {
                mark = "login"
            }
            type.contains(MODULE_MAIN) -> {
                mark = "main"
            }
            type.contains(MODULE_MINE) -> {
                mark = "mine"
            }
            type.contains(MODULE_VIDEO) -> {
                mark = "video"
            }
            type.contains(MODULE_PICTURE) -> {
                mark = "picture"
            }
        }
        val mMark = captureName(mark)
        return ProcessorCreate(mMark, "m$mMark", mark)
    }


    /**
     * 首字母大写
     */
    private fun captureName(b: String): String {
        val toCharArray = b.toCharArray()
        toCharArray[0] = toCharArray[0] - 32
        return String(toCharArray)
    }

}