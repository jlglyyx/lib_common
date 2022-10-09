package com.yang.lib_common.api

import com.yang.lib_common.remote.di.response.MResult
import retrofit2.http.GET

interface BaseApiService {

    @GET("user/addCollect")
    suspend fun addCollect(): MResult<String>
}