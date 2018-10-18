package com.lujayn.latiwala.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.lujayn.latiwala.R;
import com.lujayn.latiwala.bean.BeanCountry;
import com.lujayn.latiwala.bean.BeanState;
import com.lujayn.latiwala.bean.BeanUserData;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by Shailesh on 09/05/16.
 */
public class CreateAccountActivity extends AppCompatActivity implements RequestTaskDelegate {

    EditText et_fname, et_lname, et_address, et_country, et_state, et_city, et_postcode, et_email,
            et_phone, et_password, et_company, et_username;
    String fname, lname, address, country, state, city, postcode, email, phone, company, password, username;
    String userName, user_id, user_fname, user_lname, user_email, bill_address, bill_city, bill_state, bill_country, bill_email, bill_fname, bill_lname,
            bill_phone, bill_postcode, bill_company, ship_address, ship_city, ship_company, ship_state, ship_country,
            ship_fname, ship_lname, ship_postcode, ship_country_code, bill_state_code, bill_country_code, ship_state_code;
    String userID, userEmail, user_name;
    TextView btn_create;
    ProgressDialog progressDialog;
    ArrayList<BeanCountry> countriesList;
    ArrayList<BeanState> statelist;
    String country_code, key, state_code;
    SessionManager manager;
    ArrayList<BeanUserData> userDataList;
    BeanUserData beanUserdata;
    DataWrite dataWrite;
    String color_tool_back, color_tool_title, color_tool_icon, color_statusbar, color_btn, color_btn_text;
    RelativeLayout rl_tool;
    private ApplicationData appData;
    TextView tvTitle;
    String status;
    Spinner spiner_country, spiner_state;
    LinearLayout ll_back;
    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        activity = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        manager = new SessionManager();
        appData = ApplicationData.getSharedInstance();
        SettingOption settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(CreateAccountActivity.this, AppConstant.SETTING_OPTION);
        color_tool_back = settingOption.getData().getOptions().getToolbar_back_color();
        color_tool_title = settingOption.getData().getOptions().getToolbar_title_color();
        color_tool_icon = settingOption.getData().getOptions().getToolbar_icon_color();
        color_statusbar = settingOption.getData().getOptions().getStatus_bar_color();
        color_btn = settingOption.getData().getOptions().getCate_shape_color();
        color_btn_text = settingOption.getData().getOptions().getCate_name_color();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color_statusbar));
        }

        dataWrite = new DataWrite(this);
        dataWrite.open();
        beanUserdata = new BeanUserData();

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        et_fname = (EditText) findViewById(R.id.etFirstName_create);
        et_lname = (EditText) findViewById(R.id.etLastName_create);
        et_address = (EditText) findViewById(R.id.etAddress_create);
        et_company = (EditText) findViewById(R.id.etCompany_create);
        et_country = (EditText) findViewById(R.id.etCountry_create);
        et_state = (EditText) findViewById(R.id.etState_create);
        et_city = (EditText) findViewById(R.id.etCity_create);
        et_postcode = (EditText) findViewById(R.id.etPostcode_create);
        et_email = (EditText) findViewById(R.id.etEmail_create);
        et_phone = (EditText) findViewById(R.id.etPhone_create);
        et_password = (EditText) findViewById(R.id.etPasswordAccount_create);
        et_username = (EditText) findViewById(R.id.etUsername_create);
        rl_tool = (RelativeLayout) findViewById(R.id.tool);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        spiner_country = (Spinner) findViewById(R.id.spiner_country);
        spiner_state = (Spinner) findViewById(R.id.spiner_state);

        btn_create = (TextView) findViewById(R.id.btnCreate);
        btn_create.setBackgroundColor(Color.parseColor(color_btn));
        btn_create.setTextColor(Color.parseColor(color_btn_text));
        rl_tool.setBackgroundColor(Color.parseColor(color_tool_back));
        RelativeLayout rl_header = (RelativeLayout) findViewById(R.id.rl_header);
        rl_header.setBackgroundColor(Color.parseColor(color_btn));

        tvTitle.setText(R.string.register);
        tvTitle.setTextColor(Color.parseColor(color_tool_title));

        Drawable myIcon5 = getResources().getDrawable(R.drawable.ic_arrow_back);
        //ColorFilter filter5 = new LightingColorFilter( Color.TRANSPARENT, Color.TRANSPARENT);
        myIcon5.setColorFilter(Color.parseColor(color_tool_icon), PorterDuff.Mode.SRC_ATOP);

        Intent i = getIntent();

        if (i.hasExtra("SHOP")) {
            key = i.getStringExtra("SHOP");
        }

        if (i.hasExtra("LOGIN")) {
            key = i.getStringExtra("LOGIN");
        }

        if (i.hasExtra("GUEST")) {
            key = i.getStringExtra("GUEST");

            et_username.setVisibility(View.GONE);
            et_password.setVisibility(View.GONE);
            btn_create.setText(R.string.continue1);
        }


        et_country.setOnClickListener(onClickMethod);
        et_state.setOnClickListener(onClickMethod);
        btn_create.setOnClickListener(onClickMethod);
        ll_back.setOnClickListener(onClickMethod);
        getCountry("country");
    }

    private View.OnClickListener onClickMethod = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.etCountry_create:
                    //getCountry("country");
                    getCountry();
                    break;
                case R.id.etState_create:
                    //getState("state");

                    break;

                case R.id.btnCreate:

                    if (key.equals("guest")) {

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
                            saveGuestBilling("guest_bill");
                        }

                    } else {


                        if (et_fname.getText().length() == 0) {
                            et_fname.setError(getString(R.string.please_enter_your_first_name));

                        } else if (et_lname.getText().length() == 0) {
                            et_lname.setError(getString(R.string.please_enter_your_last_name));

                        } else if (et_username.getText().length() == 0) {
                            et_username.setError(getString(R.string.please_enter_your_username));

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
                        } else if (et_password.getText().toString().length() < 12 && !ApplicationData.isValidPassword(et_password.getText().toString())) {

                            et_password.setError(getString(R.string.validationpass));

                        } else {

                            register_webservice_call("register");
                        }
                    }

                    break;

                case R.id.ll_back:

                    if (key.equals("guest")) {

                        Intent in = new Intent(CreateAccountActivity.this, LoginActivity.class);
                        in.putExtra("SHOP", "shopping");
                        startActivity(in);
                        finish();
                    } else if (key.equals("shopping")) {
                        Intent in = new Intent(CreateAccountActivity.this, LoginActivity.class);
                        in.putExtra("SHOP", "shopping");
                        startActivity(in);
                        finish();
                    } else {
                        Intent in = new Intent(CreateAccountActivity.this, LoginActivity.class);
                        in.putExtra("LOGIN", key);
                        startActivity(in);
                        finish();
                    }


                    break;
            }
        }
    };

    private void saveGuestBilling(String guest_bill) {


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
        user_id = "0.5";
        // manager.setPreferences(activity,AppConstant.user_pass,et_password.getText().toString());

        dataWrite.deletedData();

        dataWrite.insertBilling(user_id, fname, lname, address, country, state, city, postcode, email, phone, country_code, state_code, company);
        dataWrite.insertShipping(user_id, "", "", "", "", "", "", "", "", "", "");

        Intent i = new Intent(CreateAccountActivity.this, CheckOutActivity.class);
        i.putExtra("GUEST", key);
        startActivity(i);
        finish();

    }

    private void statedisplay() {
        final List<String> list = new ArrayList<String>();

        statelist = dataWrite.fetchStates(AppConstant.COUNTRY_CODE);
        for (int i = 0; i < statelist.size(); i++) {
           /* Log.d("shailesh",
                    "state names =====" + statelist.get(i).getState_name());*/
            list.add(statelist.get(i).getState_name());

        }
        ArrayAdapter aa = new ArrayAdapter(CreateAccountActivity.this, R.layout.spinner_layout, list);
        //Setting the ArrayAdapter data on the Spinner
        spiner_state.setAdapter(aa);
        spiner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = list.get(position).toString();
                AppDebugLog.println("selected item text ==== " + s);
                et_state.setText(s);
                state_code = statelist.get(position).getState_code();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

/*
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateAccountActivity.this);
        LayoutInflater inflater = CreateAccountActivity.this.getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.country_city_listview, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("List");
        ListView lv = (ListView) convertView.findViewById(R.id.lv_countrycity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateAccountActivity.this, android.R.layout.simple_list_item_1, list);
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
                alert.dismiss();

            }
        });*/
    }

    private void getCountry() {
        progressDialog.show();
        countriesList = dataWrite.fetchCountry();

        if (countriesList.size() > 0) {
            countrydata();
        } else {

            getCountry("country");

        }

    }


    private void getCountry(String country) {
        progressDialog.show();
        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_COUNTRY;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, country);
    }

    private void register_webservice_call(String register) {
        progressDialog.show();

        fname = et_fname.getText().toString();
        lname = et_lname.getText().toString();
        address = et_address.getText().toString();
        country = et_country.getText().toString();
        state = et_state.getText().toString();
        city = et_city.getText().toString();
        postcode = et_postcode.getText().toString();
        email = et_email.getText().toString();
        phone = et_phone.getText().toString();
        company = et_company.getText().toString();
        password = et_password.getText().toString();
        username = et_username.getText().toString();


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("google", "no");
            jsonObject.putOpt("billing_first_name", fname);
            jsonObject.putOpt("billing_last_name", lname);
            jsonObject.putOpt("billing_address_1", address);
            jsonObject.putOpt("billing_company", company);
            jsonObject.putOpt("billing_city", city);
            jsonObject.putOpt("billing_state", state_code);
            jsonObject.putOpt("billing_country", country_code);
            jsonObject.putOpt("billing_email", email);
            jsonObject.putOpt("billing_postcode", postcode);
            jsonObject.putOpt("billing_phone", phone);
            jsonObject.putOpt("password", password);
            jsonObject.put("user_name", username);

            jsonObject.putOpt("shipping_first_name", fname);
            jsonObject.putOpt("shipping_last_name", lname);
            jsonObject.putOpt("shipping_address_1", address);
            jsonObject.putOpt("shipping_company", company);
            jsonObject.putOpt("shipping_city", city);
            jsonObject.putOpt("shipping_state", state_code);
            jsonObject.putOpt("shipping_country", country_code);
            jsonObject.putOpt("shipping_postcode", postcode);
            // jsonObject.putOpt("shipping_phone",phone);


        } catch (Exception e) {
            // TODO: handle exception
        }

        //   Log.d("shailesh", "data  is==="+ object.toString());
        if (key.equals("shopping")) {
            Log.d("shailesh", "data  is===" + key);
            String requestURL = Webservice.BASE_URL + "" + Webservice.URL_REGISTER;
            AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
            RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
            requestTask.delegate = this;
            requestTask.execute(requestURL, register, jsonObject.toString());

        } else {

            //String requestURL = "https://www.luxuriouslathers.com/?webservice=1&wootouchservice=get_createuser";
            String requestURL = Webservice.BASE_URL + "" + Webservice.URL_REGISTER;
            AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
            RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
            requestTask.delegate = this;
            requestTask.execute(requestURL, "register1", jsonObject.toString());

        }

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

        ArrayAdapter aa = new ArrayAdapter(CreateAccountActivity.this, R.layout.spinner_layout, list);
        //Setting the ArrayAdapter data on the Spinner
        spiner_country.setAdapter(aa);
        spiner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = list.get(position).toString();
                AppDebugLog.println("selected item text ==== " + s);
                et_country.setText(s);
                country_code = countriesList.get(position).getCountry_code();
                et_state.setText("");
                getState(country_code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

/*
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateAccountActivity.this);
        LayoutInflater inflater = CreateAccountActivity.this.getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.country_city_listview, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("List");
        ListView lv = (ListView) convertView.findViewById(R.id.lv_countrycity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateAccountActivity.this, android.R.layout.simple_list_item_1, list);
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
                et_state.setText("");
                getState(country_code);
                alert.dismiss();

            }
        });*/
    }

    private void getState(String country_code) {

        JSONObject jsonObject = new JSONObject();
        try {

            AppDebugLog.println("country code===" + country_code);
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
        if (key.equals("guest")) {

            Intent in = new Intent(CreateAccountActivity.this, LoginActivity.class);
            startActivity(in);
            finish();
        } else if (key.equals("shopping")) {
            Intent in = new Intent(CreateAccountActivity.this, LoginActivity.class);
            in.putExtra("SHOP", "shoping");
            startActivity(in);
            finish();
        } else {
            Intent in = new Intent(CreateAccountActivity.this, LoginActivity.class);
            in.putExtra("LOGIN", key);
            startActivity(in);
            finish();
        }

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
                statedisplay();
                progressDialog.dismiss();
                break;

            case Success:

                progressDialog.dismiss();
                AppDebugLog.println("Response in Register : " + response);
                ApplicationData appdata = ApplicationData.getSharedInstance();

                DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
                dataWrite.open();
                SettingOption settingOption;
                SessionManager manager = new SessionManager();
                JSONObject object;
                ArrayList<BeanUserData> userDataList = new ArrayList<BeanUserData>();
                String userName, user_id, user_fname, user_lname, user_email, bill_address, bill_city, bill_state, bill_country, bill_email, bill_fname, bill_lname,
                        bill_phone, bill_postcode, bill_company, ship_address, ship_city, ship_company, ship_state, ship_country,
                        ship_fname, ship_lname, ship_postcode, ship_country_code, bill_state_code, bill_country_code, ship_state_code;
                String userID, userEmail, user_name;
                try {
                    object = new JSONObject(response);
                    JSONObject jsonObject = object.optJSONObject("data");
                    if (object.getInt("success") == 0) {
                        appdata.setResponseCode(AppConstant.HTTPResponseCode.Success0);
                        dataWrite.showToast(jsonObject.getString("registererror"));

                    } else if (object.getInt("success") == 1) {

                        appdata.setResponseCode(AppConstant.HTTPResponseCode.Success);
                        JSONObject jsonObject1 = jsonObject.optJSONObject("userinfo");
                        BeanUserData beanUserdata = new BeanUserData();
                        userName = jsonObject1.getString("display_name");
                        user_id = jsonObject1.getString("ID");
                        user_email = jsonObject1.getString("user_email");
                        user_lname = jsonObject1.getString("user_last_name");
                        user_fname = jsonObject1.getString("user_first_name");

                        beanUserdata.setDisplay_name(userName);
                        beanUserdata.setID(user_id);
                        beanUserdata.setUser_email(user_email);
                        beanUserdata.setUser_first_name(user_fname);
                        beanUserdata.setUser_last_name(user_lname);
                        beanUserdata.setUser_login(jsonObject1.getString("user_login"));
                        beanUserdata.setUser_nicename(jsonObject1.getString("user_nicename"));


                        JSONObject jsonObject2 = jsonObject.optJSONObject("billing_address");
                        bill_address = jsonObject2.getString("billing_address_1");
                        bill_city = jsonObject2.getString("billing_city");
                        bill_country = jsonObject2.getString("billing_country");
                        bill_company = jsonObject2.getString("billing_company");
                        bill_email = jsonObject2.getString("billing_email");
                        bill_fname = jsonObject2.getString("billing_first_name");
                        bill_lname = jsonObject2.getString("billing_last_name");
                        bill_phone = jsonObject2.getString("billing_phone");
                        bill_postcode = jsonObject2.getString("billing_postcode");
                        bill_state = jsonObject2.getString("billing_state");
                        bill_country_code = jsonObject2.getString("billing_country_code");
                        bill_state_code = jsonObject2.getString("billing_state_code");


                        beanUserdata.setBilling_address_1(bill_address);
                        beanUserdata.setBilling_city(bill_city);
                        beanUserdata.setBilling_country(bill_country);
                        beanUserdata.setBilling_company(bill_company);
                        beanUserdata.setBilling_email(bill_email);
                        beanUserdata.setBilling_first_name(bill_fname);
                        beanUserdata.setBilling_last_name(bill_lname);
                        beanUserdata.setBilling_phone(bill_phone);
                        beanUserdata.setBilling_postcode(bill_postcode);
                        beanUserdata.setBilling_state(bill_state);
                        beanUserdata.setBilling_country_code(bill_country_code);
                        beanUserdata.setShipping_state_code(bill_state_code);
                        //Log.d("TEST", "state====== "+jsonObject2.getString("billing_state"));


                        JSONObject jsonObject3 = jsonObject.optJSONObject("shipping_address");

                        ship_address = jsonObject3.getString("shipping_address_1");
                        ship_city = jsonObject3.getString("shipping_city");
                        ship_state = jsonObject3.getString("shipping_state");
                        ship_company = jsonObject3.getString("shipping_company");
                        ship_country = jsonObject3.getString("shipping_country");
                        ship_fname = jsonObject3.getString("shipping_first_name");
                        ship_lname = jsonObject3.getString("shipping_last_name");
                        ship_postcode = jsonObject3.getString("shipping_postcode");
                        ship_country_code = jsonObject3.getString("shipping_country_code");
                        ship_state_code = jsonObject3.getString("shipping_state_code");

                        beanUserdata.setShipping_address_1(ship_address);
                        beanUserdata.setShipping_city(ship_city);
                        beanUserdata.setShipping_company(ship_company);
                        beanUserdata.setShipping_country(ship_country);
                        beanUserdata.setShipping_state(ship_state);
                        beanUserdata.setShipping_first_name(ship_fname);
                        beanUserdata.setShipping_last_name(ship_lname);
                        beanUserdata.setShipping_postcode(ship_postcode);
                        beanUserdata.setShipping_country_code(ship_country_code);
                        beanUserdata.setShipping_state_code(ship_state_code);


                        userID = beanUserdata.getID();
                        userEmail = beanUserdata.getUser_email();
                        user_name = beanUserdata.getDisplay_name();

                        userDataList.add(beanUserdata);

                        manager.setPreferences(ApplicationContext.getAppContext(), "status", "1");
                        manager.setPreferences(ApplicationContext.getAppContext(), AppConstant.USER_ID, userID);
                        manager.setPreferences(ApplicationContext.getAppContext(), AppConstant.USER_EMAIL, userEmail);
                        manager.setPreferences(ApplicationContext.getAppContext(), AppConstant.USER_NAME, user_name);
                        manager.setPreferencesArraylist(ApplicationContext.getAppContext(), AppConstant.USER_DATA, userDataList);
                        userDataList = manager.getPreferencesArraylist(ApplicationContext.getAppContext(), AppConstant.USER_DATA);

                        // Log.d("userDataList","share name===="+ userDataList.get(0).getDisplay_name());

                        dataWrite.deletedData();

                        dataWrite.insertUser(user_fname, user_lname, user_id, user_email, bill_address, bill_city, bill_state, bill_state_code,
                                bill_country, bill_country_code, bill_email, bill_fname, bill_lname, bill_phone, bill_postcode,
                                ship_address, ship_city, ship_state, ship_state_code, ship_country, ship_country_code,
                                ship_fname, ship_lname, ship_postcode, bill_company, ship_company);
                    }
                    object = new JSONObject(response);
                    String success = object.getString("success");
                    if (success.equalsIgnoreCase("1")) {
                        if (key.equals("login")) {

                            Intent intentLogin = new Intent(CreateAccountActivity.this, MainActivity.class);
                            intentLogin.putExtra("LOGIN", "login");
                            startActivity(intentLogin);
                            finish();

                        } else {
                            Intent i = new Intent(CreateAccountActivity.this, CheckOutActivity.class);
                            i.putExtra("USER", "user");
                            startActivity(i);
                            finish();
                        }

                    } else {
                        JSONObject jsonObj = object.getJSONObject("data");
                        String registrationError = jsonObj.getString("registererror");
                        Toast.makeText(activity, registrationError, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*               progressDialog.dismiss();
                if (key.equals("login")) {

                    Intent intentLogin = new Intent(CreateAccountActivity.this, PayUmoneyActivity.class);
                    intentLogin.putExtra("LOGIN", "login");
                    startActivity(intentLogin);
                    finish();

                } else {
                    Intent i = new Intent(CreateAccountActivity.this, CheckOutActivity.class);
                    i.putExtra("USER", "user");
                    startActivity(i);
                    finish();
                }*/

                break;

            case Success0:

                progressDialog.dismiss();
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
}
