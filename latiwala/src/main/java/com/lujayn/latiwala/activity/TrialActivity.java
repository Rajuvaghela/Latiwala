package com.lujayn.latiwala.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lujayn.latiwala.R;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationData;

import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.database.DataWrite;
import com.lujayn.latiwala.preferences.SharePreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

/**
 * Created by Shailesh on 21/02/17.
 */

public class TrialActivity extends AppCompatActivity {
    SessionManager manager;
    EditText et_trial_url;
    TextView btn_trial;
    SettingOption settingOption;
    private AlertDialog progressDialog;
    private ApplicationData appData;
    DataWrite dataWrite;
    SharePreferences sharePreferences;
    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial_screen);
        activity = this;
        sharePreferences = new SharePreferences();
        progressDialog = new SpotsDialog(activity, R.style.Custom);
        manager = new SessionManager();
        dataWrite = new DataWrite(this);
        appData = ApplicationData.getSharedInstance();
        et_trial_url = (EditText) findViewById(R.id.etUrlTrial);

        // et_trial_url.setText("http://");
        String ftrial = manager.getPreferences(activity, AppConstant.FInstall);

        if (ftrial.equals("1")) {
            et_trial_url.setText(sharePreferences.getFirstHalfTrialURL());
        }

        btn_trial = (TextView) findViewById(R.id.btnTrial);

        btn_trial.setOnClickListener(onClickMethod);
    }

    private View.OnClickListener onClickMethod = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.btnTrial:
                    if (et_trial_url.getText().length() == 0) {
                        et_trial_url.setError(getString(R.string.please_enter_your_rul));
                    } else {
                        String url = et_trial_url.getText().toString();

                        String last = url.substring(url.length() - 1);

                        // Log.e("LASTSTRING","last charector== "+ url.substring(url.length() - 1));

                        AppDebugLog.println("last charector== " + url.substring(url.length() - 1));

                        if (last.equals("/")) {
                            // http://192.168.100.105/woo_store/?webservice=1&vootouchservice=get_setting_option
                            String BASE_URL = url.trim() + "?webservice=1&vootouchservice=";

                            manager.setPreferences(activity, AppConstant.TRIAL_URL, BASE_URL);
                            if (isValidUrl(BASE_URL)) {

                                sharePreferences.setFirstHalfTrialURL(url);
                                manager.setPreferences(activity, "finstall", "1");
                                Intent i = new Intent(activity, SplashScreen.class);
                                startActivity(i);
                                finish();

                            } else {
                                dataWrite.showToast(getString(R.string.invalid_url));

                            }

                        } else {
                            String URL = url.trim() + "/?webservice=1&vootouchservice=";
                            manager.setPreferences(activity, AppConstant.TRIAL_URL, URL);
                            if (isValidUrl(URL)) {

                                manager.setPreferences(activity, "finstall", "1");
                                Intent i = new Intent(activity, SplashScreen.class);
                                startActivity(i);
                                finish();
                            } else {
                                dataWrite.showToast(getString(R.string.invalid_url));
                            }
                        }

                    }
                    break;
            }
        }
    };

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        if (m.matches())
            return true;
        else
            return false;
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

}
