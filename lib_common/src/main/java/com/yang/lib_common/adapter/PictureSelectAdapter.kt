package com.yang.lib_common.adapter

import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yang.lib_common.R
import com.yang.lib_common.data.MediaInfoBean
import java.text.DecimalFormat

/**
 * @Author Administrator
 * @ClassName PictureSelectAdapter
 * @Description
 * @Date 2021/8/6 14:39
 */
class PictureSelectAdapter(
    layoutResId: Int,
    data: MutableList<MediaInfoBean>,
    private val showSelect: Boolean = true) : BaseQuickAdapter<MediaInfoBean, BaseViewHolder>(layoutResId, data) {


    override fun convert(helper: BaseViewHolder, item: MediaInfoBean) {
        val ivImg = helper.getView<ImageView>(R.id.iv_image)
        if (showSelect) {
            helper.addOnClickListener(R.id.cl_cb)
            if (item.fileType == 1) {
                item.fileDurationTime?.let {
                    helper.setText(R.id.tv_time, secToTime(it))
                    helper.setVisible(R.id.tv_time, true)
                }

            } else {
                helper.setVisible(R.id.tv_time, false)
            }
            if (item.isSelect) {
                helper.setText(R.id.cb_image, "${item.selectPosition}")
                helper.setBackgroundRes(R.id.cb_image, R.drawable.shape_select_picture)
            } else {
                helper.setText(R.id.cb_image, "")
                helper.setBackgroundRes(R.id.cb_image, R.drawable.shape_no_select_picture)
            }
        } else {
            helper.setVisible(R.id.cl_cb, false)
            helper.setVisible(R.id.tv_time, false)
        }
        if (item.fileType == 1) {
            try {
                Glide.with(ivImg)
                    .setDefaultRequestOptions(RequestOptions().frame(1000).centerCrop())
                    .load(item.filePath)
                    .error(R.drawable.iv_image_error)
                    .placeholder(R.drawable.iv_image_placeholder)
                    .into(ivImg)
            }catch (e:Exception){
                Log.i(TAG, "convert: ssssssssssssssss     vvv ${e.message}")
            }
            helper.setVisible(R.id.iv_play, true)
        } else {
            try {
                Glide.with(ivImg)
                    .load(item.filePath)
                    .centerCrop()
                    .error(R.drawable.iv_image_error)
                    .placeholder(R.drawable.iv_image_placeholder)
                    .into(ivImg)
            }catch (e:Exception){
                Log.i(TAG, "convert: ssssssssssssssss   ppp ${e.message}")
            }

            helper.setVisible(R.id.iv_play, false)
        }


    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.i("TAG=ssss", "onViewAttachedToWindow: ")
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        Log.i("TAGs", "onAttachedToRecyclerView: ")
    }

    override fun bindToRecyclerView(recyclerView: RecyclerView?) {
        super.bindToRecyclerView(recyclerView)
        Log.i("TAGs", "bindToRecyclerView: ")
    }


    private fun secToTime(data: String): String {
        val time = data.toInt() / 1000
        val timeStr: String
        if (time <= 0) return "00:00:00" else {
            timeStr = "${unitFormat(time / 60 / 60 % 60)}:${unitFormat(time / 60 % 60)}:${unitFormat(time % 60)}"
        }
        return timeStr
    }

    private fun unitFormat(i: Int): String {
        return DecimalFormat("00").format(i)
    }
}