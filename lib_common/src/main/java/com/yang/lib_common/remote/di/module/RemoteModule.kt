package com.yang.lib_common.remote.di.module

import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.yang.lib_common.api.BaseApiService
import com.yang.lib_common.constant.AppConstant
import com.yang.lib_common.interceptor.LogInterceptor
import com.yang.lib_common.interceptor.UrlInterceptor
import com.yang.lib_common.scope.RemoteScope
import com.yang.lib_common.util.getDefaultMMKV
import dagger.Module
import dagger.Provides
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class RemoteModule {


    @RemoteScope
    @Provides
    fun provideOkHttpClient(logInterceptor: Interceptor,@Named("HeadInterceptor") headInterceptor:Interceptor): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(headInterceptor)
            .addInterceptor(UrlInterceptor())
            .addInterceptor(logInterceptor)
            .connectTimeout(AppConstant.ClientInfo.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(AppConstant.ClientInfo.READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(AppConstant.ClientInfo.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectionPool(ConnectionPool())
            .build()

    }

    @RemoteScope
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConstant.ClientInfo.BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create()
                )
            )
            .client(okHttpClient)
            .build()
    }


    @RemoteScope
    @Provides
    fun provideLogInterceptor(): Interceptor {
        return LogInterceptor()
    }

    @Named("HeadInterceptor")
    @RemoteScope
    @Provides
    fun provideHeadInterceptor(): Interceptor {
        return Interceptor {
            var request = it.request()
            val token = getDefaultMMKV().decodeString(AppConstant.Constant.TOKEN)
            if (!TextUtils.isEmpty(token)){
                request = request.newBuilder().addHeader(AppConstant.Constant.TOKEN, token).build()
            }
            it.proceed(request)
        }
    }


    @RemoteScope
    @Provides
    fun provideBaseApiService(retrofit: Retrofit): BaseApiService {
        return retrofit.create(BaseApiService::class.java)
    }





}