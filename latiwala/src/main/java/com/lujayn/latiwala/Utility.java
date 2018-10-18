package com.lujayn.latiwala;

/**
 * Created by lujayn-15 on 23/4/18.
 */

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lujayn.latiwala.common.ApplicationContext;


public class Utility {
    static Typeface tf;
    private static Context context = ApplicationContext.context;
    private static SharedPreferences preferences;
    public static Context act = ApplicationContext.getAppContext();
    private static ProgressDialog pd;
    public static TelephonyManager telephonyManager;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static String IMEI_Number_Holder;



    public static Typeface getTypeFace() {
        tf = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        // tf = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        return tf;
    }


    public static Typeface getTypeFaceTab() {
        tf = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
        // tf = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        return tf;
    }

    public static boolean checkConnectivity() {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (cm == null) {
                return false;
            } else if (info == null) {
                return false;
            } else if (info.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static ProgressDialog getDialog(Activity activity) {
        pd = new ProgressDialog(activity);
        pd.setMessage("Please Wait...");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
        return pd;
    }

    public static void showMsg(int id) {
        Toast.makeText(context, context.getResources().getString(id), Toast.LENGTH_SHORT).show();
    }


    public static String getEmployeeCode() {
        preferences = null;
        preferences = context.getSharedPreferences("EmployeeCodePreference", Context.MODE_PRIVATE);
        String s = preferences.getString("EmployeeCode", "");
        return s;
    }

    public static void setEmployeeCode(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("EmployeeCodePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EmployeeCode", s);
        editor.commit();
    }

    public static void setEmployeeCodeBank() {
        preferences = null;
        preferences = context.getSharedPreferences("EmployeeCodePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EmployeeCode", "");
        editor.commit();
    }


    //ManagerCode  start
    public static String getManagerCodePreference() {
        preferences = null;
        preferences = context.getSharedPreferences("ManagerCodePreference", Context.MODE_PRIVATE);
        String s = preferences.getString("ManagerCode", "");

        return s;
    }

    public static void setManagerCodePreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("ManagerCodePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ManagerCode", s);
        editor.commit();
    }

    public static void setManagerCodeBank() {
        preferences = null;
        preferences = context.getSharedPreferences("ManagerCodePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ManagerCode", "");
        editor.commit();
    }
    //ManagerCode  end

    //Roll and Rights
    public static String getAnnouncementAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementAdd", Context.MODE_PRIVATE);
        String s = preferences.getString("AnnouncementAdd", "");

        return s;
    }

    public static void setAnnouncementAdd(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AnnouncementAdd", s);
        editor.commit();
    }

