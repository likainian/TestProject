package com.example.testproject.network.interceptor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.testproject.network.CallBack;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by mike.li.
 * 进度
 */

public class ProgressResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private static CallBack<File> callBack;

    public static void setCallBack(CallBack<File> callBack) {
        ProgressResponseBody.callBack = callBack;
    }

    public ProgressResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }
    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(source(responseBody.source()));
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long readLength = 0L;
            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                readLength += bytesRead != -1 ? bytesRead : 0;
                if(callBack!=null)callBack.onProgress(readLength,responseBody.contentLength());
                return bytesRead;
            }
        };
    }
}
