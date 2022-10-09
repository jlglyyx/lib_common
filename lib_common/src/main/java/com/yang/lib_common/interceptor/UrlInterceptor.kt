package com.yang.lib_common.interceptor

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response

class UrlInterceptor : Interceptor {

    companion object {
        private const val TAG = "UrlInterceptor"
        var url: String? = null

        /**
         * 每次拦截器修改baseUrl后 下次是否还是使用修改的url 默认不使用
         */
        var disposable: Boolean = false
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val oldUrl = request.url()
        val encodedPath = oldUrl.encodedPath()
        val encodedQuery = oldUrl.encodedQuery()
        if (url != null || !TextUtils.isEmpty(url)) {
            if (!url!!.endsWith("/")) {
                throw Exception("url请以\"/\"结尾")
            }
            val stringBuilder = StringBuilder(url!!)
            stringBuilder.append(encodedPath.substring(1, encodedPath.length))
                .append(if (TextUtils.isEmpty(encodedQuery)) "" else "?${encodedQuery}")
            request = request.newBuilder().url(stringBuilder.toString()).build()
            if (disposable) {
                url = null //重置url
            }
        }
        return chain.proceed(request)
    }

}