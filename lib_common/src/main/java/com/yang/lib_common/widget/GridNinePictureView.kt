package com.yang.lib_common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yang.lib_common.R
import com.yang.lib_common.util.dip2px
import kotlin.math.abs
import kotlin.math.ceil

class GridNinePictureView : ViewGroup {

    private var mContext: Context = context
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var dataSize = 0
    private var spanCount = 3
    private val initSpanCount = 3
    private var mImageViewWidth: Int = 0
    private var rectF: RectF = RectF()
    private var maxChild = 9
    private var mPaint = Paint()
    private var mTextPaint = Paint()
    var imageCallback: ImageCallback? = null
    private lateinit var childView: ViewGroup

    interface ImageCallback {
        fun imageClickListener(position: Int)
    }

    var data: MutableList<String> = mutableListOf()
        set(value) {
            this.removeAllViews()
            field = value
            dataSize = field.size
            if (!field.isNullOrEmpty()) {
                for (i in 0 until if (dataSize > maxChild) {
                    maxChild
                } else {
                    dataSize
                }) {
                    childView = LayoutInflater.from(mContext)
                        .inflate(R.layout.view_item_grid_nine_picture, this, false) as ViewGroup
                    val imageView = childView.findViewById<ImageView>(R.id.iv_nine_image)
                    imageView.setOnClickListener {
                        imageCallback?.imageClickListener(i)
                    }
                    if (data[i].endsWith(".mp4")) {
                        val ivPlay = childView.findViewById<ImageView>(R.id.iv_play)
                        ivPlay.visibility = View.VISIBLE
                        Glide.with(mContext)
                            .setDefaultRequestOptions(RequestOptions().frame(1000))
                            .load(
                                data[i]
                            )
                            .centerCrop()
                            .error(R.drawable.iv_image_error)
                            .placeholder(R.drawable.iv_image_placeholder)
                            .into(imageView)
                    } else {
                        Glide.with(mContext)
                            .load(
                                data[i]
                            )
                            .centerCrop()
                            .error(R.drawable.iv_image_error)
                            .placeholder(R.drawable.iv_image_placeholder)
                            .into(imageView)
                    }
                    addView(childView)
                }

                spanCount = if (childCount < initSpanCount){
                    dataSize
                }else{
                    initSpanCount
                }
            }
        }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
        val obtainStyledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.GridNinePictureView)
        maxChild = obtainStyledAttributes.getInt(R.styleable.GridNinePictureView_maxChild, maxChild)
        spanCount =
            obtainStyledAttributes.getInt(R.styleable.GridNinePictureView_spanCount, spanCount)
        obtainStyledAttributes.recycle()
    }


    private fun init() {
        mPaint.color = Color.parseColor("#afffffff")
        mPaint.style = Paint.Style.FILL
        mTextPaint.color = Color.parseColor("#8a8a8a")
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textSize = 30f.dip2px(mContext).toFloat()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        mImageViewWidth = widthSize / spanCount
        for (i in 0 until childCount) {
            val childAt = getChildAt(i)
            val layoutParams = childAt.layoutParams
            layoutParams.width = mImageViewWidth
            layoutParams.height = mImageViewWidth
            childAt.layoutParams = layoutParams
        }
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            widthSize,
            (widthSize / spanCount) * ceil(
                if (dataSize > maxChild) {
                    maxChild.toFloat()
                } else {
                    dataSize.toFloat()
                } / spanCount.toFloat()
            ).toInt()*3/4
        )
    }



    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layoutImage()
    }

    private fun layoutImage() {
        var countWidth = 0
        var countHeight = 0
        for (i in 0 until childCount) {
            val childAt = getChildAt(i)
            if (i % spanCount == 0) {
                if (i > 0) {
                    countHeight += mImageViewWidth
                }
                countWidth = 0
            } else {
                countWidth += mImageViewWidth
            }

            childAt.layout(
                countWidth,
                countHeight*3/4,
                countWidth + mImageViewWidth,
                (countHeight + mImageViewWidth)*3/4
            )
            if (i == maxChild - 1) {
                rectF.set(
                    countWidth.toFloat(),
                    countHeight*3/4.toFloat(),
                    (countWidth + mImageViewWidth).toFloat(),
                    (countHeight + mImageViewWidth)*3/4.toFloat()
                )
            }

        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (dataSize - maxChild > 0) {
            val exceedCount = dataSize - maxChild
            val bgLayer = canvas.saveLayer(rectF, null)
            canvas.drawRect(rectF, mPaint)
            canvas.restoreToCount(bgLayer)
            val textLayer = canvas.saveLayer(rectF, null)
            val drawTextContent = "+$exceedCount"
            canvas.drawText(
                drawTextContent,
                (rectF.left + (rectF.right - rectF.left) / 2) - mTextPaint.measureText(
                    drawTextContent
                ) / 2,
                (rectF.top + (rectF.bottom - rectF.top) / 2) + (abs(
                    mTextPaint.ascent() + abs(
                        mTextPaint.descent()
                    )
                )) / 2,
                mTextPaint
            )
            canvas.restoreToCount(textLayer)
        }
    }


}