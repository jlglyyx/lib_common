package com.yang.lib_common.util

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup

/**
 * @ClassName: ViewLayoutChangeUtil
 * @Description:
 * @Author: yxy
 * @Date: 2022/6/8 15:02
 */
class ViewLayoutChangeUtil {

    var currentHeight = 0

    lateinit var layoutParams:ViewGroup.LayoutParams

    fun add(rootView: View){
        val rect = Rect()
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            rootView.getWindowVisibleDisplayFrame(rect)
            if (currentHeight != rect.bottom){
                layoutParams.height = rect.bottom
                rootView.requestLayout()
                currentHeight = rect.bottom
            }
        }
        layoutParams = rootView.layoutParams
    }

}