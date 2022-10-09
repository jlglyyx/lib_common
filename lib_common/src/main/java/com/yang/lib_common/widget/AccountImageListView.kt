package com.yang.lib_common.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.ui.graphics.Color.Companion.White
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.yang.lib_common.R
import kotlin.math.max
import kotlin.math.min

/**
 * @ClassName: AccountImageListView
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/27 16:38
 */
class AccountImageListView : LinearLayout {

    private val maxSize = 5

    var pathList = mutableListOf<String>()
    set(value) {
        field = value
        createView(value)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        orientation = HORIZONTAL

    }

    private fun createView(list:MutableList<String>){

        removeAllViews()
        for (i in 0 until list.size) {
            val shapeAbleImageView = ShapeableImageView(context)
            shapeAbleImageView.shapeAppearanceModel =
                ShapeAppearanceModel.Builder().setAllCorners(CornerFamily.ROUNDED, 40f).build()
            val layoutParams = ViewGroup.LayoutParams(80, 80)
            shapeAbleImageView.layoutParams = layoutParams
            if (i >= maxSize){
                shapeAbleImageView.setBackgroundColor(Color.parseColor("#2a808080"))
                Glide.with(context)
                    .load(R.drawable.iv_ellipsis)
                    .into(shapeAbleImageView)
                addView(shapeAbleImageView)
                return
            }else{
                Glide.with(context)
                    .load(list[i])
                    .into(shapeAbleImageView)
                addView(shapeAbleImageView)
            }
        }

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        var countWidth = 0
        for (i in 0 until childCount) {
            val childAt = getChildAt(i)
            childAt.layout(countWidth, 0, countWidth + childAt.width, childAt.height)
            countWidth += childAt.width - childAt.width/3
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}