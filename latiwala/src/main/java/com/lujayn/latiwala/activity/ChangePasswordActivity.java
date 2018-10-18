package com.lujayn.latiwala.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lujayn.latiwala.R;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationData;
import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.common.Webservice;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.database.DataWrite;
import com.lujayn.latiwala.network.RequestTask;
import com.lujayn.latiwala.network.RequestTaskDelegate;

import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class ChangePasswordActivity extends AppCompatActivity implements RequestTaskDelegate, View.OnClickListener {

    EditText et_crntPass, et_nwPass, et_cnfrmPass;
    String fname, lname, email, currentpass, newpass, cnfrmpass;
    DataWrite dataWrite;
    private ProgressDialog progressDialog;
    TextView btn_save;
    SessionManager manager;
    String userID;
    String color_back, color_font,color_statusbar;
    private ApplicationData appData;
    Activity activity;
    LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        activity = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        appData = ApplicationData.getSharedInstance();
        manager = new SessionManager();
        userID = manager.getPreferences(activity, AppConstant.USER_ID);
        SettingOption settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(activity, AppConstant.SETTING_OPTION);
        color_back = settingOption.getData().getOptions().getCate_shape_color();
        color_font = settingOption.getData().getOptions().getCate_name_color();
        color_statusbar = settingOption.getData().getOptions().getStatus_bar_color();

        dataWrite = new DataWrite(activity);
        dataWrite.open();


        et_crntPass = (EditText) findViewById(R.id.etCrntPass_accdetail);
        et_nwPass = (EditText) findViewById(R.id.etNewPass_accdetail);
        et_cnfrmPass = (EditText) findViewById(R.id.etCnfNewPass_accdetail);
        btn_save = (TextView) findViewById(R.id.btnSaveChange_accdetail);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        RelativeLayout rl_header = (RelativeLayout) findViewById(R.id.rl_header);

        rl_header.setBackgroundColor(Color.parseColor(color_back));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color_statusbar));
        }


        btn_save.setBackgroundColor(Color.parseColor(color_back));
        btn_save.setTextColor(Color.parseColor(color_font));
        btn_save.setOnClickListener(onClickMethod);
        ll_back.setOnClickListener(this);
    }

    private View.OnClickListener onClickMethod = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.btnSaveChange_accdetail:

                    if (et_crntPass.getText().length() == 0) {
                        et_crntPass.setError(getString(R.string.please_enter_current_password));

                    } else if (et_nwPass.getText().toString().length() < 12 && !ApplicationData.isValidPassword(et_nwPass.getText().toString())) {
                        et_nwPass.setError(getString(R.string.validationpass));

                    } else if (et_cnfrmPass.getText().length() == 0) {
                        et_cnfrmPass.setError(getString(R.string.please_re_enter_password));
                    } else if (!et_cnfrmPass.getText().toString().equals(et_nwPass.getText().toString())) {
                        et_cnfrmPass.setError(getString(R.string.password_not_match));
                    } else {
                        changepassword("password");
                    }

                    break;
            }
        }
    };

    private void changepassword(String password) {
        progressDialog.show();

        currentpass = et_crntPass.getText().toString();
        newpass = et_nwPass.getText().toString();
        cnfrmpass = et_cnfrmPass.getText().toString();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.putOpt("password", currentpass);
            jsonObject.putOpt("new_password", newpass);
            jsonObject.putOpt("user_id", userID);
        } catch (Exception e) {

        }

        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_CHANGE_PASSWORD;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, "Changepassword", jsonObject.toString());

    }

    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("Password responseCode : " + responseCode);

        switch (responseCode) {
            case Success:
                finish();
                progressDialog.dismiss();
                break;

            case ServerError:
                progressDialog.dismiss();
                appData.showUserAlert(activity, getString(R.string.alert_title_message),
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
                finish();
                break;
        }
    }
}
