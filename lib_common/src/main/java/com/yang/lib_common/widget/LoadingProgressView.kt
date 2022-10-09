package com.yang.lib_common.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.yang.lib_common.R
import com.yang.lib_common.util.filterEmptyFile
import com.yang.lib_common.util.getFilePath
import java.util.*

/**
 * @Author Administrator
 * @ClassName LoadingProgressView
 * @Description
 * @Date 2021/11/18 11:11
 */
class LoadingProgressView : View {

    private var mContext: Context
    private lateinit var mPaint: Paint
    private var mWidth = 0f
    private var mHeight = 0f
    private var startX = 0f
    private var endX = 0f
    private var currentProgress = 0
    private var imageBitmap: Bitmap? = null
    var loadStart = true
        set(value) {
            field = value
            if (field) {
                this.visibility = VISIBLE
            } else {
                this.visibility = GONE
                startX = 0f
                endX = 0f
                if (mWidth != 0f && mHeight != 0f) {
                    getBitmap()
                }
            }
        }
    var mProgress = 0
        set(value) {
            Log.i("TAG", "====: $field  $currentProgress")
            field = value
            if (currentProgress == field) {
                return
            }
            currentProgress = field

            invalidate()
        }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context!!
        init()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mPaint.shader = LinearGradient(
            0f, 0f, mWidth, mHeight, intArrayOf(
                ContextCompat.getColor(mContext, R.color.mediumspringgreen),
                ContextCompat.getColor(mContext, R.color.lawngreen),
                ContextCompat.getColor(mContext, R.color.thistle),
                ContextCompat.getColor(mContext, R.color.coral),
                ContextCompat.getColor(mContext, R.color.maroon)
            ),
            floatArrayOf(0f, 0.1f, 0.2f, 0.3f, 1f), Shader.TileMode.REPEAT
        )
        getBitmap()
    }

    private fun init() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
//        Log.i("TAG", "onDraw: ")
        imageBitmap?.let {
            val imageSaveLayer = canvas.saveLayer(0f, 0f, mWidth, mHeight, null)
            canvas.drawBitmap(it, 0f, mHeight / 2 - it.height / 2, null)
            canvas.restoreToCount(imageSaveLayer)
        }
        startX = endX
        endX = mWidth * currentProgress / 100
        val saveLayer = canvas.saveLayer(0f, 0f, mWidth, 30f, null)
        canvas.drawLine(0f, 0f, endX, 0f, mPaint)
        canvas.restoreToCount(saveLayer)

    }


    private fun getBitmap() {
        val filePath = getFilePath().filterEmptyFile()
        var randomNum = 1
        if (filePath.size > 0) {
            if (filePath.size > 1){
                randomNum = filePath.size - 1
            }

            val s = filePath[Random().nextInt(randomNum)]
            Log.i("TAG", "createBitmap: $s")
            imageBitmap = BitmapFactory.decodeFile(s)
            imageBitmap = imageBitmap?.let {
                Bitmap.createScaledBitmap(
                    it,
                    mWidth.toInt(),
                    mWidth.toInt() * it.height / it.width, true
                )
            }
        }
    }


}