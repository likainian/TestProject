package com.example.testproject.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.*;

/**
 * Created by mike.li.
 */

public class FileUtil {

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    //屏幕的高
    public static int getDisplayHeight(Context context) {
        return Math.max(context.getResources().getDisplayMetrics().widthPixels,
                context.getResources().getDisplayMetrics().heightPixels);
    }
    //屏幕的宽
    public static int getDisplayWidth(Context context) {
        return Math.min(context.getResources().getDisplayMetrics().widthPixels, context
                .getResources().getDisplayMetrics().heightPixels);
    }

    public static float getDisplayDensity(Context context){
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static String getSavePicturePath(){
        return FileUtil.createPath(Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"irenshi"+ File.separator+"picture"+ File.separator);
    }
    public static String getSaveFilePath(){
        return FileUtil.createPath(Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"irenshi"+ File.separator+"file"+ File.separator);
    }

    public static String getSaveDBPath(){
        return FileUtil.createPath(Environment.getExternalStorageDirectory().getAbsolutePath()+
                File.separator+"irenshi"+ File.separator+"db"+ File.separator);
    }

    public static boolean checkSdCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static boolean isExistFile(File file) {
        return file!=null && file.exists();
    }

    public static boolean isExistFile(String file) {
        return CheckUtils.isNotEmpty(file) && new File(file).exists();
    }


    public static void compressImage(File file, File compressFile){
        if(file.length()<1024*1024){
            FileUtil.copy(file.getPath(), compressFile.getPath());
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中,这里从90开始
        int quality = 90;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        Log.d("ttt","before=="+baos.toByteArray().length);
        //  循环判断如果压缩后图片是否大于maxSize(例如:100kb),大于继续压缩
        while (baos.toByteArray().length > 1024*1024) {
            baos.reset(); // 重置baos即清空baos
            quality -= 10;// 每次都减少10
            // 这里压缩options，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        Log.d("ttt","after=="+baos.toByteArray().length);
        try{
            FileOutputStream fos = new FileOutputStream(compressFile.getPath());
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveBitmap(File file, Bitmap bitmap) {
        file.deleteOnExit();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, outputStream);
            fos.write(outputStream.toByteArray());
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String createPath(String path) {
        File file = new File(path);
        if (file.exists()) return path;
        boolean mkdirs = file.mkdirs();
        if (mkdirs) {
            return path;
        } else {
            return null;
        }
    }

    public static void copy(String oldPath, String newPath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(oldPath);    //读入原文件
            FileOutputStream fileOutputStream = new FileOutputStream(newPath);
            byte[] buffer = new byte[1024];
            int byteRead;
            while ((byteRead = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Uri getFileUri(Context context, File file){
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(context, "com.ihr.PROVIDER", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }
}