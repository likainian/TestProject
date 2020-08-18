package com.example.testproject.network


import com.example.testproject.network.interceptor.LogInterceptor
import com.example.testproject.network.interceptor.ProgressInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.fastjson.FastJsonConverterFactory

import java.util.concurrent.TimeUnit

/**
 * Created by mike.li.
 * 请求设置
 */

internal object RetrofitClient {
    private var retrofit: Retrofit? = null
    val instance: Retrofit?
        @Synchronized get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .client(
                        OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .addInterceptor(ProgressInterceptor())
                            .addInterceptor(LogInterceptor())
                            .build()
                    )
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}
