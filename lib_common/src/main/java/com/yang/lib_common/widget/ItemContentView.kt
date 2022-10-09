package com.yang.lib_common.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yang.lib_common.R

/**
 * @Author Administrator
 * @ClassName ItemContentView
 * @Description
 * @Date 2021/9/28 14:34
 */
class ItemContentView:LinearLayout {

    private lateinit var tvContent:TextView

    private lateinit var tvRightContent:TextView

    var rightContent:String? = null
    set(value) {
         field = value
        tvRightContent.text = field
    }
    var rightContentFontSize:Float = 13f
    set(value) {
         field = value
        tvRightContent.setTextSize(TypedValue.COMPLEX_UNIT_SP,field)
    }
    var content:String? = null
    set(value) {
         field = value
        tvContent.text = field
    }
    var contentFontSize:Float = 15f
    set(value) {
         field = value
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP,field)
    }

    constructor(context: Context) : this(context,null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        if (attrs != null) {
            init(context,attrs)
        }
    }

    private fun init(context:Context,attrs: AttributeSet){
        val view = LayoutInflater.from(context).inflate(R.layout.view_item_content, this, true)
        val llLl = view.findViewById<LinearLayout>(R.id.ll_ll)
        val ivLeft = view.findViewById<ImageView>(R.id.iv_left)
        val ivRight = view.findViewById<ImageView>(R.id.iv_right)
        tvContent = view.findViewById<TextView>(R.id.tv_content)
        tvRightContent = view.findViewById(R.id.tv_rightContent)
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ItemContentView)
        val leftContent = obtainStyledAttributes.getString(R.styleable.ItemContentView_itemLeftContent)
        val leftImgVisible =
            obtainStyledAttributes.getBoolean(R.styleable.ItemContentView_itemLeftImgVisible, true)
        val leftImgSrc =
            obtainStyledAttributes.getResourceId(R.styleable.ItemContentView_itemLeftImgSrc, 0)

        val rightContent = obtainStyledAttributes.getString(R.styleable.ItemContentView_itemRightContent)
        val rightImgVisible =
            obtainStyledAttributes.getBoolean(R.styleable.ItemContentView_itemRightImgVisible, true)
        val rightContentVisible =
            obtainStyledAttributes.getBoolean(R.styleable.ItemContentView_itemRightContentVisible, true)
        val rightImgSrc =
            obtainStyledAttributes.getResourceId(R.styleable.ItemContentView_itemRightImgSrc, 0)
        val itemBg = obtainStyledAttributes.getResourceId(
            R.styleable.ItemContentView_itemBg, 0
        )
        if (itemBg != 0) {
            llLl.setBackgroundResource(itemBg)
        }
        tvContent.text = leftContent
        tvRightContent.text = rightContent
        if (leftImgVisible) {
            ivLeft.visibility = View.VISIBLE
        } else {
            ivLeft.visibility = View.GONE
        }

        if (leftImgSrc != 0){
            ivLeft.setImageResource(leftImgSrc)
        }
        if (rightImgVisible) {
            ivRight.visibility = View.VISIBLE
        } else {
            ivRight.visibility = View.GONE
        }
        if (rightImgSrc != 0){
            ivRight.setImageResource(rightImgSrc)
        }

        if (rightContentVisible) {
            tvRightContent.visibility = View.VISIBLE
        } else {
            tvRightContent.visibility = View.GONE
        }
        obtainStyledAttributes.recycle()
    }

}