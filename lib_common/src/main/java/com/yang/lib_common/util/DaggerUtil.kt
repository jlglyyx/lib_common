@file:JvmName("DaggerUtil")
package com.yang.lib_common.util

import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.yang.lib_common.base.viewmodel.BaseViewModel

/**
 * @Author Administrator
 * @ClassName DaggerUtil
 * @Description
 * @Date 2021/8/17 16:21
 */

fun <T: BaseViewModel> getViewModel(viewModelStoreOwner: ViewModelStoreOwner, @NonNull clazz: Class<T>):T{

    return ViewModelProvider(viewModelStoreOwner).get(clazz)
}

fun <T: BaseViewModel> getViewModel(viewModelStoreOwner: ViewModelStoreOwner, @NonNull factory: ViewModelProvider.Factory, @NonNull clazz: Class<T>):T{

    return ViewModelProvider(viewModelStoreOwner, factory).get(clazz)
}