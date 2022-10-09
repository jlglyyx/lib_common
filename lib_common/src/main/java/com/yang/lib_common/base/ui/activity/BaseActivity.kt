package com.yang.lib_common.base.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yang.lib_common.R
import com.yang.lib_common.base.viewmodel.BaseViewModel
import com.yang.lib_common.bus.event.UIChangeLiveData
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.addActivity
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.removeActivity
import kotlin.system.measureTimeMillis

abstract class BaseActivity<VB:ViewBinding> : AppCompatActivity() {

    private var uC: UIChangeLiveData? = null

    private var loadingPopupView: LoadingPopupView? = null

    private var emptyView: View? = null

    private lateinit var _mViewBinding: VB

    val mViewBinding get() = _mViewBinding

    val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        val openTime = measureTimeMillis {
            super.onCreate(savedInstanceState)
            _mViewBinding = initViewBinding()
            setContentView(_mViewBinding.root)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            initViewModel()
            uC = initUIChangeLiveData()
            initData()
            initView()
            registerListener()
            addActivity(this)
            //LogisticsCenter.instance.injectViewModel(this)
        }
        Log.e(TAG, "OpenView===: $TAG  OpeTime===: $openTime")
    }


    abstract fun initViewBinding(): VB

    abstract fun initData()

    abstract fun initView()

    abstract fun initViewModel()

    open fun initUIChangeLiveData(): UIChangeLiveData? {//在ViewModel层操作ui
        return null
    }

    inline fun <reified T : ViewBinding> bind(crossinline bind: (LayoutInflater) -> T) = bind(layoutInflater)

    fun <T : BaseViewModel> getViewModel(@NonNull clazz: Class<T>): T {

        return ViewModelProvider(this).get(clazz)
    }

    fun <T : BaseViewModel> getViewModel(
        @NonNull factory: ViewModelProvider.Factory,
        @NonNull clazz: Class<T>
    ): T {

        return ViewModelProvider(this, factory).get(clazz)
    }

    fun finishRefreshLoadMore(smartRefreshLayout: SmartRefreshLayout) {
        uC?.let { uC ->
            uC.refreshEvent.observe(this, Observer {
                smartRefreshLayout.finishRefresh()
            })
            uC.loadMoreEvent.observe(this, Observer {
                smartRefreshLayout.finishLoadMore()
            })
        }
    }

    fun showRecyclerViewEvent(adapter: BaseQuickAdapter<*, *>) {
        uC?.let { uC ->
            uC.showRecyclerViewEvent.observe(this, Observer {
                if (it == AppConstant.LoadingViewEnum.ERROR_VIEW) {
                    emptyView =
                        LayoutInflater.from(this).inflate(R.layout.view_error_data, null, false)
                } else if (it == AppConstant.LoadingViewEnum.EMPTY_VIEW) {
                    emptyView =
                        LayoutInflater.from(this).inflate(R.layout.view_empty_data, null, false)
                }
                adapter.setNewData(null)
                adapter.emptyView = emptyView
            })
        }
    }

    fun registerRefreshAndRecyclerView(
        smartRefreshLayout: SmartRefreshLayout,
        adapter: BaseQuickAdapter<*, *>
    ) {
        uC?.let { uC ->
            uC.refreshEvent.observe(this, Observer {
                smartRefreshLayout.finishRefresh()
            })
            uC.loadMoreEvent.observe(this, Observer {
                smartRefreshLayout.finishLoadMore()
            })
            uC.showRecyclerViewEvent.observe(this, Observer {
                if (it == AppConstant.LoadingViewEnum.ERROR_VIEW) {
                    emptyView =
                        LayoutInflater.from(this).inflate(R.layout.view_error_data, null, false)
                } else if (it == AppConstant.LoadingViewEnum.EMPTY_VIEW) {
                    emptyView =
                        LayoutInflater.from(this).inflate(R.layout.view_empty_data, null, false)
                }
                adapter.setNewData(null)
                adapter.emptyView = emptyView
            })
        }
    }


    private fun registerListener() {
        uC?.let { uC ->
            uC.showLoadingEvent.observe(this, Observer {
                if (loadingPopupView == null) {
                    loadingPopupView =
                        XPopup.Builder(this).dismissOnTouchOutside(false).asLoading(it)
                } else {
                    loadingPopupView?.setTitle(it)
                }
                if (!loadingPopupView?.isShow!!) {
                    loadingPopupView?.show()
                }
            })

            uC.dismissDialogEvent.observe(this, Observer {
                loadingPopupView?.dismiss()
            })
            uC.finishActivityEvent.observe(this, Observer {
                finish()
            })
            uC.startActivityEvent.observe(this, Observer {
                buildARouter(it).navigation()
            })
        }

    }

    private fun unRegisterListener() {
        uC?.let { uC ->
            uC.showLoadingEvent.removeObservers(this)
            uC.dismissDialogEvent.removeObservers(this)
            uC.refreshEvent.removeObservers(this)
            uC.loadMoreEvent.removeObservers(this)
            uC.finishActivityEvent.removeObservers(this)
            uC.requestSuccessEvent.removeObservers(this)
            uC.requestFailEvent.removeObservers(this)
            uC.showRecyclerViewEvent.removeObservers(this)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unRegisterListener()
        uC = null
        emptyView = null
        loadingPopupView?.dismiss()
        loadingPopupView = null
        removeActivity(this)
    }
}