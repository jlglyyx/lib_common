package com.yang.lib_common.dialog

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.huawei.hms.hmsscankit.ScanUtil
import com.lxj.xpopup.core.BottomPopupView
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.databinding.DialogConfirmBinding
import com.yang.lib_common.databinding.DialogShareBinding
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.getDefaultMMKV
import com.yang.lib_common.util.toJson

/**
 * @ClassName: ShareDialog
 * @Description:
 * @Author: yxy
 * @Date: 2022/8/4 17:34
 */
class ShareDialog(context: Context) : BottomPopupView(context) {

    private val mBinding by lazy {
        DialogShareBinding.bind(bottomPopupContainer.getChildAt(0))
    }

    private var buildBitmap: Bitmap? = null

    var onItemClickListener:OnItemClickListener? = null

    interface OnItemClickListener{

        fun onCancelClickListener()

        fun onConfirmClickListener(type:Int)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_share
    }

    override fun onCreate() {
        super.onCreate()
        if (null == buildBitmap) {
            buildBitmap = ScanUtil.buildBitmap("sssssssssss", 500, 500)
            mBinding.ivErCode.setImageBitmap(buildBitmap)
        }
        mBinding.tvCancel.clicks().subscribe {
            onItemClickListener?.onCancelClickListener()
            dismiss()
        }
        mBinding.llQq.clicks().subscribe {
            onItemClickListener?.onConfirmClickListener(0)
            dismiss()
        }
        mBinding.llWeChat.clicks().subscribe {
            onItemClickListener?.onConfirmClickListener(1)
            dismiss()
        }
        mBinding.llCopy.clicks().subscribe {
            onItemClickListener?.onConfirmClickListener(2)
            dismiss()
        }
    }
}