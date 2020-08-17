package com.example.testproject.network;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike.li.
 * 接口
 */

interface RetrofitApi {

    //不带参Get
    @GET
    Observable<String> requestGet(@Url String url);

    //带参Get
    @GET
    Observable<String> requestGet(@Url String url, @QueryMap HashMap<String, Object> map);

    //不带参Post
    @POST
    Observable<String> requestPost(@Url String url);

    //带参Post
    @POST
    Observable<String> requestPost(@Url String url, @Body RequestBody requestBody);

    //表单Post
    @FormUrlEncoded
    @POST
    Observable<String> requestFormPost(@Url String url, @FieldMap HashMap<String, Object> map);

    //上传
    @Multipart
    @POST
    Observable<String> upload(@Url String url, @Part MultipartBody.Part part);

    //下载
    @GET
    Observable<ResponseBody> download(@Url String url);

    //下载
    @GET
    Observable<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> map);

}
