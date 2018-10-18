package com.lujayn.latiwala.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lujayn.latiwala.bean.BeanUserData;
import com.lujayn.latiwala.bean.SettingOption;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Shailesh on 29/06/16.
 */
public class SessionManager {

    public void setPreferences(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstant.APP_TAG, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }

    public void setPreferences(Context context, String key, Integer value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstant.APP_TAG, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();

    }


    public void setPreferences(Context context, String key, SettingOption value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstant.APP_TAG, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key,json);
        editor.commit();

    }

    public  String getPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences(AppConstant.APP_TAG,Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }



    public SettingOption getPreferencesOption(Context context, String key){

        SettingOption settingOption = new SettingOption();
        SharedPreferences prefs = context.getSharedPreferences(AppConstant.APP_TAG,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        Type type_one = new TypeToken<SettingOption>() {
        }.getType();
        settingOption = gson.fromJson(json,type_one);
        return settingOption;
    }



    public void setPreferencesArraylist(Context context, String key, ArrayList<BeanUserData> value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(AppConstant.APP_TAG, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key,json);
        editor.commit();

    }


    public  ArrayList getPreferencesArraylist(Context context,String key) {

        SharedPreferences prefs = context.getSharedPreferences(AppConstant.APP_TAG,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        Type type = new TypeToken<ArrayList<BeanUserData>>() {}.getType();
        ArrayList<BeanUserData> arrayList = gson.fromJson(json, type);

        return arrayList;
    }
}
