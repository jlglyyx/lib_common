package com.yang.lib_common.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yang.lib_common.R

/**
 * @ClassName: PictureAdapter
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/29 16:50
 */
class PictureAdapter (data: MutableList<String>?) : BaseQuickAdapter<String, BaseViewHolder>( data) {
    init {
        mLayoutResId = R.layout.view_item_grid_nine_picture
    }
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setVisible(R.id.iv_play,item.endsWith(".mp4"))
        val imageView = helper.getView<ImageView>(R.id.iv_nine_image)
        if (item.endsWith(".mp4")){
            Glide.with(mContext)
                .setDefaultRequestOptions(RequestOptions().frame(1000))
                .load(item)
                .centerCrop()
                .error(R.drawable.iv_image_error)
                .placeholder(R.drawable.iv_image_placeholder)
                .into(imageView)
        }else{
            Glide.with(mContext)
                .load(item)
                .centerCrop()
                .error(R.drawable.iv_image_error)
                .placeholder(R.drawable.iv_image_placeholder)
                .into(imageView)
        }
    }
}