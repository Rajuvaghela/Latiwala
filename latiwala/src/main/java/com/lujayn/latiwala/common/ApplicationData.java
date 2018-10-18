package com.lujayn.latiwala.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.lujayn.latiwala.R;
import com.lujayn.latiwala.bean.BeanCategory;
import com.lujayn.latiwala.bean.BeanSubCategory;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.network.ConnectionDetector;
import com.lujayn.latiwala.common.AppConstant.HTTPResponseCode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationData {
    private static ApplicationData sharedInstance;

    Context context;
    private SharedPreferences preferences;
    //private DataBaseHelper dbManager;

    private AppConstant.HTTPResponseCode responseCode;

    private ConnectionDetector connectionDetector;
    private ArrayList<BeanCategory> categoryList;
    private ArrayList<String> sluglist;
    private ArrayList<String> responseproduct;
    private ArrayList<BeanSubCategory> subCategories;

    private ApplicationData() {

    }

    public static ApplicationData getSharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new ApplicationData();
            sharedInstance.initialize();
        }
        return sharedInstance;
    }

    private void initialize() {

        setApplicationPreferences();

        AppDebugLog.setFileLogMode(AppConstant.FILE_DEBUG);
        AppDebugLog.setProductionMode(AppConstant.PRODUCTION_DEBUG);


        this.context = ApplicationContext.getAppContext();

        connectionDetector = new ConnectionDetector(context);

        categoryList = new ArrayList<BeanCategory>();
        sluglist = new ArrayList<String>();
        responseproduct = new ArrayList<String>();
        subCategories = new ArrayList<BeanSubCategory>();

    }

    public Context getContext() {
        return ApplicationContext.getAppContext();
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public ConnectionDetector getConnectionDetector() {
        return connectionDetector;
    }

    public HTTPResponseCode getResponseCode() {
        return responseCode;
    }





    public void setResponseCode(HTTPResponseCode responseCode) {
        this.responseCode = responseCode;
    }


    /**
     * Application Preference related functions
     */

    private void setApplicationPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.getAppContext());

        if (preferences.getInt(AppConstant.dBVersion, AppConstant.defaultDBVersion) == AppConstant.defaultDBVersion) {
            preferences.edit().putInt(AppConstant.dBVersion, AppConstant.defaultDBVersion).commit();
        }
    }

    public void showUserAlert(Context context, String title, String message, OnClickListener listner) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(context.getText(R.string.btn_ok), listner).show();
    }

    public void showConfirmationAlert(Context context, String title, String message, String btnPositiveTitle,
                                      String btnNegativeTitle, OnClickListener btnPositiveListner, OnClickListener btnNegativeListner) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(btnPositiveTitle, btnPositiveListner)
                .setNegativeButton(btnNegativeTitle, btnNegativeListner).show();
    }

    public int getResId(String variableName, Context context, Class<?> c) {
        try {
            java.lang.reflect.Field idField = c.getField(variableName);
            return idField.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getAndroidVersion() {
        int version = android.os.Build.VERSION.SDK_INT;
        System.out.println("Android Version : " + version);
        return version;
    }

    public void setDBVersion(int dbVersion) {
        preferences.edit().putInt(AppConstant.dBVersion, dbVersion).commit();
    }

    public int getDBVersion() {
        return preferences.getInt(AppConstant.dBVersion, AppConstant.defaultDBVersion);
    }

    public ArrayList<BeanCategory> getCategoryList() {
        return categoryList;
    }

    public static void setSharedInstance(ApplicationData sharedInstance) {
        ApplicationData.sharedInstance = sharedInstance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void setConnectionDetector(ConnectionDetector connectionDetector) {
        this.connectionDetector = connectionDetector;
    }

    public ArrayList<String> getSluglist() {
        return sluglist;
    }

    public void setSluglist(ArrayList<String> sluglist) {
        this.sluglist = sluglist;
    }

    public void setCategoryList(ArrayList<BeanCategory> categoryList) {
        this.categoryList = categoryList;


    }

    public ArrayList<String> getResponseproduct() {
        return responseproduct;
    }

    public void setResponseproduct(ArrayList<String> responseproduct) {
        this.responseproduct = responseproduct;
    }

    public ArrayList<BeanSubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<BeanSubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public static float getFloatValue(float value){
        DecimalFormat df2 = new DecimalFormat("#.00");
        //float y = Float.parseFloat(String.format("%.2f",value).replace(',','.'));
        float y = Float.parseFloat(df2.format(value).replace(',','.'));
        Log.d("TAG", "getFloatValue()==== =="+ df2.format(value));
        return y;

    }

    public static float getFloatValue(double value){
        DecimalFormat df2 = new DecimalFormat("#.00");
        // float y = Float.parseFloat(String.format("%.2f",value).replace(',','.'));
        float y = Float.parseFloat(df2.format(value).replace(',','.'));
        Log.d("TAG", "getFloatValue()==== =="+ df2.format(value));
        return y;
    }

    public static String getStringValue(double value){
        DecimalFormat df2 = new DecimalFormat("#.00");
        // float y = Float.parseFloat(String.format("%.2f",value).replace(',','.'));
        String y = df2.format(value).replace(',','.');
        Log.d("TAG", "getFloatValue()==== =="+ df2.format(value));
        return y;
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}

