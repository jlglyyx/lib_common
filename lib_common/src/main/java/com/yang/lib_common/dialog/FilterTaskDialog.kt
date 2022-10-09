package com.yang.lib_common.dialog

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.RadioGroup
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lxj.xpopup.core.BottomPopupView
import com.yang.lib_common.R
import com.yang.lib_common.databinding.DialogFilterTaskBinding
import com.yang.lib_common.databinding.DialogImageViewpagerBinding
import com.yang.lib_common.util.LocationUtil
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.getScreenPx

/**
 * @Author Administrator
 * @ClassName FilterTaskDialog
 * @Description
 * @Date 2021/11/23 11:59
 */
class FilterTaskDialog(context: Context) : BottomPopupView(context) {


    private val mBinding by lazy {
        DialogFilterTaskBinding.bind(bottomPopupContainer.getChildAt(0))
    }
    override fun getImplLayoutId(): Int {
        return R.layout.dialog_filter_task
    }

    override fun onCreate() {
        super.onCreate()

        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

        }

        mBinding.tvCancel.clicks().subscribe {
            clear()
        }

        mBinding.tvConfirm.clicks().subscribe {
            dismiss()
        }
    }

    override fun getMaxHeight(): Int {
        return getScreenPx(context)[1]/5*3
    }


    private fun clear(){
        mBinding.radioGroup.clearCheck()
        mBinding.etPriceStart.setText("")
        mBinding.etPriceEnd.setText("")
    }


    override fun onDismiss() {
        super.onDismiss()
    }


}