package com.yang.lib_common.remote.di.component

import android.app.Application
import com.google.gson.Gson
import com.yang.lib_common.api.BaseApiService
import com.yang.lib_common.base.di.component.BaseComponent
import com.yang.lib_common.remote.di.module.RemoteModule
import com.yang.lib_common.scope.RemoteScope
import com.yang.lib_common.upload.UploadManage
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@RemoteScope
@Component(modules = [RemoteModule::class], dependencies = [BaseComponent::class])
interface RemoteComponent {
    fun getBaseApiService(): BaseApiService
    fun provideOkHttpClient(): OkHttpClient
    fun provideRetrofit(): Retrofit
    //fun provideBaseViewModelFactory(): BaseViewModelFactory
    fun provideApplication(): Application

    fun provideGson(): Gson

    fun inject(uploadManage: UploadManage)

}