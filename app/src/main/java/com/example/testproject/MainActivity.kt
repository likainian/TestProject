package com.example.testproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import android.widget.TextView
import com.example.testproject.util.SharedPreferencesUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MainContract.MainView {

    private var mTvContent: TextView? = null
    private var mToolbarRight: FrameLayout? = null
    private var mainPresenter: MainPresenter? = null
    private var subscribe: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTvContent = findViewById(R.id.tv_content)
        mToolbarRight = findViewById(R.id.toolbar_right)
        mToolbarRight!!.setOnClickListener { RecordActivity.startActivity(this@MainActivity) }
        val homeData = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.HOME_DATA, "")
        mTvContent!!.text = homeData
        mainPresenter = MainPresenter()
        mainPresenter!!.attach(this)
        initData()
    }

    private fun initData() {
        subscribe = Observable.interval(0, 5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ mainPresenter!!.getHomeData() }, { throwable -> throwable.printStackTrace() })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (subscribe != null && !subscribe!!.isDisposed) subscribe!!.dispose()
    }

    override fun showHomeData(response: String) {
        mTvContent!!.text = response
    }

}