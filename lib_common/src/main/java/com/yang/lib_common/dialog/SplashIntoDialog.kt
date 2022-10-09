package com.yang.lib_common.dialog

import android.content.Context
import com.lxj.xpopup.impl.FullScreenPopupView
import com.yang.lib_common.R

/**
 * @ClassName: SplashIntoDialog
 * @Description:
 * @Author: Administrator
 * @Date: 2022/4/6 10:03
 */
class SplashIntoDialog(context: Context) : FullScreenPopupView(context) {


    override fun getImplLayoutId(): Int {
        return R.layout.dialog_splash_into
    }


    override fun onCreate() {
        super.onCreate()
        fullPopupContainer.setOnClickListener {
            dismiss()
        }
    }


    override fun onDismiss() {
        super.onDismiss()
    }
}