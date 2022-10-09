package com.yang.lib_common.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.yang.lib_common.R
import com.yang.lib_common.databinding.ItemTaskAutoPalyBinding

/**
 * @ClassName: AutoScrollListView
 * @Description:
 * @Author: yxy
 * @Date: 2022/7/28 10:53
 */
class AutoScrollListView : LinearLayout, LifecycleObserver {

    private var showPosition = 0
    private var viewObjectAnimator: ObjectAnimator? = null
    private var currentPosition = 0
    var list = mutableListOf<String>("133****4568任务进行中","139****4358接受任务","136****4568任务进行中","133****4521任务已完成","127****4568任务已完成",)
    var firstBinding: ItemTaskAutoPalyBinding =
        ItemTaskAutoPalyBinding.inflate(LayoutInflater.from(context), this, false)
    var secondBinding: ItemTaskAutoPalyBinding =
        ItemTaskAutoPalyBinding.inflate(LayoutInflater.from(context), this, false)


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        setView(firstBinding)
        setView(secondBinding)
        addView(firstBinding.root)
        addView(secondBinding.root)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.i("TAG", "onSizeChanged: ")
        val firstView = getChildAt(0)
        val secondView = getChildAt(1)
        viewObjectAnimator =
            ObjectAnimator.ofFloat(this, "translationY", 0f, -firstView.measuredHeight.toFloat())
        viewObjectAnimator?.let {
            it.duration = 4000
            it.repeatCount = -1
            it.interpolator = LinearInterpolator()
            it.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationRepeat(animation: Animator?) {
                    super.onAnimationRepeat(animation)
                    showPosition = if (showPosition == 0) {
                        removeView(firstView)
                        setView(firstBinding)
                        addView(firstView)
                        1
                    } else {
                        removeView(secondView)
                        setView(secondBinding)
                        addView(secondView)
                        0
                    }
                }
            })
            it.start()
        }
    }

    fun setView(binding:ItemTaskAutoPalyBinding){
        binding.tvName.text = list[currentPosition]
        Glide.with(context).load("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2F99689cbe5898812b3b1340545c08847a430d047f.jpg&refer=http%3A%2F%2Fi0.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1661329266&t=3bd482aedc9184e4a91f252914b2291f").into(binding.sivImg)
        if (currentPosition < list.size-1){
            currentPosition++
        }else{
            currentPosition = 0
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, getChildAt(0).measuredHeight * 2)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        viewObjectAnimator?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        viewObjectAnimator?.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        viewObjectAnimator?.cancel()
    }

}