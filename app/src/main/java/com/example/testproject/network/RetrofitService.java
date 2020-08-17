package com.example.testproject.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import com.alibaba.fastjson.JSON;
import com.example.testproject.network.interceptor.ProgressResponseBody;
import com.example.testproject.util.FileUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by mike.li.
 */

public class RetrofitService {
    protected static RetrofitApi retrofitApi;
    public static RetrofitService getInstance() {
        if(retrofitApi ==null){
            retrofitApi = RetrofitClient.getInstance().create(RetrofitApi.class);
        }
        return new RetrofitService();
    }

    //不带参Get
    public void requestGet(String path,CallBack<String> callBack){
        handString(retrofitApi.requestGet(path),callBack);
    }

    //带参Get
    public void requestGet(String path, HashMap<String,Object> map, CallBack<String> callBack){
        handString(retrofitApi.requestGet(path,map),callBack);
    }

    //不带参Post
    public void requestPost(String path,CallBack<String> callBack){
        handString(retrofitApi.requestPost(path),callBack);
    }

    //带参Post
    public void requestPost(String path, Map<String,Object> map, CallBack<String> callBack){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map));
        handString(retrofitApi.requestPost(path,requestBody),callBack);
    }
    //带参Post
    public void requestPost(String path, Object object, CallBack<String> callBack){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(object));
        handString(retrofitApi.requestPost(path,requestBody),callBack);
    }

    //表单Post
    public void requestFormPost(String path,@NonNull HashMap<String,Object> map,CallBack<String> callBack){
        handString(retrofitApi.requestFormPost(path,map),callBack);
    }

    //上传文件
    public void uploadFile(String path, File file, CallBack<String> callBack){
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        handString(retrofitApi.upload(path,part),callBack);
    }

    //下载文件
    public void downloadFile(final String url,String fileName, final CallBack<File> callBack){
        handFile(retrofitApi.download(url),fileName,callBack);
    }

    //上传图片
    public void uploadImage(String path, File file, CallBack<String> callBack){
        File compressFile = new File(FileUtil.getSavePicturePath() + file.getName());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), compressFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        handString(retrofitApi.upload(path,part),callBack);
    }

    //下载无缓存图片
    public void downloadImage(final String url, final CallBack<Bitmap> callBack){
        handBitmap(retrofitApi.download(url),callBack);
    }
    //下载无缓存图片
    public void downloadImage(final String url, Map<String,Object> map, final CallBack<Bitmap> callBack){
        handBitmap(retrofitApi.download(url,map),callBack);
    }

    //处理文件
    private void handFile(Observable<ResponseBody> observable, final String fileName, final CallBack<File> callBack) {
        ProgressResponseBody.setCallBack(callBack);
        observable.map(new Function<ResponseBody, File>() {
                    @Override
                    public File apply(ResponseBody responseBody) throws Exception {
                        InputStream is = responseBody.byteStream();
                        File file = new File(FileUtil.getSaveFilePath() + fileName);
                        file.deleteOnExit();
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            fos.flush();
                        }
                        fos.close();
                        bis.close();
                        is.close();
                        return file;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        callBack.onResponse(file);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onError(throwable);
                    }
                });
    }

    //处理图片
    private void handBitmap(Observable<ResponseBody> observable, final CallBack<Bitmap> callBack) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream(), null, options);
                        callBack.onResponse(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onError(throwable);
                    }
                });
    }

    //处理字符串
    private void handString(Observable<String> observable, final CallBack<String> callBack){
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String string) throws Exception {
                        //以前的code
                        callBack.onResponse(string);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callBack.onError(throwable);
                    }
                });
    }

}
