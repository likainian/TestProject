package com.example.testproject.network


import com.example.testproject.BuildConfig
import com.example.testproject.util.LogUtil
import com.example.testproject.util.ToastUtil

/**
 * Created by mike.li.
 */

abstract class CallBack<T> {
    abstract fun onResponse(response: T)
    //返回进度
    fun onProgress(readLength: Long, contentLength: Long) {
        LogUtil.i("percent:$readLength:$contentLength")
    }

    open fun onError(throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace()
        }
        throwable.message?.let { ToastUtil.showToast(it) }
    }
}
