package com.yang.lib_common.base.repository

import com.yang.lib_common.remote.di.response.MResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRepository {

    suspend fun <T : Any> withContextIO(mResult: suspend () -> MResult<T>): MResult<T> {
        return withContext(Dispatchers.IO) {
            mResult().isSuccess()
        }
    }

    private fun <T : MResult<*>> T.isSuccess(): T {
        if (!this.success) {
            throw Exception(this.message)
        }
        return this
    }




}