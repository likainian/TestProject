package com.example.testproject.app

import android.app.Application
/**
 * Created by mike.li.
 */
class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    companion object {
        private var instance: TestApplication? = null
        @Synchronized
        fun getInstance(): TestApplication? {
            return instance
        }
    }
}
