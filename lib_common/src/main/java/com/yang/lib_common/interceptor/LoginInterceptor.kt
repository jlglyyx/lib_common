package com.yang.lib_common.interceptor

import android.content.Context
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.yang.lib_common.R
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.util.buildARouter
import com.yang.lib_common.util.getDefaultMMKV


/**
 * @ClassName LoginInterceptor
 *
 * @Description
 *
 * @Author 1
 *
 * @Date 2021/1/21 11:33
 */
@Interceptor(priority = 1, name = "登录状态拦截器")
class LoginInterceptor : IInterceptor {

    lateinit var mContext: Context

    companion object {
        private const val TAG = "LoginInterceptor"
    }

    /**
     * 需要登录操作页面
     */
    private var toLoginList = arrayListOf(
        AppConstant.RoutePath.MINE_OTHER_PERSON_INFO_ACTIVITY,
        AppConstant.RoutePath.OPEN_VIP_ACTIVITY,
        AppConstant.RoutePath.MY_PUSH_ACTIVITY,
        AppConstant.RoutePath.MY_COLLECTION_ACTIVITY,
        AppConstant.RoutePath.PRIVACY_ACTIVITY,
        AppConstant.RoutePath.SETTING_ACTIVITY,
        AppConstant.RoutePath.ADD_DYNAMIC_ACTIVITY,
        AppConstant.RoutePath.MINE_OBTAIN_TURNOVER_ACTIVITY,
        AppConstant.RoutePath.MINE_SIGN_TURNOVER_ACTIVITY,
        AppConstant.RoutePath.MINE_EXTENSION_TURNOVER_ACTIVITY,
        AppConstant.RoutePath.MINE_OBTAIN_EXCHANGE_ACTIVITY,
        AppConstant.RoutePath.MINE_OBTAIN_TASK_ACTIVITY,
        AppConstant.RoutePath.OPEN_VIP_ACTIVITY
    )

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        postcard.timeout = 2
        when (getDefaultMMKV().decodeInt(AppConstant.Constant.LOGIN_STATUS, -1)) {
            AppConstant.Constant.LOGIN_SUCCESS -> {
                callback.onContinue(postcard)
            }
            AppConstant.Constant.LOGIN_FAIL -> {
                buildARouter(AppConstant.RoutePath.LOGIN_ACTIVITY).navigation()
            }
            AppConstant.Constant.LOGIN_NO_PERMISSION -> {
                val indexOf = toLoginList.indexOf(postcard.path)
                if (indexOf != -1) {
                    postcard.group = AppConstant.RoutePath.MODULE_LOGIN
                    postcard.path = AppConstant.RoutePath.LOGIN_ACTIVITY
                    postcard.destination = Class.forName("com.yang.module_login.ui.activity.LoginActivity")
                    postcard.withOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(mContext, R.anim.bottom_in, R.anim.bottom_out))
                    /*数据传递存在风险 其他页面传递来了数据 未清除其他页面传递的数据 可能在下个页面存在数据接收风险*/
                    postcard.withInt(AppConstant.Constant.DATA,0)
                    callback.onContinue(postcard)
                } else {
                    callback.onContinue(postcard)
                }
            }
            else -> {
                callback.onContinue(postcard)
            }
        }
        Log.i(TAG, "process: 拦截器====${postcard.path}=====")
    }

    override fun init(context: Context) {

        mContext = context
        //仅会调用一次
    }

}