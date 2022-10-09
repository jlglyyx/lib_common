package com.yang.lib_common.remote.di.response

data class MResult<T : Any>(
    val data: T, val code: String, val message: String,val success : Boolean,val total:Int,val count:Int
)

