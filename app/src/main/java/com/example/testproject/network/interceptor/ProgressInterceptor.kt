package com.example.testproject.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

import java.io.IOException

/**
 * Created by mike.li.
 * 进度
 */

class ProgressInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        return response.newBuilder().body(response.body()?.let { ProgressResponseBody(it) }).build()
    }
}
