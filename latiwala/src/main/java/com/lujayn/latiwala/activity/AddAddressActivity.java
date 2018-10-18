package com.lujayn.latiwala.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lujayn.latiwala.R;
import com.lujayn.latiwala.bean.BeanCountry;
import com.lujayn.latiwala.bean.BeanState;
import com.lujayn.latiwala.bean.BeanUserData;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationData;
import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.common.Webservice;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.database.DataWrite;
import com.lujayn.latiwala.network.RequestTask;
import com.lujayn.latiwala.network.RequestTaskDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;
import dmax.dialog.SpotsDialog;

public class AddAddressActivity extends AppCompatActivity implements RequestTaskDelegate, View.OnClickListener {


    EditText et_fname, et_lname, et_address, et_city, et_postcode, et_email, et_phone, et_company;
    String user_id, fname, lname, address, country, state, city, postcode, email, phone, company, ship_state_code, ship_country_code;
    TextView btn_save;
    ArrayList<BeanCountry> countriesList;
    ArrayList<BeanState> statelist;
    String country_code, tag_address, state_code, bill_country_code, bill_state_code;
    SessionManager manager;
    String color_btn, color_btn_text,color_statusbar;
    ArrayList<BeanUserData> userDataList;
    ArrayList<BeanUserData> userShippinglist;
    ArrayList<BeanUserData> userBillinglist;
    BeanUserData beanUserdata;
    DataWrite dataWrite;
    private ProgressDialog progressDialog;
    private ApplicationData appData;
    Activity activity;
    TextView et_country, et_state;
    LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        activity = this;
        tag_address = getIntent().getExtras().getString("tag");

        manager = new SessionManager();
        appData = ApplicationData.getSharedInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        SettingOption settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(activity, AppConstant.SETTING_OPTION);
        color_btn = settingOption.getData().getOptions().getCate_shape_color();
        color_btn_text = settingOption.getData().getOptions().getCate_name_color();
        color_statusbar = settingOption.getData().getOptions().getStatus_bar_color();

        dataWrite = new DataWrite(activity);
        dataWrite.open();

        btn_save = (TextView) findViewById(R.id.btnSave);
        // userDataList = new ArrayList<BeanUserData>();
        beanUserdata = new BeanUserData();
        user_id = manager.getPreferences(activity, AppConstant.USER_ID);
        // userDataList= manager.getPreferencesArraylist(activity,AppConstant.USER_DATA);

