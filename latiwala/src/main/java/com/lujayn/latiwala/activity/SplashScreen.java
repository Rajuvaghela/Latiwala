package com.lujayn.latiwala.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.AppConstant.HTTPResponseCode;
import com.lujayn.latiwala.common.AppConstant.HttpRequestType;
import com.lujayn.latiwala.common.ApplicationData;

import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.common.SharedPrefManager;
import com.lujayn.latiwala.common.Webservice;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.database.DataWrite;
import com.lujayn.latiwala.network.RequestTask;
import com.lujayn.latiwala.network.RequestTaskDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import cn.refactor.lib.colordialog.PromptDialog;

import static com.lujayn.latiwala.Utility.checkConnectivity;


/**
 * Created by Shailesh on 25/07/16.
 */
public class SplashScreen extends AppCompatActivity implements RequestTaskDelegate {
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST = 0;
    SessionManager manager;
    DataWrite dataWrite;
    ImageView ivSplashLogo;
    String color_back;
    LinearLayout linearLayout;
    private ApplicationData appData;
    CountDownTimer lTimer;
    private ProgressDialog pdialog;
    ProgressBar progressBar;
    public static final String TOPIC_GLOBAL = "global";
    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        activity = this;
        setStatusBarTranslucent(true);
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
        initialize();
    }

    private void initialize() {
        appData = ApplicationData.getSharedInstance();
        ivSplashLogo = (ImageView) findViewById(R.id.ivSplashLogo);
        linearLayout = (LinearLayout) findViewById(R.id.llSplash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        dataWrite = new DataWrite(this);
        dataWrite.open();
        manager = new SessionManager();
        Webservice.BASE_URL = "http://latiwalaa.com/?webservice=1&vootouchservice=";

        dataWrite.clearDatabase();
        getPermissionToWriteStorage();
        if (checkConnectivity()) {
            sendTokenToServer();
        } else {
            showNoInternetPromptDlg();
        }

    }


    public void getPermissionToWriteStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST);
            }
        } else {
            if (checkConnectivity()) {
                settingOption("option");
            } else {
                showNoInternetPromptDlg();
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkConnectivity()) {
                    settingOption("option");
                } else {
                    showNoInternetPromptDlg();
                }
            } else {
                SplashScreen.this.finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void settingOption(String option) {
          progressBar.setVisibility(View.VISIBLE);
        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_SETTING_OPTION;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, option);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //registerReceiver(receive, new IntentFilter(Intent_Class_Data.NOTIFICATION));

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // unregisterReceiver(receive);

    }

    @Override
    public void backgroundActivityComp(String response, HttpRequestType completedRequestType) {
        HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("responseCode : " + responseCode);
        switch (responseCode) {
            case Country:
                Thread background = new Thread() {
                    public void run() {
                        try {

                            String status = manager.getPreferences(SplashScreen.this, AppConstant.STATUS);
                            //String status_login = manager.getPreferences(SplashScreen.this,AppConstant.STATUS_LOGIN);
                            AppDebugLog.println("status" + status);
                            AppDebugLog.println("Splash end time ------------------: " + System.currentTimeMillis() / 1000);
                            if (status.equals("1")) {
                                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                dataWrite.deletedData();
                                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                            progressBar.setVisibility(View.GONE);

                            //Remove activity
                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                        }


                    }

                };

                // start thread
                background.start();

                break;

            case Success:
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();

                break;

            case NetworkError:
                showUserAlert(this, getString(R.string.alert_title_message),
                        getString(R.string.alert_body_network_error), null);
                break;

            case ServerError:
                showUserAlert(this, getString(R.string.alert_title_message),
                        getString(R.string.alert_body_something_was_wrong), null);
                break;

            case NotiSuccess:

                break;
        }
    }

    public void showUserAlert(Context context, String title, String message, DialogInterface.OnClickListener listner) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(message)
                .setPositiveButton(context.getText(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        manager.setPreferences(SplashScreen.this, "finstall", "0");
                    }
                }).show();

    }

    @Override
    public void timeOut() {

    }

    @Override
    public void codeError(int code) {

    }

    @Override
    public void percentageDownloadCompleted(int percentage, Object record) {

    }

    private void sendTokenToServer() {
        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("type", "1");
            jsonObject.put("userid", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_NOTIFICATION;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, "noti", jsonObject.toString());
    }
    private void showNoInternetPromptDlg() {
        new PromptDialog(this)
                .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setAnimationEnable(true)
                .setTitleText(getString(R.string.no_internet_connection))
                .setContentText(getString(R.string.please_try_againg))
                .setPositiveListener(getString(R.string.try_again), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        initialize();
                    }
                }).show();
    }
}
