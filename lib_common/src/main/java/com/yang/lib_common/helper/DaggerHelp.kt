@file:JvmName("DaggerHelp")

package com.yang.lib_common.helper

import com.yang.lib_common.app.BaseApplication
import com.yang.lib_common.base.di.component.BaseComponent
import com.yang.lib_common.base.di.component.DaggerBaseComponent
import com.yang.lib_common.base.di.module.BaseModule
import com.yang.lib_common.remote.di.component.DaggerRemoteComponent
import com.yang.lib_common.remote.di.component.RemoteComponent
import com.yang.lib_common.remote.di.module.RemoteModule

private const val TAG = "DaggerHelp"

fun getBaseComponent(): BaseComponent {
    return DaggerBaseComponent.builder().baseModule(
        BaseModule(
            BaseApplication.baseApplication
        )
    ).build()
}

fun getRemoteComponent(): RemoteComponent {
    return DaggerRemoteComponent.builder().baseComponent(getBaseComponent())
        .remoteModule(RemoteModule()).build()
}



