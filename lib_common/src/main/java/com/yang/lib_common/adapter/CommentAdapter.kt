package com.yang.lib_common.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.data.CommentData

/**
 * @Author Administrator
 * @ClassName CommentAdapter
 * @Description
 * @Date 2021/11/25 11:12
 */
class CommentAdapter(data: MutableList<CommentData>?) :
    BaseMultiItemQuickAdapter<CommentData, BaseViewHolder>(data) {
    private var childSize = 0
    init {
        addItemType(AppConstant.Constant.PARENT_COMMENT_TYPE, R.layout.item_comment)
        addItemType(AppConstant.Constant.CHILD_COMMENT_TYPE, R.layout.item_child_comment)
        addItemType(
            AppConstant.Constant.CHILD_REPLY_COMMENT_TYPE,
            R.layout.item_child_reply_comment
        )
    }

    override fun convert(helper: BaseViewHolder, item: CommentData) {

        helper.setText(R.id.tv_comment, item.comment)
        val sivImg = helper.getView<ShapeableImageView>(R.id.siv_img)
        Glide.with(sivImg)
            .load("https://img1.baidu.com/it/u=1834859148,419625166&fm=26&fmt=auto&gp=0.jpg")
            .error(R.drawable.iv_image_error)
            .placeholder(R.drawable.iv_image_placeholder)
            .into(sivImg)


        when (item.mLevel) {
            AppConstant.Constant.PARENT_COMMENT_TYPE -> {
                helper.setGone(R.id.tv_open_comment, !item.isExpanded)
                helper.setGone(
                    R.id.tv_open_comment,
                    !(null == item.subItems || item.subItems.size == 0 || item.isExpanded)
                )
                childSize = 0
                getSubItemsSize(item)
                helper.setText(R.id.tv_open_comment, "-----展开${childSize}条评论-----")
                helper.itemView.setOnClickListener {
                    if (item.isExpanded) {
                        collapse(helper.layoutPosition)
                    } else {
                        expand(helper.layoutPosition)
                    }
                }
            }
            AppConstant.Constant.CHILD_COMMENT_TYPE -> {
                when (item.mItemType) {
                    AppConstant.Constant.CHILD_COMMENT_TYPE -> {

                    }
                    AppConstant.Constant.CHILD_REPLY_COMMENT_TYPE -> {
                        helper.addOnClickListener(R.id.siv_reply_img)
                        val sivReplyImg = helper.getView<ShapeableImageView>(R.id.siv_reply_img)
                        Glide.with(sivReplyImg)
                            .load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202011%2F17%2F20201117105437_45d41.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1640489453&t=5685c55865958fe47ba34bbfe0b91405")
                            .error(R.drawable.iv_image_error)
                            .placeholder(R.drawable.iv_image_placeholder)
                            .into(sivReplyImg)
                    }
                }
            }
        }

        helper.addOnClickListener(R.id.siv_img)
            .addOnClickListener(R.id.tv_reply)
    }

    private fun getSubItemsSize(item: CommentData){
        if (item.hasSubItem()){
            childSize += item.subItems.size
            item.subItems.forEach {
                if (it.hasSubItem()){
                    getSubItemsSize(it)
                }
            }
        }
    }
}