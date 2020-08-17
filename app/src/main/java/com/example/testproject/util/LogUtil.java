package com.example.testproject.util;

import android.util.Log;
import com.example.testproject.BuildConfig;

/**
 * Created by mike.li.
 */

public class LogUtil {
    public static void i(String message){
        if(BuildConfig.DEBUG){
            log(generateTag(), message);
        }
    }
    public static void i(String tag, String message){
        if(BuildConfig.DEBUG){
            log(tag, message);
        }
    }
    private static String generateTag() {
        StackTraceElement sElements = new Throwable().getStackTrace()[2];
        String className = sElements.getFileName();
        String methodName = sElements.getMethodName();
        int lineNumber = sElements.getLineNumber();
        return String.format("%s:%s(%s:%d)", className, methodName,className, lineNumber);
    }
    private static void log(String tag, String msg) {
        if (tag == null || tag.length() == 0|| msg == null || msg.length() == 0)return;
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.i(tag, msg);
        }else {
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.substring(segmentSize, msg.length());
                Log.i(tag, logContent);
            }
            Log.i(tag, msg);// 打印剩余日志
        }
    }
}
