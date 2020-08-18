package com.example.testproject.network.interceptor

import com.example.testproject.network.CallBack
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

import java.io.File
import java.io.IOException

/**
 * Created by mike.li.
 * 进度
 */

class ProgressResponseBody(private val responseBody: ResponseBody) : ResponseBody() {
    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        return Okio.buffer(source(responseBody.source()))
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var readLength = 0L
            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                readLength += if (bytesRead != -1L) bytesRead else 0
                if (callBack != null) callBack!!.onProgress(readLength, responseBody.contentLength())
                return bytesRead
            }
        }
    }

    companion object {
        private var callBack: CallBack<File>? = null

        fun setCallBack(callBack: CallBack<File>) {
            ProgressResponseBody.callBack = callBack
        }
    }
}