    public static void setAnnouncementAddBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AnnouncementAdd", "");
        editor.commit();
    }

    public static String getAnnouncementUpdate() {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementUpdate", Context.MODE_PRIVATE);
        String s = preferences.getString("AnnouncementAdd", "");

        return s;
    }

    public static void setAnnouncementUpdate(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AnnouncementUpdate", s);
        editor.commit();
    }

    public static void setAnnouncementUpdateBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AnnouncementUpdate", "");
        editor.commit();
    }

    public static String getAnnouncementDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementDelete", Context.MODE_PRIVATE);
        String s = preferences.getString("AnnouncementDelete", "");

        return s;
    }

    public static void setAnnouncementDelete(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AnnouncementDelete", s);
        editor.commit();
    }

    public static void setAnnouncementDeleteBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AnnouncementDelete", "");
        editor.commit();
    }

    public static String getAnnouncementPublish() {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementPublish", Context.MODE_PRIVATE);
        String s = preferences.getString("AnnouncementPublish", "");

        return s;
    }

    public static void setAnnouncementPublish(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementPublish", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AnnouncementPublish", s);
        editor.commit();
    }

    public static void setAnnouncementPublishBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("AnnouncementPublish", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("AnnouncementPublish", "");
        editor.commit();
    }

    public static String getEventAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("EventAdd", Context.MODE_PRIVATE);
        String s = preferences.getString("EventAdd", "");

        return s;
    }

    public static void setEventAdd(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("EventAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EventAdd", s);
        editor.commit();
    }

    public static void setEventAddBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("EventAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EventAdd", "");
        editor.commit();
    }

    public static String getEventUpdate() {
        preferences = null;
        preferences = context.getSharedPreferences("EventUpdate", Context.MODE_PRIVATE);
        String s = preferences.getString("EventUpdate", "");

        return s;
    }

    public static void setEventUpdate(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("EventUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EventUpdate", s);
        editor.commit();
    }

    public static void setEventUpdateBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("EventUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EventUpdate", "");
        editor.commit();
    }

    public static String getEventDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("EventDelete", Context.MODE_PRIVATE);
        String s = preferences.getString("EventDelete", "");

        return s;
    }

    public static void setEventDelete(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("EventDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EventDelete", s);
        editor.commit();
    }

    public static void setEventDeleteBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("EventDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EventDelete", "");
        editor.commit();
    }

    public static String getEventPublish() {
        preferences = null;
        preferences = context.getSharedPreferences("EventPublish", Context.MODE_PRIVATE);
        String s = preferences.getString("EventPublish", "");

        return s;
    }

    public static void setEventPublish(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("EventPublish", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EventPublish", s);
        editor.commit();
    }

    public static void setEventPublishBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("EventPublish", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("EventPublish", "");
        editor.commit();
    }

    public static String getTaskAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("TaskAdd", Context.MODE_PRIVATE);
        String s = preferences.getString("TaskAdd", "");

        return s;
    }

    public static void setTaskAdd(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("TaskAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TaskAdd", s);
        editor.commit();
    }

    public static void setTaskAddBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("TaskAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TaskAdd", "");
        editor.commit();
    }

    public static String getTaskUpdate() {
        preferences = null;
        preferences = context.getSharedPreferences("TaskUpdate", Context.MODE_PRIVATE);
        String s = preferences.getString("TaskUpdate", "");

        return s;
    }

    public static void setTaskUpdate(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("TaskUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TaskUpdate", s);
        editor.commit();
    }

    public static void setTaskUpdateBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("TaskUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TaskUpdate", "");
        editor.commit();
    }

    public static String getTaskDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("TaskDelete", Context.MODE_PRIVATE);
        String s = preferences.getString("TaskDelete", "");

        return s;
    }

    public static void setTaskDelete(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("TaskDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TaskDelete", s);
        editor.commit();
    }

    public static void setTaskDeleteBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("TaskDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TaskDelete", "");
        editor.commit();
    }

    public static String getTodo() {
        preferences = null;
        preferences = context.getSharedPreferences("Todo", Context.MODE_PRIVATE);
        String s = preferences.getString("Todo", "");

        return s;
    }

    public static void setTodo(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("Todo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Todo", s);
        editor.commit();
    }
    public static void setTodoBlank() {
        preferences = null;
        preferences = context.getSharedPreferences("Todo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Todo", "");
        editor.commit();
    }
    public static String getApprovalAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalAdd", Context.MODE_PRIVATE);
        String s = preferences.getString("ApprovalAdd", "");

        return s;
    }
    public static void setApprovalAdd(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ApprovalAdd", s);
        editor.commit();
    }

    public static void setApprovalAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ApprovalAdd", "");
        editor.commit();
    }
    public static String getApprovalUpdate() {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalUpdate", Context.MODE_PRIVATE);
        String s = preferences.getString("ApprovalUpdate", "");
        return s;
    }

    public static void setApprovalUpdate(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ApprovalUpdate", s);
        editor.commit();
    }

    public static void setApprovalUpdate() {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ApprovalUpdate", "");
        editor.commit();
    }
    public static String getApprovalDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalDelete", Context.MODE_PRIVATE);
        String s = preferences.getString("ApprovalDelete", "");
        return s;
    }

    public static void setApprovalDelete(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ApprovalDelete", s);
        editor.commit();
    }

    public static void setApprovalDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ApprovalDelete", "");
        editor.commit();
    }
    public static String getApprovalTakeAction() {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalTakeAction", Context.MODE_PRIVATE);
        String s = preferences.getString("ApprovalTakeAction", "");
        return s;
    }

    public static void setApprovalTakeAction(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalTakeAction", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ApprovalTakeAction", s);
        editor.commit();
    }

    public static void setApprovalTakeAction() {
        preferences = null;
        preferences = context.getSharedPreferences("ApprovalTakeAction", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ApprovalTakeAction", "");
        editor.commit();
    }
    public static String getMarketAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("MarketAdd", Context.MODE_PRIVATE);
        String s = preferences.getString("MarketAdd", "");
        return s;
    }

    public static void setMarketAdd(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("MarketAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MarketAdd", s);
        editor.commit();
    }

    public static void setMarketAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("MarketAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MarketAdd", "");
        editor.commit();
    }
    public static String getMarketUpdate() {
        preferences = null;
        preferences = context.getSharedPreferences("MarketUpdate", Context.MODE_PRIVATE);
        String s = preferences.getString("MarketUpdate", "");
        return s;
    }

    public static void setMarketUpdate(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("MarketUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MarketUpdate", s);
        editor.commit();
    }

    public static void setMarketUpdate() {        preferences = null;
        preferences = context.getSharedPreferences("MarketUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MarketUpdate", "");
        editor.commit();
    }
    public static String getMarketDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("MarketDelete", Context.MODE_PRIVATE);
        String s = preferences.getString("MarketDelete", "");
        return s;
    }
    public static void setMarketDelete(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("MarketDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MarketDelete", s);
        editor.commit();
    }

    public static void setMarketDelete() {        preferences = null;
        preferences = context.getSharedPreferences("MarketDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MarketDelete", "");
        editor.commit();
    }
    public static String getKnowledgeAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("KnowledgeAdd", Context.MODE_PRIVATE);
        String s = preferences.getString("KnowledgeAdd", "");
        return s;
    }
    public static void setKnowledgeAdd(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("KnowledgeAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("KnowledgeAdd", s);
        editor.commit();
    }

    public static void setKnowledgeAdd() {        preferences = null;
        preferences = context.getSharedPreferences("KnowledgeAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("KnowledgeAdd", "");
        editor.commit();
    }
    public static String getKnowledgeUpdate() {
        preferences = null;
        preferences = context.getSharedPreferences("KnowledgeUpdate", Context.MODE_PRIVATE);
        String s = preferences.getString("KnowledgeUpdate", "");
        return s;
    }
    public static void setKnowledgeUpdate(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("KnowledgeUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("KnowledgeUpdate", s);
        editor.commit();
    }

    public static void setKnowledgeUpdate() {        preferences = null;
        preferences = context.getSharedPreferences("KnowledgeUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("KnowledgeUpdate", "");
        editor.commit();
    }
    public static String getKnowledgeDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("KnowledgeDelete", Context.MODE_PRIVATE);
        String s = preferences.getString("KnowledgeDelete", "");
        return s;
    }
    public static void setKnowledgeDelete(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("KnowledgeDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("KnowledgeDelete", s);
        editor.commit();
    }

    public static void setKnowledgeDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("KnowledgeDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("KnowledgeDelete", "");
        editor.commit();
    }
    public static String getFileAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("FileAdd", Context.MODE_PRIVATE);
        String s = preferences.getString("FileAdd", "");
        return s;
    }
    public static void setFileAdd(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("FileAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FileAdd", s);
        editor.commit();
    }

    public static void setFileAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("FileAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FileAdd", "");
        editor.commit();
    }
    public static String getFileUpdate() {
        preferences = null;
        preferences = context.getSharedPreferences("FileUpdate", Context.MODE_PRIVATE);
        String s = preferences.getString("FileUpdate", "");
        return s;
    }
    public static void setFileUpdate(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("FileUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FileUpdate", s);
        editor.commit();
    }

    public static void setFileUpdate() {
        preferences = null;
        preferences = context.getSharedPreferences("FileUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FileUpdate", "");
        editor.commit();
    }
    public static String getFileDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("FileDelete", Context.MODE_PRIVATE);
        String s = preferences.getString("FileDelete", "");
        return s;
    }
    public static void setFileDelete(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("FileDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FileDelete", s);
        editor.commit();
    }

    public static void setFileDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("FileDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FileDelete", "");
        editor.commit();
    }
    public static String getPhotoAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("PhotoAdd", Context.MODE_PRIVATE);
        String s = preferences.getString("PhotoAdd", "");
        return s;
    }
    public static void setPhotoAdd(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("PhotoAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PhotoAdd", s);
        editor.commit();
    }

    public static void setPhotoAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("PhotoAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PhotoAdd", "");
        editor.commit();
    }
    public static String getPollsAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("PollsAdd", Context.MODE_PRIVATE);
        String s = preferences.getString("PollsAdd", "");
        return s;
    }
    public static void setPollsAdd(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("PollsAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PollsAdd", s);
        editor.commit();
    }

    public static void setPollsAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("PollsAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PollsAdd", "");
        editor.commit();
    }
    public static String getPollsUpdate() {
        preferences = null;
        preferences = context.getSharedPreferences("PollsUpdate", Context.MODE_PRIVATE);
        String s = preferences.getString("PollsUpdate", "");
        return s;
    }
    public static void setPollsUpdate(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("PollsUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PollsUpdate", s);
        editor.commit();
    }

    public static void setPollsUpdate() {
        preferences = null;
        preferences = context.getSharedPreferences("PollsUpdate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PollsUpdate", "");
        editor.commit();
    }
    public static String getPollsDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("PollsDelete", Context.MODE_PRIVATE);
        String s = preferences.getString("PollsDelete", "");
        return s;
    }
    public static void setPollsDelete(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("PollsDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PollsDelete", s);
        editor.commit();
    }

    public static void setPollsDelete() {
        preferences = null;
        preferences = context.getSharedPreferences("PollsDelete", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PollsDelete", "");
        editor.commit();
    }
    public static String getPollsPublish() {
        preferences = null;
        preferences = context.getSharedPreferences("PollsPublish", Context.MODE_PRIVATE);
        String s = preferences.getString("PollsPublish", "");
        return s;
    }
    public static void setPollsPublish(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("PollsPublish", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PollsPublish", s);
        editor.commit();
    }

    public static void setPollsPublish() {
        preferences = null;
        preferences = context.getSharedPreferences("PollsPublish", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PollsPublish", "");
        editor.commit();
    }
    public static String getPollsVote() {
        preferences = null;
        preferences = context.getSharedPreferences("PollsVote", Context.MODE_PRIVATE);
        String s = preferences.getString("PollsVote", "");
        return s;
    }
    public static void setPollsVote(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("PollsVote", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PollsVote", s);
        editor.commit();
    }

    public static void setPollsVote() {
        preferences = null;
        preferences = context.getSharedPreferences("PollsVote", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PollsVote", "");
        editor.commit();
    }
    public static String getCalenderAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("CalenderAdd", Context.MODE_PRIVATE);
        String s = preferences.getString("CalenderAdd", "");
        return s;
    }
    public static void setCalenderAdd(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("CalenderAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("CalenderAdd", s);
        editor.commit();
    }

    public static void setCalenderAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("CalenderAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("CalenderAdd", "");
        editor.commit();
    }
    public static String getSettingsAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("SettingsAdd", Context.MODE_PRIVATE);
        String s = preferences.getString("SettingsAdd", "");
        return s;
    }
    public static void setSettingsAdd(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("SettingsAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SettingsAdd", s);
        editor.commit();
    }

    public static void setSettingsAdd() {
        preferences = null;
        preferences = context.getSharedPreferences("SettingsAdd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SettingsAdd", "");
        editor.commit();
    }
    //ManagerName  end

    //ManagerName  start
    public static String getManagerNamePreference() {
        preferences = null;
        preferences = context.getSharedPreferences("ManagerNamePreference", Context.MODE_PRIVATE);
        String s = preferences.getString("ManagerName", "");

        return s;
    }

    public static void setManagerNamePreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("ManagerNamePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ManagerName", s);
        editor.commit();
    }

    public static void setManagerNameBank() {
        preferences = null;
        preferences = context.getSharedPreferences("ManagerNamePreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ManagerName", "");
        editor.commit();
    }
    //ManagerName  end


    //manager id start
    public static String getManagerIdPreference() {
        preferences = null;
        preferences = context.getSharedPreferences("ManagerIdPreference", Context.MODE_PRIVATE);
        String s = preferences.getString("Managerid", "");

        return s;
    }

    public static void setManagerIdPreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("ManagerIdPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Managerid", s);
        editor.commit();
    }

    public static void setManagerIdBank() {
        preferences = null;
        preferences = context.getSharedPreferences("ManagerIdPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Managerid", "");
        editor.commit();
    }
    //manager id end



    //profile image start

    public static String getPeopleProfileImgPreference() {
        preferences = null;
        preferences = context.getSharedPreferences("IdPreference", Context.MODE_PRIVATE);
        String s = preferences.getString("UserProfile", "");

        return s;

    }

    public static void setPeopleProfileImgPreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("IdPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserProfile", s);
        editor.commit();
    }

    public static void setPeopleProfileImgBank() {
        preferences = null;
        preferences = context.getSharedPreferences("IdPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("UserProfile", "");
        editor.commit();
    }
    //profile image end




    //region PeopleId
    public static String getPeoplePasswordPreference() {
        preferences = null;
        preferences = context.getSharedPreferences("Password", Context.MODE_PRIVATE);
        String s = preferences.getString("Userpass", "");
        return s;
    }

    public static void setPeoplePasswordPreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("Password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Userpass", s);
        editor.commit();
    }

    public static void setPeoplePasswordBank() {
        preferences = null;
        preferences = context.getSharedPreferences("Password", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Userpass", "");
        editor.commit();
    }
    //endregion


    //region PeopleId
    public static String getPeopleIdPreference() {
        preferences = null;
        preferences = context.getSharedPreferences("IdPreference", Context.MODE_PRIVATE);
        String s = preferences.getString("Userid", "");

        return s;
    }

    public static void setPeopleIdPreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("IdPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Userid", s);
        editor.commit();
    }

    public static void setPeopleIdBank() {
        preferences = null;
        preferences = context.getSharedPreferences("IdPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Userid", "");
        editor.commit();
    }
    //endregion

    //region FullName
    public static String getFullNamePreference() {
        preferences = null;
        preferences = context.getSharedPreferences("FullName", Context.MODE_PRIVATE);
        String s = preferences.getString("fullName", "");

        return s;
    }

    public static void setFullNamePreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("FullName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fullName", s);
        editor.commit();
    }

    public static void setFullNameBank() {
        preferences = null;
        preferences = context.getSharedPreferences("FullName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fullName", "");
        editor.commit();
    }
    //endregion


    //HashKey
    public static String getHashKeyPreference() {
        preferences = null;
        preferences = context.getSharedPreferences("HashKey", Context.MODE_PRIVATE);
        String s = preferences.getString("hashKey", "8da0d88b6ad7f6f4dcff509be7cfd5cba192");

        return s;
    }

    public static void setHashKeyPreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("HashKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("hashKey", s);
        editor.commit();
    }

    public static void setHashKeyBank() {
        preferences = null;
        preferences = context.getSharedPreferences("HashKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("hashKey", "");
        editor.commit();
    }
    //end HashKey

    //region EmailAddress
    public static String getEmailAddressPreference() {
        preferences = null;
        preferences = context.getSharedPreferences("EmailAddress", Context.MODE_PRIVATE);
        String s = preferences.getString("emailAddress", "");

        return s;
    }

    public static void setEmailAddressPreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("EmailAddress", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("emailAddress", s);
        editor.commit();
    }

    public static void setEmailAddressBank() {
        preferences = null;
        preferences = context.getSharedPreferences("EmailAddress", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("emailAddress", "");
        editor.commit();
    }
    //endregion

    //region MobileNo
    public static String getMobileNoPreference() {
        preferences = null;
        preferences = context.getSharedPreferences("MobileNo", Context.MODE_PRIVATE);
        String s = preferences.getString("mobileNo", "");

        return s;
    }

    public static void setMobileNoPreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("MobileNo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mobileNo", s);
        editor.commit();
    }

    public static void setMobileNoBank() {
        preferences = null;
        preferences = context.getSharedPreferences("MobileNo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mobileNo", "");
        editor.commit();
    }
    //endregion

    //region DepartmentId
    public static String getDepartmentIdPreference() {
        preferences = null;
        preferences = context.getSharedPreferences("DepartmentId", Context.MODE_PRIVATE);
        String s = preferences.getString("departmentId", "");

        return s;
    }

    public static void setDepartmentIdPreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("DepartmentId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("departmentId", s);
        editor.commit();
    }

    public static void setDepartmentIdBank() {
        preferences = null;
        preferences = context.getSharedPreferences("DepartmentId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("departmentId", "");
        editor.commit();
    }
    //endregion

    //region BranchId
    public static String getBranchIdPreference() {
        preferences = null;
        preferences = context.getSharedPreferences("BranchId", Context.MODE_PRIVATE);
        String s = preferences.getString("branchId", "");

        return s;
    }

    public static void setBranchIdPreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("BranchId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("branchId", s);
        editor.commit();
    }

    public static void setBranchIdBank() {
        preferences = null;
        preferences = context.getSharedPreferences("BranchId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("branchId", "");
        editor.commit();
    }
    //endregion

    //region TokenId
    public static String getTokenIdPreference() {
        preferences = null;
        preferences = context.getSharedPreferences("TokenId", Context.MODE_PRIVATE);
        String s = preferences.getString("tokenId", "");

        return s;
    }

    public static void setTokenIdPreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("TokenId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tokenId", s);
        editor.commit();
    }

    public static void setTokenIdBank() {
        preferences = null;
        preferences = context.getSharedPreferences("TokenId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("tokenId", "");
        editor.commit();
    }
    //endregion

    //region RegionId
    public static String getRegionIdPreference() {
        preferences = null;
        preferences = context.getSharedPreferences("RegionId", Context.MODE_PRIVATE);
        String s = preferences.getString("regionId", "");

        return s;
    }



    public static void setRegionIdPreference(String s) {
        preferences = null;
        preferences = context.getSharedPreferences("RegionId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("regionId", s);
        editor.commit();
    }

    public static void setRegionIdBank() {
        preferences = null;
        preferences = context.getSharedPreferences("RegionId", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("regionId", "");
        editor.commit();
    }
    //endregion

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getIMeiNumaber() {
        telephonyManager = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
        IMEI_Number_Holder = telephonyManager.getDeviceId();
        return IMEI_Number_Holder;
    }

    public static void SetvollyTime30Sec(JsonObjectRequest request) {
        int socketTimeout = 30000;//30 seconds waiting time
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context act) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) act, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(act);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) act, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) act, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}