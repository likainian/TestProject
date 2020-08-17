package com.example.testproject.network.interceptor;

import android.support.annotation.NonNull;
import com.example.testproject.util.LogUtil;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;

/**
 * Created by mike.li.
 * debug模式的log
 */

public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        //url
        HttpUrl url = request.url();
        //request.headers
        Headers headers = request.headers();
        LogUtil.i(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        LogUtil.i(url.toString()+" 请求方式:"+request.method());
        LogUtil.i(url.toString()+" 请求header:"+headers.toString());
        //requestBody
        RequestBody requestBody= request.body();
        if(requestBody!=null){
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            String paramsStr = buffer.readUtf8();
            LogUtil.i(url.toString()+" 请求body:"+paramsStr);
        }else {
            LogUtil.i(url.toString()+" 请求body: {}");
        }
        //response
        Response response = chain.proceed(request);
        if(response!=null){
            //response.headers
            Headers responseHeaders = response.headers();
            HttpUrl url2 = response.request().url();
            LogUtil.i(url2.toString()+" 返回header:"+responseHeaders.toString());
            //responseBody
            String responseBody = response.peekBody(1024 * 1024).string();
            LogUtil.i(url2.toString()+" 返回body:"+responseBody);
        }else {
            LogUtil.i(url.toString()+" 返回body is empty");
        }
        LogUtil.i("=======================================================");
        return response;
    }
}
