package com.yang.lib_common.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.yang.lib_common.R
import com.yang.lib_common.observer.ILifecycleObserver
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

/**
 * @Author Administrator
 * @ClassName TestView
 * @Description
 * @Date 2021/11/9 9:09
 */
class TestView : View , ILifecycleObserver {

    data class PointBean(var pointF: PointF, var offset: Float,var maxOffset: Float, var speed: Float, var angle: Float,var alpha:Int)

    private lateinit var mPaint: Paint
    private lateinit var mPointPaint: Paint
    private lateinit var mPath: Path
    private lateinit var mDisBitmap: Bitmap
    private lateinit var mShadowDisBitmap :Bitmap
    private lateinit var mSrcBitmap: Bitmap
    private lateinit var porterDuffXfermode: PorterDuffXfermode
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var pointList = mutableListOf<PointBean>()
    private var degrees = 0f
    private var pointSize = 300
    private var pos = FloatArray(2)
    private var tan = FloatArray(2)
    private var random:Random = Random()

    private val pointRadius:Float = 3f
    private var imageRadius:Float = 0f
    private val offset = 5
    private val maxOffset = 500
    private val speed = 3f
    private val maxSpeed = 3
    private val maxOffsetXY = 6
    private var job: Job? = null
    private var isStop = false



    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        mPath = Path()
        createPoint()
        imageRadius = mDisBitmap.width / 2f
    }

    private fun init() {
        mDisBitmap = BitmapFactory.decodeResource(resources, R.drawable.a)
        mSrcBitmap = BitmapFactory.decodeResource(resources, R.drawable.b)
        mShadowDisBitmap = mDisBitmap.extractAlpha()

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.FILL
        /*设置阴影*/
        //mPaint.setShadowLayer(10f,3f,3f,Color.DKGRAY)
        mPaint.maskFilter = BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL)



        mPointPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPointPaint.style = Paint.Style.FILL
        mPointPaint.color = Color.WHITE


        porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }


    private fun createPoint() {
        mPath.addCircle(mWidth / 2f, mHeight / 2f, mDisBitmap.width / 2f + 10f, Path.Direction.CW)
        val pathMeasure = PathMeasure()
        pathMeasure.setPath(mPath, false)
        for (i in 0 until pointSize) {
            pathMeasure.getPosTan(i * pathMeasure.length / pointSize, pos, tan)
            val point = PointF(random.nextInt(maxOffsetXY)+pos[0]-3, random.nextInt(maxOffsetXY)+pos[1]-3)
            val angle = acos((pos[0] - mWidth / 2) / (mDisBitmap.width / 2f + 10f))
            val offset = random.nextInt(offset).toFloat()
            val maxOffset = random.nextInt(maxOffset).toFloat()
            val speed = random.nextInt(maxSpeed)+speed
            val alpha = (255 - offset/maxOffset*255).toInt()
            val pointBean = PointBean(point, offset,maxOffset, speed, angle,alpha)
            pointList.add(pointBean)
        }

        job = GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                while (!isStop){
                    pointList.forEach {
                        if (it.offset > it.maxOffset) {
                            it.offset = 0f
                            it.speed = random.nextInt(maxSpeed) + speed
                            if (it.maxOffset <= 30) {
                                it.maxOffset = Random().nextInt(maxOffset).toFloat()
                            }
                            it.alpha = 255
                        }
                        it.pointF.x =
                            mWidth / 2 + (cos(it.angle) * (mDisBitmap.width / 2f + 10f + it.offset))
                        if (it.pointF.y > mHeight / 2) {
                            it.pointF.y =
                                mHeight / 2 + (sin(it.angle) * (mDisBitmap.width / 2f + 10f + it.offset))
                        } else {
                            it.pointF.y =
                                mHeight / 2 - (sin(it.angle) * (mDisBitmap.width / 2f + 10f + it.offset))
                        }

                        it.alpha = (255 - it.offset / it.maxOffset * 255).toInt()
                        it.offset += it.speed

                    }
                    postInvalidate()
                    delay(30)
                }
            }

        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor((0xcc1c093e).toInt())
        val pointSaveLayer = canvas.saveLayer(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), null)
        pointList.forEach {
            mPointPaint.alpha = it.alpha
            mPointPaint.setARGB(random.nextInt(255),random.nextInt(255),random.nextInt(255),random.nextInt(255))
            canvas.drawCircle(it.pointF.x, it.pointF.y, pointRadius, mPointPaint)
        }
        canvas.restoreToCount(pointSaveLayer)

        canvas.rotate(degrees, mWidth / 2f, mHeight / 2f)

        val saveLayer = canvas.saveLayer(mWidth / 2 - mDisBitmap.width / 2f, mHeight / 2 - mDisBitmap.height / 2f, mWidth / 2 - mDisBitmap.width / 2f + mDisBitmap.width, mHeight / 2 - mDisBitmap.height / 2f + mDisBitmap.height, null)
        degrees += 2f
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, imageRadius, mPaint)
        mPaint.xfermode = porterDuffXfermode
        canvas.drawBitmap(mShadowDisBitmap, mWidth / 2 - mDisBitmap.width / 2f, mHeight / 2 - mDisBitmap.height / 2f, mPaint)
        canvas.drawBitmap(mDisBitmap, mWidth / 2 - mDisBitmap.width / 2f, mHeight / 2 - mDisBitmap.height / 2f, mPaint)
        mPaint.xfermode = null
        canvas.restoreToCount(saveLayer)
    }

    override fun onResume() {
       if (isStop){
           isStop = false
       }
    }

    override fun onPause() {
        if (!isStop){
            isStop = true
        }
    }

    override fun onDestroy() {
        job?.cancel()
    }


}