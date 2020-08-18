package com.example.testproject.util

import android.view.Gravity
import android.widget.Toast
import com.example.testproject.app.TestApplication
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by mike.li.
 */

object ToastUtil {
    private var toast: Toast? = null
    private var oldMsg: String? = null
    private var time: Long = 0
    fun showToast(message: String) {
        if (CheckUtils.isEmpty<Any>(message)) return
        Observable.create(ObservableOnSubscribe<Any> {
            if (message != oldMsg) {
                if (toast != null) {
                    toast!!.cancel()
                }
                toast = Toast.makeText(TestApplication.getInstance(), message, Toast.LENGTH_LONG)
                toast!!.setGravity(Gravity.CENTER, 0, 0)
                toast!!.show()
                time = System.currentTimeMillis()
            } else {
                // 显示内容一样时，只有间隔时间大于2秒时才显示
                if (System.currentTimeMillis() - time > 2000) {
                    if (toast != null) {
                        toast!!.cancel()
                    }
                    toast = Toast.makeText(TestApplication.getInstance(), message, Toast.LENGTH_LONG)
                    toast!!.setGravity(Gravity.CENTER, 0, 0)
                    toast!!.show()
                    time = System.currentTimeMillis()
                }
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe()
        oldMsg = message
    }
}
