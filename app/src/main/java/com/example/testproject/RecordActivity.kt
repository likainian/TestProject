package com.example.testproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import android.widget.TextView
import com.example.testproject.util.SharedPreferencesUtil

class RecordActivity : AppCompatActivity() {
    private var mToolbarLeft: FrameLayout? = null
    private var mToolbarMiddle: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        mToolbarMiddle = findViewById(R.id.toolbar_middle)
        mToolbarMiddle!!.text = getString(R.string.text_record)
        mToolbarLeft = findViewById(R.id.toolbar_left)
        mToolbarLeft!!.setOnClickListener { onBackPressed() }
        val mTvRecord = findViewById<TextView>(R.id.tv_record)
        val homeData = SharedPreferencesUtil.getInstance().homeData
        for (data in homeData) {
            mTvRecord.append(data)
            mTvRecord.append("\n\n")
        }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, RecordActivity::class.java))
        }
    }
}
