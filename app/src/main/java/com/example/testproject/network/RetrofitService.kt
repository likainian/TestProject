package com.example.testproject.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.alibaba.fastjson.JSON
import com.example.testproject.network.interceptor.ProgressResponseBody
import com.example.testproject.util.FileUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.HashMap

/**
 * Created by mike.li.
 */

open class RetrofitService {

    //不带参Get
    fun requestGet(path: String, callBack: CallBack<String>) {
        handString(retrofitApi!!.requestGet(path), callBack)
    }

    //带参Get
    fun requestGet(path: String, map: HashMap<String, Any>, callBack: CallBack<String>) {
        handString(retrofitApi!!.requestGet(path, map), callBack)
    }

    //不带参Post
    fun requestPost(path: String, callBack: CallBack<String>) {
        handString(retrofitApi!!.requestPost(path), callBack)
    }

    //带参Post
    fun requestPost(path: String, map: Map<String, Any>, callBack: CallBack<String>) {
        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(map))
        handString(retrofitApi!!.requestPost(path, requestBody), callBack)
    }

    //带参Post
    fun requestPost(path: String, `object`: Any, callBack: CallBack<String>) {
        val requestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(`object`))
        handString(retrofitApi!!.requestPost(path, requestBody), callBack)
    }

    //表单Post
    fun requestFormPost(path: String, map: HashMap<String, Any>, callBack: CallBack<String>) {
        handString(retrofitApi!!.requestFormPost(path, map), callBack)
    }

    //上传文件
    fun uploadFile(path: String, file: File, callBack: CallBack<String>) {
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), file)
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        handString(retrofitApi!!.upload(path, part), callBack)
    }

    //下载文件
    fun downloadFile(url: String, fileName: String, callBack: CallBack<File>) {
        handFile(retrofitApi!!.download(url), fileName, callBack)
    }

    //上传图片
    fun uploadImage(path: String, file: File, callBack: CallBack<String>) {
        val compressFile = File(FileUtil.savePicturePath!! + file.name)
        val requestBody = RequestBody.create(MediaType.parse("image/png"), compressFile)
        val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
        handString(retrofitApi!!.upload(path, part), callBack)
    }

    //下载无缓存图片
    fun downloadImage(url: String, callBack: CallBack<Bitmap>) {
        handBitmap(retrofitApi!!.download(url), callBack)
    }

    //下载无缓存图片
    fun downloadImage(url: String, map: Map<String, Any>, callBack: CallBack<Bitmap>) {
        handBitmap(retrofitApi!!.download(url, map), callBack)
    }

    //处理文件
    private fun handFile(observable: Observable<ResponseBody>, fileName: String, callBack: CallBack<File>) {
        ProgressResponseBody.setCallBack(callBack)
        observable.map { responseBody ->
            val `is` = responseBody.byteStream()
            val file = File(FileUtil.saveFilePath!! + fileName)
            file.deleteOnExit()
            val fos = FileOutputStream(file)
            val bis = BufferedInputStream(`is`)
            val buffer = ByteArray(1024)
            var len: Int
            do {
                len = bis.read(buffer)
                if (len != -1) {
                    fos.write(buffer, 0, len)
                    fos.flush()
                }
            } while (true)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ file ->
                callBack.run {
//                    onResponse(response = file)
                }}, { throwable -> callBack.onError(throwable) })
    }

    //处理图片
    private fun handBitmap(observable: Observable<ResponseBody>, callBack: CallBack<Bitmap>) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888
                val bitmap = BitmapFactory.decodeStream(responseBody.byteStream(), null, options)
                callBack.run {
                    onResponse(response = bitmap)
                }
            }, { throwable -> callBack.onError(throwable) })
    }

    //处理字符串
    private fun handString(observable: Observable<String>, callBack: CallBack<String>) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ string ->
                //以前的code
                callBack.onResponse(string)
            }, { throwable -> callBack.onError(throwable) })
    }

    companion object {
        protected var retrofitApi: RetrofitApi? = null
        val getInstance: RetrofitService
            get() {
                if (retrofitApi == null) {
                    retrofitApi = RetrofitClient.instance!!.create(RetrofitApi::class.java)
                }
                return RetrofitService()
            }
    }

}
