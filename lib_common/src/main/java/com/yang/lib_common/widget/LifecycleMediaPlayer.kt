package com.yang.lib_common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.yang.lib_common.observer.ILifecycleObserver
import com.yang.lib_common.util.getScreenPx

/**
 * @Author Administrator
 * @ClassName LifecycleMediaPlayer
 * @Description
 * @Date 2021/11/29 15:00
 */
class LifecycleMediaPlayer : ViewGroup, SurfaceHolder.Callback, ILifecycleObserver {

    companion object {
        private const val TAG = "LifecycleMediaPlayer"
    }

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var mHolder: SurfaceHolder

    private var isSurfaceCreated = true

    private var position = 0

    private var isReset = false

    private var mWidth = 0f

    private var mHeight = 0f

    private var mVideoWidth = 0

    private var mVideoHeight = 0

    private var mContext: Context

    private var screenPx: IntArray

    private lateinit var surfaceView: SurfaceView

    private lateinit var ivCover: ImageView

    private var currentPath = ""



    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context!!
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioAttributes(
            AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA).build()
        )
        screenPx = getScreenPx(mContext)
        setWillNotDraw(false)
        initSurfaceView()
        setBackgroundColor(Color.BLACK)
    }


    private fun initSurfaceView() {
        surfaceView = SurfaceView(mContext)
        mHolder = surfaceView.holder
        mHolder.addCallback(this@LifecycleMediaPlayer)
        ivCover = ImageView(mContext)
        ivCover.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        ivCover.setBackgroundColor(Color.WHITE)
        this.addView(surfaceView)
        this.addView(ivCover)
    }

    fun initMediaPlayer(path: String): MediaPlayer? {
        try {
            if (!isReset){
                if (currentPath == path){
                    return null
                }else{
                    position = 0
                }
            }

            currentPath = path
            return mediaPlayer?.apply {
                setDataSource(path)
                isLooping = true
                setOnPreparedListener {
                    start()
                    if (isReset) {
                        mediaPlayer?.seekTo(position)
                        Log.i(TAG, "setOnPreparedListener: $position")
                        isReset = false
                    }
                    mVideoWidth = it.videoWidth
                    mVideoHeight = it.videoHeight
                    scaleVideo(mVideoWidth,mVideoHeight)
                }
                setOnInfoListener { mp, what, extra ->
                    Log.i(TAG, "setOnInfoListener: $mp   $what   $extra")
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        ivCover.visibility = View.GONE
                    }
                    return@setOnInfoListener false
                }
                setOnErrorListener { mp, what, extra ->
                    Log.i(TAG, "setOnErrorListener: $mp   $what   $extra")
                    if (what == -38) {
                        mp.reset()
                        isReset = true
                        initMediaPlayer(path)
                    }
                    return@setOnErrorListener true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, "initMediaPlayer: ${e.toString()}")
            ivCover.visibility = View.VISIBLE
            return null
        }
    }

    fun restartVideo(path: String){
        mediaPlayer?.let {
            it.reset()
            isReset = false
            initMediaPlayer(path)
        }

    }

    private fun scaleVideo(mVideoWidth:Int,mVideoHeight:Int){
        val childAt = getChildAt(0)
        if (mVideoHeight <= measuredHeight / 5 * 4) {
            val vScaleW = screenPx[0]
            val vScaleH = mVideoHeight * screenPx[0] / mVideoWidth
            childAt.layoutParams = LayoutParams(vScaleW, vScaleH)
            Log.i(TAG, "scaleVideo:$mVideoWidth   $vScaleW ==  $mVideoHeight  $vScaleH  == ${vScaleH / vScaleW.toFloat()}")
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childAt = getChildAt(0)
        if (childAt.measuredHeight >= screenPx[1]) {
            childAt.layout(0, 0, screenPx[0], childAt.measuredHeight)
        } else {
            val i = (measuredHeight - childAt.measuredHeight) / 2
//            if (measuredHeight > screenPx[1]){
//                i += (measuredHeight - screenPx[1]) / 2
//            }
            childAt.layout(0, i, childAt.measuredWidth, i+childAt.measuredHeight)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
    }


    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        //canvas.drawColor(Color.BLACK)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun stop() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
        }
    }


    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (!isSurfaceCreated) {
            mediaPlayer?.start()
        }
        mediaPlayer?.setDisplay(holder)
        isSurfaceCreated = true
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        isSurfaceCreated = false
        mediaPlayer?.let {
            position = it.currentPosition
            it.pause()
        }
    }


    override fun onResume() {
    }

    override fun onPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    override fun onDestroy() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }

}