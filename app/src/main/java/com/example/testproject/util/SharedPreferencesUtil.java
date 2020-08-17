package com.example.testproject.util;

import android.app.Application;
import android.content.SharedPreferences;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.testproject.app.TestApplication;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesUtil {
    private static SharedPreferencesUtil instance = null;
    private SharedPreferences sharedPreferences;

    public static final String HOME_DATA = "HOME_DATA";
    public static final String HOME_DATA_TIME = "HOME_DATA_TIME";


    public SharedPreferencesUtil(Application application) {
        this.sharedPreferences = application.getSharedPreferences(application.getPackageName(), MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesUtil getInstance() {
        if (null == instance) {
            instance = new SharedPreferencesUtil(TestApplication.Companion.getInstance());
        }
        return instance;
    }

    public void remove(String key){
        sharedPreferences.edit().remove(key).apply();
    }

    private <T> void putObject(String key, T object) {
        if(object==null){
            remove(key);
        }else {
            sharedPreferences.edit().putString(key,JSONObject.toJSONString(object)).apply();
        }
    }

    private <T> T getObject(String key, Class<T> clazz) {
        String string = sharedPreferences.getString(key, "");
        if(CheckUtils.isEmpty(string)){
            return null;
        }else {
            try {
                return JSONObject.parseObject(string,clazz);
            }catch (Exception e){
                return null;
            }
        }
    }
    private <T> T getObject(String key, Class<T> clazz, T defValue) {
        String string = sharedPreferences.getString(key, "");
        if(CheckUtils.isEmpty(string)){
            return defValue;
        }else {
            return JSONObject.parseObject(string,clazz);
        }
    }

    private <T> List<T> getObjectList(String key, Class<T> clazz) {
        String jsonString = getString(key, "");
        if (CheckUtils.isNotEmpty(jsonString)) {
            return JSON.parseArray(jsonString, clazz);
        }else {
            return new ArrayList<>();
        }
    }

    private <T> void putObjectList(String key, List<T> list) {
        if (CheckUtils.isNotEmpty(list)) {
            putString(key, JSON.toJSONString(list));
        } else {
            remove(key);
        }
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key,value).apply();
    }

    private void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key,value).apply();
    }

    private void putLong(String key, long value){
        sharedPreferences.edit().putLong(key,value).apply();
    }

    public String getString(String key, String value) {
        return sharedPreferences.getString(key, value);
    }

    public boolean getBoolean(String key, boolean value) {
        return sharedPreferences.getBoolean(key, value);
    }

    private long getLong(String key, long value) {
        return sharedPreferences.getLong(key, value);
    }

    public void setHomeData(String data){
        List<String> objectList = getObjectList(HOME_DATA_TIME, String.class);
        objectList.add(0,data);
        putObjectList(HOME_DATA_TIME,objectList);
    }
    public List<String> getHomeData(){
        return getObjectList(HOME_DATA_TIME, String.class);
    }
}
