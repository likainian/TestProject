package com.example.testproject.network

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

import java.util.HashMap

/**
 * Created by mike.li.
 * 接口
 */

interface RetrofitApi {

    //不带参Get
    @GET
    fun requestGet(@Url url: String): Observable<String>

    //带参Get
    @GET
    fun requestGet(@Url url: String, @QueryMap map: HashMap<String, Any>): Observable<String>

    //不带参Post
    @POST
    fun requestPost(@Url url: String): Observable<String>

    //带参Post
    @POST
    fun requestPost(@Url url: String, @Body requestBody: RequestBody): Observable<String>

    //表单Post
    @FormUrlEncoded
    @POST
    fun requestFormPost(@Url url: String, @FieldMap map: HashMap<String, Any>): Observable<String>

    //上传
    @Multipart
    @POST
    fun upload(@Url url: String, @Part part: MultipartBody.Part): Observable<String>

    //下载
    @GET
    fun download(@Url url: String): Observable<ResponseBody>

    //下载
    @GET
    fun download(@Url url: String, @QueryMap map: Map<String, Any>): Observable<ResponseBody>

}
