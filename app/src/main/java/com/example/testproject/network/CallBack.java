package com.example.testproject.network;


import com.example.testproject.BuildConfig;
import com.example.testproject.util.LogUtil;
import com.example.testproject.util.ToastUtil;

/**
 * Created by mike.li.
 */

public abstract class CallBack<T> {
    public abstract void onResponse(T response);
    //返回进度
    public void onProgress(long readLength, long contentLength){
        LogUtil.i("percent:"+readLength+":"+contentLength);
    }
    public void onError(final Throwable throwable){
        if(BuildConfig.DEBUG){
            throwable.printStackTrace();
        }
        ToastUtil.showToast(throwable.getMessage());
    }
}
