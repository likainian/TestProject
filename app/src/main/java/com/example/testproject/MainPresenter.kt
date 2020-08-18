package com.example.testproject

import com.example.testproject.network.CallBack
import com.example.testproject.network.RetrofitService
import com.example.testproject.util.SharedPreferencesUtil
import com.example.testproject.util.TimeUtil

class MainPresenter : MainContract.MainPresenter {
    private var mView: MainContract.MainView? = null

    fun attach(mView: MainContract.MainView) {
        this.mView = mView
    }

    override fun getHomeData() {
        RetrofitService.getInstance.requestGet("https://api.github.com/", object : CallBack<String>() {
            override fun onResponse(response: String) {
                SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.HOME_DATA, response)
                val time = TimeUtil.longToString(System.currentTimeMillis(), TimeUtil.FORMAT_YEAR_MONTH_DAY_TIME)
                SharedPreferencesUtil.getInstance().setHomeData("$time 调用成功\n$response")
                mView!!.showHomeData(response)
            }

            override fun onError(throwable: Throwable) {
                val time = TimeUtil.longToString(System.currentTimeMillis(), TimeUtil.FORMAT_YEAR_MONTH_DAY_TIME)
                SharedPreferencesUtil.getInstance().setHomeData("$time 调用失败\n$throwable.message")
                super.onError(throwable)
            }
        })
    }
}