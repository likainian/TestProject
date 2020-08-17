package com.example.testproject.network.interceptor;

import android.support.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by mike.li.
 * 进度
 */

public class ProgressInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response.newBuilder().body(new ProgressResponseBody(response.body())).build();
    }
}
