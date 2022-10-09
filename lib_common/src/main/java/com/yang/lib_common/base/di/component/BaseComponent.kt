package com.yang.lib_common.base.di.component

import android.app.Application
import com.yang.lib_common.base.di.module.BaseModule
import com.yang.lib_common.scope.ApplicationScope
import com.google.gson.Gson
import dagger.Component

@ApplicationScope
@Component(modules = [BaseModule::class])
interface BaseComponent {
    fun provideApplication(): Application

    fun provideGson(): Gson
}