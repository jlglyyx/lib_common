package com.yang.lib_common.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lxj.xpopup.impl.FullScreenPopupView
import com.lxj.xpopup.photoview.PhotoView
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.yang.lib_common.R
import com.yang.lib_common.down.thread.MultiMoreThreadDownload
import com.yang.lib_common.util.clicks
import kotlinx.android.synthetic.main.dialog_image_viewpager.view.*


class ImageViewPagerDialog(
    context: Context,
    private var data: MutableList<String>,
    private var position: Int,
    private var showDown: Boolean = false,
    private var showCollection: Boolean = false
) : FullScreenPopupView(context) {

    companion object {
        private const val TAG = "ImageViewPagerDialog"
    }


    private var mContext: Context = context

    var imageViewPagerDialogCallBack: ImageViewPagerDialogCallBack? = null

    interface ImageViewPagerDialogCallBack {
        fun getPosition(position: Int)

        fun onViewClickListener(view: View)
    }


    override fun getImplLayoutId(): Int {
        return R.layout.dialog_image_viewpager
    }

    override fun onCreate() {
        super.onCreate()
        tv_count.text = "1/${data.size}"
        initViewPager()
        initToolbar()
    }

    private fun initToolbar() {

        iv_back.clicks().subscribe {
            imageViewPagerDialogCallBack?.onViewClickListener(this)
            dismiss()
        }
        if (showDown) {
            iv_down.visibility = View.VISIBLE
            iv_down.clicks().subscribe {
                imageViewPagerDialogCallBack?.onViewClickListener(this)
                MultiMoreThreadDownload.Builder(mContext)
                    .parentFilePath(
                        "${Environment.getExternalStorageDirectory()}/MFiles/${
                            if (data[position].endsWith(
                                    ".mp4"
                                )
                            ) "video" else "picture"
                        }"
                    )
                    .filePath(
                        "${System.currentTimeMillis()}${
                            data[position].substring(
                                data[position].lastIndexOf(
                                    "."
                                )
                            )
                        }"
                    )
                    .fileUrl(data[position])
                    .build()
                    .start()
            }
        }

        if (showCollection) {
            iv_collection.visibility = View.VISIBLE
            iv_collection.clicks().subscribe {
                imageViewPagerDialogCallBack?.onViewClickListener(this)
                iv_collection.setImageResource(R.drawable.iv_collection_click)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initViewPager() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(mPosition: Int) {
                super.onPageSelected(mPosition)
                tv_count.text = "${mPosition + 1}/${data.size}"
                position = mPosition
            }
        })
        viewPager.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                val inflate = LayoutInflater.from(mContext)
                    .inflate(R.layout.dialog_image_viewpager_item, parent, false)
                return object : RecyclerView.ViewHolder(inflate) {

                }

            }

            override fun getItemCount(): Int {
                return data.size
            }

            override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
                super.onViewDetachedFromWindow(holder)
                if (data[holder.absoluteAdapterPosition].endsWith(".mp4")) {
                    GSYVideoManager.releaseAllVideos()
                }
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


                val photoView = holder.itemView.findViewById<PhotoView>(R.id.photoView)
                val gsyVideoPlayer =
                    holder.itemView.findViewById<StandardGSYVideoPlayer>(R.id.detailPlayer)
                val endsWith = data[position].endsWith(".mp4")

                gsyVideoPlayer.findViewById<View>(com.shuyu.gsyvideoplayer.R.id.surface_container)
                    .setOnClickListener {
                        dismiss()
                    }


                photoView.setOnClickListener {
                    dismiss()
                }

                if (endsWith) {
                    photoView.visibility = View.GONE
                    gsyVideoPlayer.setUpLazy(data[position], true, null, null, "")
                    gsyVideoPlayer.titleTextView.visibility = View.GONE
                    gsyVideoPlayer.backButton.visibility = View.GONE
                    gsyVideoPlayer.fullscreenButton.visibility = View.GONE
                    gsyVideoPlayer.apply {
                        setIsTouchWiget(true)
                        isRotateViewAuto = false
                        isLockLand = false
                        isAutoFullWithSize = true
                        isShowFullAnimation = false
                        isNeedLockFull = true
                        setIsTouchWigetFull(false)
                        setThumbPlay(false)
                    }
                    val imageView = ImageView(mContext)
                    imageView.setOnClickListener {
                        dismiss()
                    }
                    Glide.with(imageView)
                        .setDefaultRequestOptions(RequestOptions().frame(1000))
                        .load(data[position])
                        .into(imageView)
                    gsyVideoPlayer.thumbImageView = imageView
                    gsyVideoPlayer.thumbImageViewLayout.visibility = VISIBLE
                } else {
                    gsyVideoPlayer.visibility = View.GONE
                    Glide.with(photoView)
                        .load(data[position])
                        .fitCenter()
                        .error(R.drawable.iv_image_error)
                        .placeholder(R.drawable.iv_image_placeholder)
                        .into(photoView)
                }
            }
        }
        viewPager.offscreenPageLimit = 20
        viewPager.setCurrentItem(position, false)
    }

    override fun onDismiss() {
        imageViewPagerDialogCallBack?.getPosition(viewPager.currentItem)
        GSYVideoManager.releaseAllVideos()
        super.onDismiss()
    }


}