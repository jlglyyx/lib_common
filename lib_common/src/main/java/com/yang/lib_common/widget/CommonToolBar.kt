package com.yang.lib_common.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.imageview.ShapeableImageView
import com.yang.lib_common.R
import com.yang.lib_common.util.clicks
import com.yang.lib_common.util.getStatusBarHeight

class CommonToolBar : ConstraintLayout {

    var imageBackCallBack: ImageBackCallBack? = null

    var imageAddCallBack: ImageAddCallBack? = null

    var tVRightCallBack: TVRightCallBack? = null

    var imageSearchCallBack: ImageSearchCallBack? = null

    var centerContent: String? = null
        set(value) {
            field = value
            tvCenterContent.text = field
        }

    lateinit var tvCenterContent: TextView

    lateinit var ivBack: ShapeableImageView

    lateinit var ivSearch: ImageView

    private lateinit var tvRightContent: TextView

    var rightContentVisible: Boolean = false
        set(value) {
            field = value
            if (field) {
                tvRightContent.visibility = View.VISIBLE
            } else {
                tvRightContent.visibility = View.GONE
            }
        }

    interface ImageSearchCallBack {
        fun imageSearchClickListener()
    }

    interface ImageBackCallBack {
        fun imageBackClickListener()
    }

    interface ImageAddCallBack {
        fun imageAddClickListener()
    }

    interface TVRightCallBack {
        fun tvRightClickListener()
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs!!)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val inflate = LayoutInflater.from(context).inflate(R.layout.view_common_toolbar, this)
        val clToolbar = inflate.findViewById<ConstraintLayout>(R.id.cl_toolbar)
        clToolbar.setPadding(0, getStatusBarHeight(context), 0, 0)
        tvCenterContent = inflate.findViewById(R.id.tv_centerContent)
        val tvLeftContent = inflate.findViewById<TextView>(R.id.tv_leftContent)
        tvRightContent = inflate.findViewById(R.id.tv_rightContent)
        ivBack = inflate.findViewById(R.id.iv_back)
        val ivAdd = inflate.findViewById<ImageView>(R.id.iv_add)
        ivSearch = inflate.findViewById(R.id.iv_search)
        val obtainStyledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.CommonToolBar)
        centerContent =
            obtainStyledAttributes.getString(R.styleable.CommonToolBar_centerContent)
        val leftContent = obtainStyledAttributes.getString(R.styleable.CommonToolBar_leftContent)
        val leftImgVisible =
            obtainStyledAttributes.getBoolean(R.styleable.CommonToolBar_leftImgVisible, true)
        val leftImgSrc =
            obtainStyledAttributes.getResourceId(R.styleable.CommonToolBar_leftImgSrc, 0)

        val rightContent = obtainStyledAttributes.getString(R.styleable.CommonToolBar_rightContent)
        val rightImgVisible =
            obtainStyledAttributes.getBoolean(R.styleable.CommonToolBar_rightImgVisible, false)
        rightContentVisible =
            obtainStyledAttributes.getBoolean(R.styleable.CommonToolBar_rightContentVisible, false)
        val rightImgSrc =
            obtainStyledAttributes.getResourceId(R.styleable.CommonToolBar_rightImgSrc, 0)
        val toolbarBg = obtainStyledAttributes.getResourceId(
            R.styleable.CommonToolBar_toolbarBg, 0
        )
        if (toolbarBg != 0) {
            clToolbar.setBackgroundResource(toolbarBg)
        }
        tvCenterContent.text = centerContent
        tvLeftContent.text = leftContent
        tvRightContent.text = rightContent
        if (leftImgVisible) {
            ivBack.visibility = View.VISIBLE
        } else {
            ivBack.visibility = View.GONE
        }

        if (leftImgSrc != 0) {
            ivBack.setImageResource(leftImgSrc)
        }
        if (rightImgVisible) {
            ivAdd.visibility = View.VISIBLE
        } else {
            ivAdd.visibility = View.GONE
        }
        if (rightImgSrc != 0) {
            ivAdd.setImageResource(rightImgSrc)
        }

        if (rightContentVisible) {
            tvRightContent.visibility = View.VISIBLE
        } else {
            tvRightContent.visibility = View.GONE
        }
        ivBack.clicks().subscribe {
            if (null != imageBackCallBack) {
                imageBackCallBack?.imageBackClickListener()
            } else {
                (context as Activity).finish()
            }
        }
        ivSearch.clicks().subscribe {
            imageSearchCallBack?.imageSearchClickListener()
        }
        tvRightContent.clicks().subscribe {
            tVRightCallBack?.tvRightClickListener()
        }
        ivAdd.clicks().subscribe {
            imageAddCallBack?.imageAddClickListener()
        }

        obtainStyledAttributes.recycle()
    }

}