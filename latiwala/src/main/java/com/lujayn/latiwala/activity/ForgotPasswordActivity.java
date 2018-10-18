package com.lujayn.latiwala.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.lujayn.latiwala.R;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationContext;
import com.lujayn.latiwala.common.ApplicationData;

import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.common.Webservice;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.database.DataWrite;
import com.lujayn.latiwala.network.RequestTask;
import com.lujayn.latiwala.network.RequestTaskDelegate;

import org.json.JSONObject;

/**
 * Created by Shailesh on 09/05/16.
 */
public class ForgotPasswordActivity extends AppCompatActivity implements RequestTaskDelegate, View.OnClickListener {
    EditText et_email;
    TextView btn_submit;
    TextView tv_forgotTitle, tv_description;
    SessionManager manager;
    String color_back, color_font, key;

    DataWrite dataWrite;
    private ApplicationData appData;
    LinearLayout ll_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        manager = new SessionManager();
        appData = ApplicationData.getSharedInstance();
        SettingOption settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(ForgotPasswordActivity.this, AppConstant.SETTING_OPTION);
        color_back = settingOption.getData().getOptions().getCate_shape_color();
        color_font = settingOption.getData().getOptions().getCate_name_color();

        dataWrite = new DataWrite(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color_back));
        }

        et_email = (EditText) findViewById(R.id.etEmail_forgotpass);
        btn_submit = (TextView) findViewById(R.id.btnSend_forgotpass);

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);


        Intent i = getIntent();

        if (i.hasExtra("SHOP")) {
            key = i.getStringExtra("SHOP");
        }

        if (i.hasExtra("LOGIN")) {
            key = i.getStringExtra("LOGIN");
        }


        btn_submit.setBackgroundColor(Color.parseColor(color_back));
        btn_submit.setTextColor(Color.parseColor(color_font));


        btn_submit.setOnClickListener(onClickMethod);
    }

    private View.OnClickListener onClickMethod = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (et_email.getText().length() == 0) {
                et_email.setError(getString(R.string.please_enter_registered_email_ID));
            } else {
                webservice_call("forgotpassword");
            }


        }
    };

    private void webservice_call(String forgotpassword) {


        JSONObject object = new JSONObject();
        try {
            object.put("email", et_email.getText().toString());
        } catch (Exception e) {
            // TODO: handle exception
        }

        //  Log.d("shailesh", "email is" + et_email.getText().toString());
        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_FORGOT_PASSWORD;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, forgotpassword, object.toString());


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
    public void onBackPressed() {

        if (key.equals("shopping")) {
            Intent intent1 = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            intent1.putExtra("SHOP", AppConstant.key);
            startActivity(intent1);
            finish();

        } else {
            Intent intent1 = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            intent1.putExtra("LOGIN", AppConstant.key);
            startActivity(intent1);
            finish();
        }
    }

    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("Login responseCode : " + responseCode);

        switch (responseCode) {
            case Success:
                AlertDialog.Builder dialog = new AlertDialog.Builder(ForgotPasswordActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Success");
                dialog.setMessage("successfully user to sending email");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if (AppConstant.key.equals("shopping")) {

                            Intent intentLogin = new Intent(ApplicationContext.getAppContext(), LoginActivity.class);
                            intentLogin.putExtra("SHOP", AppConstant.key);
                            startActivity(intentLogin);
                            finish();

                        } else if (AppConstant.key.equals("LOGIN")) {

                            Intent intentLogin = new Intent(ApplicationContext.getAppContext(), LoginActivity.class);
                            intentLogin.putExtra("LOGIN", AppConstant.key);
                            startActivity(intentLogin);
                            finish();
                        }


                    }
                });
                final AlertDialog alert = dialog.create();
                alert.show();

                break;

            case ServerError:
                appData.showUserAlert(this, getString(R.string.alert_title_message),
                        getString(R.string.alert_body_server_error), null);
                break;


        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:

                if (key.equals("shopping")) {
                    Intent intent1 = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    intent1.putExtra("SHOP", AppConstant.key);
                    startActivity(intent1);
                    finish();

                } else {
                    Intent intent1 = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    intent1.putExtra("LOGIN", AppConstant.key);
                    startActivity(intent1);
                    finish();
                }
                break;
        }
    }
}
