package com.yang.lib_common.dialog

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lxj.xpopup.core.BottomPopupView
import com.yang.lib_common.R
import com.yang.lib_common.util.LocationUtil
import com.yang.lib_common.util.getScreenPx
import kotlinx.android.synthetic.main.dialog_search_recycler_view.view.*

/**
 * @Author Administrator
 * @ClassName SearchRecyclerViewDialog
 * @Description
 * @Date 2021/11/23 11:59
 */
class SearchRecyclerViewDialog(context: Context) : BottomPopupView(context) {


    private lateinit var itemAdapter:ItemAdapter

    var searchRecyclerViewDialogCallBack:SearchRecyclerViewDialogCallBack? = null

    private var list = mutableListOf<String>()

    private var  inputTipsListener : Inputtips.InputtipsListener? = null

    private var locationUtil:LocationUtil? = null


    interface SearchRecyclerViewDialogCallBack {

        fun getText(s: String)
    }

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_search_recycler_view
    }

    override fun onCreate() {
        super.onCreate()
        initRecyclerView()
        initInput()
        locationUtil = LocationUtil(context)
        locationUtil?.apply {
            locationListener = object : LocationUtil.LocationListener{
                override fun onLocationListener(aMapLocation: AMapLocation) {
                    itemAdapter.addData(aMapLocation.district+aMapLocation.address)
                }

            }
            startLocation()
        }
    }

    override fun getMaxHeight(): Int {
        return getScreenPx(context)[1]/5*3
    }

    private fun initRecyclerView(){
        itemAdapter = ItemAdapter(android.R.layout.simple_spinner_dropdown_item, list).apply {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.getItem(position).toString()
                searchRecyclerViewDialogCallBack?.getText(item)
                dismiss()
            }
        }
        recyclerView.adapter = itemAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun initInput(){
         if (inputTipsListener == null){
             inputTipsListener = Inputtips.InputtipsListener { p0, p1 ->
                 list.clear()
                 p0?.forEach {
                     list.add("${it.district}${it.address}")
                 }
                 itemAdapter.replaceData(list)
             }
         }
        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s.toString())){
                    return
                }
                //第二个参数传入null或者“”代表在全国进行检索，否则按照传入的city进行检索
                val inputTipsQuery = InputtipsQuery(s.toString(), null)
                //限制在当前城市
                //inputTipsQuery.cityLimit = true
                val inputTips = Inputtips(context, inputTipsQuery)
                inputTips.setInputtipsListener(inputTipsListener)
                inputTips.requestInputtipsAsyn()
                Log.i("TAG", "onTextChanged: $s")
            }

            override fun afterTextChanged(s: Editable) {

                Log.i("TAG", "afterTextChanged: $s")
            }

        })


    }


    inner class ItemAdapter(layoutResId:Int,data: MutableList<String>?) :
        BaseQuickAdapter<String, BaseViewHolder>(layoutResId,data) {

        override fun convert(helper: BaseViewHolder, item: String) {
            helper.setText(android.R.id.text1,item)
        }

    }


    override fun onDismiss() {
        super.onDismiss()
        inputTipsListener = null
        locationUtil?.stopLocation()
        locationUtil?.onDestroy()
        locationUtil = null
    }


}