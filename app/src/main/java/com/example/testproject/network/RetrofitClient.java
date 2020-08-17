package com.example.testproject.network;


import com.example.testproject.network.interceptor.LogInterceptor;
import com.example.testproject.network.interceptor.ProgressInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by mike.li.
 * 请求设置
 */

class RetrofitClient {
    private static Retrofit retrofit;
    public synchronized static Retrofit getInstance(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .client( new OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .addInterceptor(new ProgressInterceptor())
                            .addInterceptor(new LogInterceptor())
                            .build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
