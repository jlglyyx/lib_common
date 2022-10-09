package com.yang.lib_common.adapter

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.yang.lib_common.R
import com.yang.lib_common.util.clicks


/**
 * @ClassName TabAndViewPagerAdapter
 *
 * @Description
 *
 * @Author 1
 *
 * @Date 2020/12/1 14:52
 */
class TabAndViewPagerAdapter(fragmentActivity: FragmentActivity, private val fragments: MutableList<Fragment>, private val titles: MutableList<String>) : FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}

class TabAndViewPagerFragmentAdapter(fragment: Fragment, private val fragments: MutableList<Fragment>, private val titles: MutableList<String>) : FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}

class ImageViewPagerAdapter(var data: MutableList<String>) : RecyclerView.Adapter<ImageViewPagerAdapter.ImageViewPagerViewHolder>() {

    var clickListener:ClickListener? = null

    interface ClickListener{
        fun onClickListener(view:View,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewPagerAdapter.ImageViewPagerViewHolder {
        val shapeAbleImageView = ShapeableImageView(parent.context)
        shapeAbleImageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        //shapeAbleImageView.scaleType = ImageView.ScaleType.FIT_CENTER
        shapeAbleImageView.setBackgroundColor(Color.BLACK)
        return ImageViewPagerViewHolder(shapeAbleImageView)
    }

    override fun onBindViewHolder(holder: ImageViewPagerAdapter.ImageViewPagerViewHolder, position: Int) {
        holder.shapeAbleImageView.clicks().subscribe {
            clickListener?.onClickListener(holder.shapeAbleImageView,position)
        }
        Glide.with(holder.shapeAbleImageView)
            .load(data[position])
            .error(R.drawable.iv_image_error)
            .fitCenter()
            .placeholder(R.drawable.iv_image_placeholder)
            .into(holder.shapeAbleImageView)
    }

    override fun getItemCount(): Int {

        return data.size
    }





    inner class ImageViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var shapeAbleImageView: ShapeableImageView = itemView as ShapeableImageView

    }
}