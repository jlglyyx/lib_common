package com.yang.lib_common.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.yang.lib_common.R
import com.yang.lib_common.util.getScreenPx

/**
 * ClassName: ImageScrollView.
 * Created by Administrator on 2021/4/1_9:56.
 * Describe:
 */
class ImageScrollView : FrameLayout, LifecycleObserver {

    companion object {
        private const val TAG = "ImageScrollView"

        const val HORIZONTAL = 0

        const val VERTICAL = 1

        const val LOOP_VERTICAL = 2

        const val NORMAL_VERTICAL = 3

        const val UP = 4

        const val DOWN = 5
    }

    private var bitmap: Bitmap? = null
    private var scaleBitmap: Bitmap? = null
    private var mPaint = Paint()
    private var mMatrix = Matrix()
    private var mBitmapCount = 0
    private var mPanDistance = 0f

    private var w: Int = 0
    private var h: Int = 0

    var speed = 0.5f

    var imageUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic2.zhimg.com%2Fv2-583a86cd154739160d2e17e185dcc8f2_r.jpg%3Fsource%3D1940ef5c&refer=http%3A%2F%2Fpic2.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1638427892&t=e2a584b32bb0b6f820613078d552716c"
    set(value) {
        field = value
        if (w == 0 || h == 0){
            return
        }
        createBitmap()
    }


    var scrollType = LOOP_VERTICAL

    private var direction = UP

    private var isPause = false


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setWillNotDraw(false)
        val obtainStyledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.ImageScrollView)
        speed = obtainStyledAttributes.getFloat(R.styleable.ImageScrollView_speed, speed)
        scrollType =
            obtainStyledAttributes.getInt(R.styleable.ImageScrollView_scrollType, LOOP_VERTICAL)
        obtainStyledAttributes.recycle()
        Log.i(TAG, "constructor: $w  $h")
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
        Log.i(TAG, "onSizeChanged: $w  $h  $imageUrl")
        createBitmap()
    }

    private fun createBitmap(){
        Glide.with(this).asBitmap()
            .load(imageUrl)
            .into(
                object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Log.i(TAG, "onResourceReady: $resource")
                        bitmap = resource
                        init(bitmap!!)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        bitmap = Bitmap.createBitmap(
                            getScreenPx(context)[0],
                            getScreenPx(context)[1],
                            Bitmap.Config.RGB_565
                        )
                        val canvas = Canvas(bitmap!!)
                        canvas.drawColor(Color.WHITE)
                        init(bitmap!!)
                    }

                })
    }


    private fun init(bitmap: Bitmap) {
        val copy = bitmap.copy(Bitmap.Config.RGB_565, true)
        scaleBitmap = scaleBitmap(copy, w, h)
        mBitmapCount = measuredHeight / scaleBitmap!!.height + 1
        if (!copy.isRecycled) {
            copy.recycle()
            System.gc()
        }

//        mPaint.colorFilter = ColorMatrixColorFilter(
//            floatArrayOf(
//                1f, 0f, 0f, 0f, 0f,
//                0f, 2f, 0f, 0f, 0f,
//                0f, 0f, 2f, 0f, 0f,
//                0f, 0f, 0f, 1f, 0f
//            )
//        )
        requestLayout()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        scaleBitmap?.let { scaleBitmap ->
            val height = scaleBitmap.height
            when (scrollType) {
                LOOP_VERTICAL -> {
                    if (height + mPanDistance != 0f) {
                        mMatrix.reset()
                        mMatrix.postTranslate(0f, mPanDistance)
                        canvas.drawBitmap(scaleBitmap, mMatrix, mPaint)
                    }
                    if (height + mPanDistance < measuredHeight) {
                        for (i in 0 until mBitmapCount) {
                            mMatrix.reset()
                            mMatrix.postTranslate(0f, (i + 1) * scaleBitmap.height + mPanDistance)
                            canvas.drawBitmap(scaleBitmap, mMatrix, mPaint)
                        }
                    }
                }
                NORMAL_VERTICAL -> {
                    mMatrix.reset()
                    mMatrix.postTranslate(0f, mPanDistance)
                    canvas.drawBitmap(scaleBitmap, mMatrix, mPaint)
                }
            }

            invalidateView()
        }
    }


    private fun invalidateView() {
        scaleBitmap?.let { scaleBitmap ->
            val length = scaleBitmap.height
            when (scrollType) {
                LOOP_VERTICAL -> {
                    if (length + mPanDistance <= 0f) {
                        mPanDistance = 0f
                    }
                    mPanDistance -= speed
                }
                NORMAL_VERTICAL -> {
                    if (length + mPanDistance <= h) {
                        direction = DOWN
                    }
                    if (length + mPanDistance >= length.toFloat()) {
                        direction = UP
                    }
                    if (direction == UP) {
                        mPanDistance -= speed
                    }
                    if (direction == DOWN) {
                        mPanDistance += speed
                    }
                }
            }
            if (!isPause) {
                postInvalidate()
            }
        }
    }

    private fun scaleBitmap(bitmap: Bitmap, w: Int, h: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val newHeight: Int
        val newWidth: Int = measuredWidth
        newHeight = newWidth * height / width
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (isPause) {
            isPause = false
            invalidateView()
        }
        Log.i(TAG, "onResume: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if (!isPause) {
            isPause = true
        }
        Log.i(TAG, "onPause: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        isPause = true
        Log.i(TAG, "onDestroy: ")
    }


}