        TextView tv_header = (TextView) findViewById(R.id.tv_header);
        tv_header.setText(getString(R.string.title_edit_address));
        et_fname = (EditText) findViewById(R.id.etFirstName);
        et_lname = (EditText) findViewById(R.id.etLastName);
        et_address = (EditText) findViewById(R.id.etAddress);
        et_country = (TextView) findViewById(R.id.etCountry);
        et_company = (EditText) findViewById(R.id.etCompany);
        et_state = (TextView) findViewById(R.id.etState);
        et_city = (EditText) findViewById(R.id.etCity);
        et_postcode = (EditText) findViewById(R.id.etPostcode);
        et_email = (EditText) findViewById(R.id.etEmail);
        et_phone = (EditText) findViewById(R.id.etPhone);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);

        btn_save.setBackgroundColor(Color.parseColor(color_btn));
        btn_save.setTextColor(Color.parseColor(color_btn_text));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color_statusbar));
        }

        RelativeLayout rl_header = (RelativeLayout) findViewById(R.id.rl_header);
        rl_header.setBackgroundColor(Color.parseColor(color_btn));

        et_country.setOnClickListener(onClickMethod);
        et_state.setOnClickListener(onClickMethod);
        btn_save.setOnClickListener(onClickMethod);
        ll_back.setOnClickListener(this);


        if (tag_address.equals("billing")) {

            userBillinglist = dataWrite.FetchBillAddress();

            et_fname.setText(userBillinglist.get(0).getBilling_first_name());
            et_lname.setText(userBillinglist.get(0).getBilling_last_name());
            et_address.setText(userBillinglist.get(0).getBilling_address_1());
            et_company.setText(userBillinglist.get(0).getBilling_company());
            et_country.setText(userBillinglist.get(0).getBilling_country());
            et_state.setText(userBillinglist.get(0).getBilling_state());
            et_city.setText(userBillinglist.get(0).getBilling_city());
            et_postcode.setText(userBillinglist.get(0).getBilling_postcode());
            et_email.setText(userBillinglist.get(0).getBilling_email());
            et_phone.setText(userBillinglist.get(0).getBilling_phone());
            bill_country_code = userBillinglist.get(0).getBilling_country_code();
            bill_state_code = userBillinglist.get(0).getBilling_state_code();

            AppDebugLog.println("billing state===" + userBillinglist.get(0).getBilling_state_code());


            if (bill_country_code.length() == 0) {

            } else {
                getState(bill_country_code);
            }

        } else if (tag_address.equals("shipping")) {

            userShippinglist = dataWrite.FetchShippingAddress();

            et_fname.setText(userShippinglist.get(0).getShipping_first_name());
            et_lname.setText(userShippinglist.get(0).getShipping_last_name());
            et_address.setText(userShippinglist.get(0).getShipping_address_1());
            et_country.setText(userShippinglist.get(0).getShipping_country());
            et_company.setText(userShippinglist.get(0).getShipping_company());
            et_state.setText(userShippinglist.get(0).getShipping_state());
            et_city.setText(userShippinglist.get(0).getShipping_city());
            et_postcode.setText(userShippinglist.get(0).getShipping_postcode());
            et_email.setVisibility(View.GONE);
            et_phone.setVisibility(View.GONE);
            country_code = userShippinglist.get(0).getShipping_country_code();
            state_code = userShippinglist.get(0).getShipping_state_code();


            if (country_code.length() == 0) {

            } else {
                getState(country_code);
            }

        }
    }

    public View.OnClickListener onClickMethod = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.etCountry:
                    getCountry("country");
                    break;
                case R.id.etState:
                    if (et_country.getText().length() == 0) {

                        dataWrite.showToast(getString(R.string.please_select_country));
                    } else {
                        statedisplay();
                    }
                    break;

                case R.id.btnSave:
                    if (tag_address.equals("billing")) {
                        if (et_fname.getText().length() == 0) {
                            et_fname.setError(getString(R.string.please_enter_your_first_name));

                        } else if (et_lname.getText().length() == 0) {
                            et_lname.setError(getString(R.string.please_enter_your_last_name));

                        } else if (et_address.getText().length() == 0) {
                            et_address.setError(getString(R.string.please_enter_your_address));

                        } else if (et_country.getText().length() == 0) {
                            et_country.setError(getString(R.string.please_enter_your_country));

                        } else if (et_state.getText().length() == 0) {
                            et_state.setError(getString(R.string.please_enter_your_state));

                        } else if (et_city.getText().length() == 0) {
                            et_city.setError(getString(R.string.please_enter_your_city));
                        } else if (et_postcode.getText().length() == 0) {

                            et_postcode.setError(getString(R.string.please_enter_your_postcode));
                        } else if (et_email.getText().length() == 0) {

                            et_email.setError(getString(R.string.please_enter_your_email));
                        } else if (et_phone.getText().length() == 0) {

                            et_phone.setError(getString(R.string.please_enter_your_phone));
                        } else {

                            saveBillingAddress();

                        }

                    } else if (tag_address.equals("shipping")) {

                        if (et_fname.getText().length() == 0) {
                            et_fname.setError(getString(R.string.please_enter_your_first_name));

                        } else if (et_lname.getText().length() == 0) {
                            et_lname.setError(getString(R.string.please_enter_your_last_name));

                        } else if (et_address.getText().length() == 0) {
                            et_address.setError(getString(R.string.please_enter_your_address));

                        } else if (et_country.getText().length() == 0) {
                            et_country.setError(getString(R.string.please_enter_your_country));

                        } else if (et_state.getText().length() == 0) {
                            et_state.setError(getString(R.string.please_enter_your_state));

                        } else if (et_city.getText().length() == 0) {
                            et_city.setError(getString(R.string.please_enter_your_city));
                        } else if (et_postcode.getText().length() == 0) {

                            et_postcode.setError(getString(R.string.please_enter_your_postcode));
                        } else {

                            saveShippingAddress();
                        }
                    }
                    break;

            }

        }
    };

    private void saveShippingAddress() {
        userDataList = new ArrayList<BeanUserData>();
        fname = et_fname.getText().toString();
        lname = et_lname.getText().toString();
        address = et_address.getText().toString();
        company = et_company.getText().toString();
        city = et_city.getText().toString();
        state = et_state.getText().toString();
        country = et_country.getText().toString();
        postcode = et_postcode.getText().toString();
        dataWrite.insertShipping(user_id, fname, lname, address, country, state, city, postcode, country_code, state_code, company);
        startActivity(new Intent(activity, EditProfileActivity.class));
        finish();
    }

    private void saveBillingAddress() {
        userDataList = new ArrayList<BeanUserData>();

        fname = et_fname.getText().toString();
        lname = et_lname.getText().toString();
        address = et_address.getText().toString();
        company = et_company.getText().toString();
        city = et_city.getText().toString();
        state = et_state.getText().toString();
        country = et_country.getText().toString();
        email = et_email.getText().toString();
        phone = et_phone.getText().toString();
        postcode = et_postcode.getText().toString();


        dataWrite.insertBilling(user_id, fname, lname, address, country, state, city, postcode, email, phone, bill_country_code, bill_state_code, company);
        startActivity(new Intent(activity, EditProfileActivity.class));
        finish();

    }


    private void getState(String country_code) {
        progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            Log.d("shailesh", "country code===" + country_code);
            AppConstant.COUNTRY_CODE = country_code;
            jsonObject.put("country_code", country_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_STATE;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, "state", jsonObject.toString());

    }

    private void getCountry(String country) {

        progressDialog.show();

        countriesList = dataWrite.fetchCountry();

        if (countriesList.size() > 0) {
            countrydata();
        } else {

            String requestURL = Webservice.BASE_URL + "" + Webservice.URL_COUNTRY;
            AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
            RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
            requestTask.delegate = this;
            requestTask.execute(requestURL, country);
        }

    }


    private void statedisplay() {
        final List<String> list = new ArrayList<String>();
        statelist = dataWrite.fetchStates(AppConstant.COUNTRY_CODE);
        for (int i = 0; i < statelist.size(); i++) {
           /* Log.d("shailesh",
                    "state names =====" + statelist.get(i).getState_name());*/
            list.add(statelist.get(i).getState_name());

        }


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.country_city_listview, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("List");
        ListView lv = (ListView) convertView.findViewById(R.id.lv_countrycity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);


        final AlertDialog alert = alertDialog.create();
        alert.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = list.get(position).toString();

                AppDebugLog.println("selected item text ==== " + s);
                et_state.setText(s);
                state_code = statelist.get(position).getState_code();
                bill_state_code = statelist.get(position).getState_code();
                state = statelist.get(position).getState_name();
                alert.dismiss();

            }
        });

    }

    private void countrydata() {
        progressDialog.dismiss();
        countriesList = dataWrite.fetchCountry();
        final List<String> list = new ArrayList<String>();
        for (int i = 0; i < countriesList.size(); i++) {
           /* Log.d("shailesh",
                    "state names =====" + countriesList.get(i).getCountry_name());*/
            list.add(countriesList.get(i).getCountry_name());

        }


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.country_city_listview, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("List");
        ListView lv = (ListView) convertView.findViewById(R.id.lv_countrycity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);


        final AlertDialog alert = alertDialog.create();
        alert.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = list.get(position).toString();

                AppDebugLog.println("selected item text ==== " + s);
                et_country.setText(s);
                country_code = countriesList.get(position).getCountry_code();
                bill_country_code = countriesList.get(position).getCountry_code();
                country = countriesList.get(position).getCountry_name();
                et_state.setText("");
                getState(country_code);
                alert.dismiss();

            }
        });
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //activity.registerReceiver(receive, new IntentFilter(Intent_Class_Data.NOTIFICATION));

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // activity.unregisterReceiver(receive);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(activity, EditProfileActivity.class));
        finish();
    }

    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("product detail responseCode : " + responseCode);

        switch (responseCode) {
            case Country:
                // progressDialog.dismiss();
                countrydata();

                break;

            case State:
                progressDialog.dismiss();
                break;

            case ServerError:
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
                startActivity(new Intent(activity, EditProfileActivity.class));
                finish();
                break;
        }
    }

}
