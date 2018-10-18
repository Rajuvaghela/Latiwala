package com.lujayn.latiwala.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lujayn.latiwala.R;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.database.DataWrite;

/**
 * Created by Shailesh on 22/08/16.
 */
public class ConfirmationActivity extends AppCompatActivity {
    TextView tv_email, tv_instruction, tv_thankyou;
    LinearLayout ll_banklist, ll_banklable;
    DataWrite dataWrite;
    TextView btn_finish;
    SessionManager manager;
    String color_finish, color_finish_text, color_statusbar;
    LayoutInflater inflater;

    //String email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        try {
            btn_finish = (TextView) findViewById(R.id.btnFinish);
            //tv_email = (TextView)findViewById(R.id.tvUseremail);
            tv_instruction = (TextView) findViewById(R.id.tvinstruction_comfirm);
            tv_thankyou = (TextView) findViewById(R.id.tvThankymsg);
            ll_banklist = (LinearLayout) findViewById(R.id.llBanklist);
            ll_banklable = (LinearLayout) findViewById(R.id.llbanklistLable);


            manager = new SessionManager();
            //email = manager.getPreferences(ConfirmationActivity.this,AppConstant.USER_EMAIL);
            manager.setPreferences(ConfirmationActivity.this, AppConstant.BILL_AS_SHIP, "0");
            SettingOption settingOption = new SettingOption();
            settingOption = manager.getPreferencesOption(ConfirmationActivity.this, AppConstant.SETTING_OPTION);
            color_statusbar = settingOption.getData().getOptions().getStatus_bar_color();
            color_finish_text = settingOption.getData().getOptions().getCate_name_color();
            color_finish = settingOption.getData().getOptions().getCate_shape_color();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor(color_statusbar));
            }
            dataWrite = new DataWrite(this);
            dataWrite.open();
            dataWrite.deletedCartData();

            //tv_email.setText(email);
            AppConstant.counter = "0";

            inflater = (LayoutInflater) getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            inflater = LayoutInflater.from(this);


            AppDebugLog.println("payment method name = " + AppConstant.payment_gateways.getTitle());

            tv_instruction.setText(AppConstant.payment_gateways.getInstructions());

            getBanklist();

            btn_finish.setBackgroundColor(Color.parseColor(color_finish));
            btn_finish.setTextColor(Color.parseColor(color_finish_text));
            btn_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ConfirmationActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //change
                    startActivity(i);
                    finish();
                }
            });
        } catch (Exception e) {
            Log.e("ConfirmationActivity", ":Exception++++++++++++++++++1");
            e.printStackTrace();
        }

    }

    private void getBanklist() {

        try {
            TextView accName, bankName, accNumber, sortCode, iban, bic;
            if (AppConstant.payment_gateways.getAccount_details().size() != 0) {
                ll_banklable.setVisibility(View.VISIBLE);
                for (int i = 0; i < AppConstant.payment_gateways.getAccount_details().size(); i++) {
                    final View view = inflater.inflate(R.layout.inflater_banklist, null);
                    accName = (TextView) view.findViewById(R.id.tvAccName);
                    bankName = (TextView) view.findViewById(R.id.tvBankName);
                    accNumber = (TextView) view.findViewById(R.id.tvAccNumber);
                    sortCode = (TextView) view.findViewById(R.id.tvSortCode);
                    iban = (TextView) view.findViewById(R.id.tvIBAN);
                    bic = (TextView) view.findViewById(R.id.tvBIC);

                    accName.setText(AppConstant.payment_gateways.getAccount_details().get(i).getAccount_name());
                    bankName.setText(AppConstant.payment_gateways.getAccount_details().get(i).getBank_name());
                    accNumber.setText(AppConstant.payment_gateways.getAccount_details().get(i).getAccount_number());
                    sortCode.setText(AppConstant.payment_gateways.getAccount_details().get(i).getSort_code());
                    iban.setText(AppConstant.payment_gateways.getAccount_details().get(i).getIban());
                    bic.setText(AppConstant.payment_gateways.getAccount_details().get(i).getBic());

                    ll_banklist.addView(view);

                }
            } else {
                ll_banklable.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            Log.e("ConfirmationActivity", ":Exception++++++++++++++++++2");
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {

    }

}
