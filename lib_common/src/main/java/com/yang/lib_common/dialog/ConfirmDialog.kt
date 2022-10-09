package com.yang.lib_common.dialog

import android.content.Context
import android.view.LayoutInflater
import com.lxj.xpopup.core.CenterPopupView
import com.yang.lib_common.R
import com.yang.lib_common.databinding.DialogConfirmBinding
import com.yang.lib_common.util.clicks

/**
 * @ClassName: ConfirmDialog
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/29 15:24
 */
class ConfirmDialog(context: Context) : CenterPopupView(context) {

    private val mBinding by lazy {
        DialogConfirmBinding.bind(contentView)
    }

    var onItemClickListener:OnItemClickListener? = null

    interface OnItemClickListener{

        fun onCancelClickListener()

        fun onConfirmClickListener()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_confirm
    }


    override fun onCreate() {
        super.onCreate()

        mBinding.tvCancel.clicks().subscribe {
            onItemClickListener?.onCancelClickListener()
            dismiss()
        }
        mBinding.tvConfirm.clicks().subscribe {
            onItemClickListener?.onConfirmClickListener()
        }
    }


    override fun onDismiss() {
        super.onDismiss()
    }
}