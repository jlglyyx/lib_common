package com.yang.lib_common.dialog

import android.content.Context
import android.view.LayoutInflater
import com.loc.fa
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.core.CenterPopupView
import com.yang.lib_common.R
import com.yang.lib_common.databinding.DialogFilterTaskBinding
import com.yang.lib_common.databinding.DialogPayTaskBinding
import com.yang.lib_common.util.clicks

/**
 * @ClassName: PayTaskDialog
 * @Description:
 * @Author: yxy
 * @Date: 2022/6/8 15:38
 */
class PayTaskDialog(context: Context) : BottomPopupView(context) {

    private val mBinding by lazy {
        DialogPayTaskBinding.bind(bottomPopupContainer.getChildAt(0))
    }

    var onItemClickListener:OnItemClickListener? = null

    interface OnItemClickListener{

        fun onCancelClickListener()

        fun onConfirmClickListener()
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_pay_task
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