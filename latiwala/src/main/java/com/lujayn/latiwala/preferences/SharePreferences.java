package com.lujayn.latiwala.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.lujayn.latiwala.common.ApplicationContext;

/**
 * Custom SharePreferences class which store ,retrive and clear data
 */
public class SharePreferences {
    private SharedPreferences preferences;
    private Context context = ApplicationContext.getAppContext();

    /**
     * set,get,clear full URL
     *
     * @return
     */
    public String getTrialURL() {
        preferences = null;
        preferences = context.getSharedPreferences("TrialURLPreference", Context.MODE_PRIVATE);
        String s = preferences.getString("TrialURL", "");
        return s;
    }

    public void setTrialURL(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("TrialURLPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TrialURL", s);
        editor.commit();
    }

    public void setTrialURLEmpty() {
        preferences = null;
        preferences = context.getSharedPreferences("TrialURLPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TrialURL", "");
        editor.commit();
    }

    /**
     * Set,get,clear First Half URL
     *
     * @return
     */
    public String getFirstHalfTrialURL() {
        preferences = null;
        preferences = context.getSharedPreferences("FirstHalfTrialURLPreference", Context.MODE_PRIVATE);
        String s = preferences.getString("FirstHalfTrialURL", "");
        return s;
    }

    public void setFirstHalfTrialURL(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("FirstHalfTrialURLPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FirstHalfTrialURL", s);
        editor.commit();
    }

    public void setFirstHalfTrialURLEmpty() {
        preferences = null;
        preferences = context.getSharedPreferences("FirstHalfTrialURLPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FirstHalfTrialURL", "");
        editor.commit();
    }


    /**
     * Set,get,clear Firebase token
     *
     * @return
     */
    public String getFirebaseToken() {
        preferences = null;
        preferences = context.getSharedPreferences("FirebaseTokenPreference", Context.MODE_PRIVATE);
        String s = preferences.getString("FirebaseToken", "");
        return s;
    }
    public void setFirebaseToken(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("FirebaseTokenPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FirebaseToken", s);
        editor.commit();
    }
    public void setFirebaseTokenEmpty() {
        preferences = null;
        preferences = context.getSharedPreferences("FirebaseTokenPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FirebaseToken", "");
        editor.commit();
    }


    /**
     * Set,get,clear Status
     *
     * @return
     */
    public String getStatus() {
        preferences = null;
        preferences = context.getSharedPreferences("StatusPreference", Context.MODE_PRIVATE);
        String s = preferences.getString("Status", "");
        return s;
    }
    public void setStatus(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("StatusPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Status", s);
        editor.commit();
    }
    public void setStatusEmpty() {
        preferences = null;
        preferences = context.getSharedPreferences("StatusPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Status", "");
        editor.commit();
    }

    /**
     * Set,get,clear UserId
     *
     * @return
     */
    public String getUserId() {
        preferences = null;
        preferences = context.getSharedPreferences("UserIdPreference", Context.MODE_PRIVATE);
        String s = preferences.getString("UserId", "");
        return s;
    }
    public void setUserId(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("UserIdPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserId", s);
        editor.commit();
    }
    public void setUserIdEmpty() {
        preferences = null;
        preferences = context.getSharedPreferences("UserIdPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserId", "");
        editor.commit();
    }


    /**
     * Set,get,clear UserEmail
     *
     * @return
     */
    public String getUserEmail() {
        preferences = null;
        preferences = context.getSharedPreferences("UserEmailPreference", Context.MODE_PRIVATE);
        String s = preferences.getString("UserEmail", "");
        return s;
    }
    public void setUserEmail(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("UserEmailPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserEmail", s);
        editor.commit();
    }
    public void setUserEmailEmpty() {
        preferences = null;
        preferences = context.getSharedPreferences("UserEmailPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserEmail", "");
        editor.commit();
    }

    /**
     * Set,get,clear UserName
     *
     * @return
     */
    public String getUserName() {
        preferences = null;
        preferences = context.getSharedPreferences("UserNamePreference", Context.MODE_PRIVATE);
        String s = preferences.getString("UserName", "");
        return s;
    }
        public void setUserName(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("UserNamePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserName", s);
        editor.commit();
    }
    public void setUserNameEmpty() {
        preferences = null;
        preferences = context.getSharedPreferences("UserNamePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserName", "");
        editor.commit();
    }


    /**
     * Set,get,clear AverageTime
     *
     * @return
     */
    public String getAverageTime() {
        preferences = null;
        preferences = context.getSharedPreferences("AverageTimePreference", Context.MODE_PRIVATE);
        String s = preferences.getString("AverageTime", "");
        return s;
    }

    public void setAverageTime(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("AverageTimePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AverageTime", s);
        editor.commit();
    }

    public void setAverageTimeEmpty() {
        preferences = null;
        preferences = context.getSharedPreferences("AverageTimePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AverageTime", "");
        editor.commit();
    }

    /**
     * Set,get,clear Count
     *
     * @return
     */
    public String getCount() {
        preferences = null;
        preferences = context.getSharedPreferences("CountPreference", Context.MODE_PRIVATE);
        String s = preferences.getString("Count", "");
        return s;
    }

    public void setCount(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("CountPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Count", s);
        editor.commit();
    }

    public void setCountEmpty() {
        preferences = null;
        preferences = context.getSharedPreferences("CountPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Count", "");
        editor.commit();
    }
}
