package com.lujayn.latiwala.common;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lujayn.latiwala.bean.BeanSearchProduct;
import com.lujayn.latiwala.payumoney.AppEnvironment;
import com.lujayn.latiwala.utils.TypefaceUtil;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-005 on 16-03-2017.
 */

public class ApplicationContext extends MultiDexApplication {

    public static final String TAG = ApplicationContext.class.getSimpleName();
    public static Context context;
    public static ApplicationContext rest;
    private RequestQueue mRequestQueue;
    AppEnvironment appEnvironment;
    public static ArrayList<BeanSearchProduct> beanSearchProducts = new ArrayList<>();
    public static ArrayList<BeanSearchProduct> beanSearchProductsTemp = new ArrayList<>();
    public  static int searchCharacterLength=2;

    @Override
    public void onCreate() {
        super.onCreate();
        rest = this;
        appEnvironment = AppEnvironment.PRODUCTION;
        context = getApplicationContext();
        TypefaceUtil.overrideFont(getApplicationContext(), "monospace", "assets/fonts/arial.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
    }

    public static synchronized ApplicationContext getInstance() {
        return rest;
    }

    public static Context getAppContext() {
        return ApplicationContext.context;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }
}

