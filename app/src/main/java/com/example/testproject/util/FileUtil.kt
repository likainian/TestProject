package com.example.testproject.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import android.util.Log

import java.io.*

/**
 * Created by mike.li.
 */

object FileUtil {

    val savePicturePath: String?
        get() = FileUtil.createPath(
            Environment.getExternalStorageDirectory().absolutePath +
                    File.separator + "irenshi" + File.separator + "picture" + File.separator
        )
    val saveFilePath: String?
        get() = FileUtil.createPath(
            Environment.getExternalStorageDirectory().absolutePath +
                    File.separator + "irenshi" + File.separator + "file" + File.separator
        )

    val saveDBPath: String?
        get() = FileUtil.createPath(
            Environment.getExternalStorageDirectory().absolutePath +
                    File.separator + "irenshi" + File.separator + "db" + File.separator
        )

    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    //屏幕的高
    fun getDisplayHeight(context: Context): Int {
        return Math.max(
            context.resources.displayMetrics.widthPixels,
            context.resources.displayMetrics.heightPixels
        )
    }

    //屏幕的宽
    fun getDisplayWidth(context: Context): Int {
        return Math.min(
            context.resources.displayMetrics.widthPixels, context
                .resources.displayMetrics.heightPixels
        )
    }

    fun getDisplayDensity(context: Context): Float {
        return context.resources.displayMetrics.scaledDensity
    }

    fun checkSdCard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    fun isExistFile(file: File?): Boolean {
        return file != null && file.exists()
    }

    fun isExistFile(file: String): Boolean {
        return CheckUtils.isNotEmpty<Any>(file) && File(file).exists()
    }


    fun compressImage(file: File, compressFile: File) {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val baos = ByteArrayOutputStream()
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中,这里从90开始
        var quality = 90
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        Log.d("ttt", "before==" + baos.toByteArray().size)
        //  循环判断如果压缩后图片是否大于maxSize(例如:100kb),大于继续压缩
        while (baos.toByteArray().size > 1024 * 1024) {
            baos.reset() // 重置baos即清空baos
            quality -= 10// 每次都减少10
            // 这里压缩options，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        }
        Log.d("ttt", "after==" + baos.toByteArray().size)
        try {
            val fos = FileOutputStream(compressFile.path)
            fos.write(baos.toByteArray())
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun saveBitmap(file: File, bitmap: Bitmap) {
        file.deleteOnExit()
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, outputStream)
            fos.write(outputStream.toByteArray())
            fos.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun createPath(path: String): String? {
        val file = File(path)
        if (file.exists()) return path
        val mkdirs = file.mkdirs()
        return if (mkdirs) {
            path
        } else {
            null
        }
    }

    fun getFileUri(context: Context, file: File): Uri {
        val fileUri: Uri
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = FileProvider.getUriForFile(context, "com.ihr.PROVIDER", file)
        } else {
            fileUri = Uri.fromFile(file)
        }
        return fileUri
    }
}