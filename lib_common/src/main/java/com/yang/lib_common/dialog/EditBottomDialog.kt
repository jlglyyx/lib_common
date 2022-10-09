package com.yang.lib_common.dialog

import android.content.Context
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.yang.lib_common.R
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.showShort
import com.lxj.xpopup.core.BottomPopupView

class EditBottomDialog constructor(context: Context) : BottomPopupView(context) {


    lateinit var editText: EditText

    var dialogCallBack: DialogCallBack? = null

    interface DialogCallBack {
        fun getComment(s: String)
    }


    override fun getImplLayoutId(): Int {
        return R.layout.dialog_edit_bottom
    }

    override fun onShow() {
        super.onShow()
        editText = findViewById(R.id.et_comment)
        findViewById<Button>(R.id.btn_finish).clicks().subscribe {
            if (TextUtils.isEmpty(editText.text.toString())) {
                showShort("输入内容为空")
                return@subscribe
            }
            dialogCallBack?.let {
                it.getComment(editText.text.toString())
            }
            dismiss()
        }

    }


}