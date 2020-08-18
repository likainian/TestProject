package com.example.testproject.util

import android.app.Application
import android.content.SharedPreferences
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.example.testproject.app.TestApplication

import java.util.ArrayList

import android.content.Context.MODE_PRIVATE

class SharedPreferencesUtil(application: Application) {
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences(application.packageName, MODE_PRIVATE)
    val homeData: List<String> get() = getObjectList(HOME_DATA_TIME, String::class.java)


    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    private fun <T> putObject(key: String, `object`: T?) {
        if (`object` == null) {
            remove(key)
        } else {
            sharedPreferences.edit().putString(key, JSONObject.toJSONString(`object`)).apply()
        }
    }

    private fun <T> getObject(key: String, clazz: Class<T>): T? {
        val string = sharedPreferences.getString(key, "")
        return if (CheckUtils.isEmpty<Any>(string)) {
            null
        } else {
            try {
                JSONObject.parseObject(string, clazz)
            } catch (e: Exception) {
                null
            }

        }
    }

    private fun <T> getObject(key: String, clazz: Class<T>, defValue: T): T {
        val string = sharedPreferences.getString(key, "")
        return if (CheckUtils.isEmpty<Any>(string)) {
            defValue
        } else {
            JSONObject.parseObject(string, clazz)
        }
    }

    private fun <T> getObjectList(key: String, clazz: Class<T>): MutableList<T> {
        val jsonString = getString(key, "")
        return if (CheckUtils.isNotEmpty(jsonString)) {
            JSON.parseArray(jsonString, clazz)
        } else {
            ArrayList()
        }
    }

    private fun <T> putObjectList(key: String, list: List<T>) {
        if (CheckUtils.isNotEmpty<Any>(list)) {
            putString(key, JSON.toJSONString(list))
        } else {
            remove(key)
        }
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    private fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    fun getString(key: String, value: String): String? {
        return sharedPreferences.getString(key, value)
    }

    fun getBoolean(key: String, value: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, value)
    }

    private fun getLong(key: String, value: Long): Long {
        return sharedPreferences.getLong(key, value)
    }

    fun setHomeData(data: String) {
        val objectList = getObjectList(HOME_DATA_TIME, String::class.java)
        objectList.add(0, data)
        putObjectList(HOME_DATA_TIME, objectList)
    }

    companion object {
        private var instance: SharedPreferencesUtil? = null

        const val HOME_DATA = "HOME_DATA"
        const val HOME_DATA_TIME = "HOME_DATA_TIME"

        @Synchronized
        fun getInstance(): SharedPreferencesUtil {
            if (null == instance) {
                instance = SharedPreferencesUtil(TestApplication.getInstance()!!)
            }
            return instance as SharedPreferencesUtil
        }
    }

}
