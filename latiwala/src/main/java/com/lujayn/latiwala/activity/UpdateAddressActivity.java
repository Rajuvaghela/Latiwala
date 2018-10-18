package com.lujayn.latiwala.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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

import dmax.dialog.SpotsDialog;

/**
 * Created by Shailesh on 10/02/17.
 */

public class UpdateAddressActivity extends AppCompatActivity implements RequestTaskDelegate {

    EditText et_fname, et_lname, et_address, et_country, et_state, et_city, et_postcode, et_email,
            et_phone, et_company;

    String fname, lname, address, country, state, city, postcode, email, phone, company, password, username;
    String userName, user_id, user_email, bill_address, bill_city, bill_state, bill_country, bill_email, bill_fname, bill_lname,
            bill_phone, bill_postcode, bill_company, ship_address, ship_city, ship_company, ship_state, ship_country,
            ship_fname, ship_lname, ship_postcode, ship_country_code, bill_state_code, bill_country_code, ship_state_code;
    String country_code, state_code;
    TextView btn_save;
    private ProgressDialog progressDialog;
    ArrayList<BeanCountry> countriesList;
    ArrayList<BeanState> statelist;
    ArrayList<BeanUserData> userDataList;
    DataWrite dataWrite;
    SessionManager manager;
    String status, key, g_key;
    CheckBox chbx_AsShipping;
    LinearLayout ll_back;
    String color_tool_back, color_tool_title, color_tool_icon, color_statusbar, color_btn, color_btn_text;
    RelativeLayout rl_tool;
    TextView title_tool;
    private ApplicationData appData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateaddress);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        manager = new SessionManager();
        appData = ApplicationData.getSharedInstance();
        dataWrite = new DataWrite(this);
        dataWrite.open();

        user_id = manager.getPreferences(UpdateAddressActivity.this, AppConstant.USER_ID);
        status = manager.getPreferences(UpdateAddressActivity.this, AppConstant.STATUS);
        SettingOption settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(UpdateAddressActivity.this, AppConstant.SETTING_OPTION);
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

        ll_back =  findViewById(R.id.ll_back);
        et_fname =  findViewById(R.id.etFirstName_update);
        et_lname =  findViewById(R.id.etLastName_update);
        et_address =  findViewById(R.id.etAddress_update);
        et_company =  findViewById(R.id.etCompany_update);
        et_country =  findViewById(R.id.etCountry_update);
        et_state =  findViewById(R.id.etState_update);
        et_city =  findViewById(R.id.etCity_update);
        et_postcode =  findViewById(R.id.etPostcode_update);
        et_email =  findViewById(R.id.etEmail_update);
        et_phone =  findViewById(R.id.etPhone_update);
        rl_tool =  findViewById(R.id.tool);
        title_tool =  findViewById(R.id.tvTitle);

        btn_save =  findViewById(R.id.btnUpdate);
        btn_save.setBackgroundColor(Color.parseColor(color_btn));
        btn_save.setTextColor(Color.parseColor(color_btn_text));
        rl_tool.setBackgroundColor(Color.parseColor(color_tool_back));

        RelativeLayout rl_header =  findViewById(R.id.rl_header);
        rl_header.setBackgroundColor(Color.parseColor(color_btn));


        title_tool.setTextColor(Color.parseColor(color_tool_title));
        Drawable myIcon5 = getResources().getDrawable(R.drawable.ic_arrow_back);
        //ColorFilter filter5 = new LightingColorFilter( Color.TRANSPARENT, Color.TRANSPARENT);
        myIcon5.setColorFilter(Color.parseColor(color_tool_icon), PorterDuff.Mode.SRC_ATOP);


        Intent i = getIntent();
        if (i.hasExtra("BILL")) {
            key = i.getStringExtra("BILL");
            title_tool.setText(getString(R.string.billing_address));
        }

        if (i.hasExtra("SHIP")) {
            key = i.getStringExtra("SHIP");
            et_email.setVisibility(View.GONE);
            et_phone.setVisibility(View.GONE);
            title_tool.setText(getString(R.string.shipping_address));
        }

        if (i.hasExtra("G_BILL")) {
            key = i.getStringExtra("G_BILL");
            title_tool.setText(getString(R.string.billing_address));
        }

        if (i.hasExtra("G_SHIP")) {
            key = i.getStringExtra("G_SHIP");
            et_email.setVisibility(View.GONE);
            et_phone.setVisibility(View.GONE);
            title_tool.setText(getString(R.string.shipping_address));
        }
        if (i.hasExtra("GUEST")) {
            g_key = i.getStringExtra("GUEST");
        }

        if (i.hasExtra("USER")) {
            g_key = i.getStringExtra("USER");
        }


        if (key.equals("billing") || key.equals("g_bill")) {
            userDataList = dataWrite.FetchUserData();

            et_fname.setText(userDataList.get(0).getBilling_first_name());
            et_lname.setText(userDataList.get(0).getBilling_last_name());
            et_address.setText(userDataList.get(0).getBilling_address_1());
            et_country.setText(userDataList.get(0).getBilling_country());
            et_company.setText(userDataList.get(0).getBilling_company());
            et_city.setText(userDataList.get(0).getBilling_city());
            et_state.setText(userDataList.get(0).getBilling_state());
            et_email.setText(userDataList.get(0).getBilling_email());
            et_phone.setText(userDataList.get(0).getBilling_phone());
            et_postcode.setText(userDataList.get(0).getBilling_postcode());
            country_code = userDataList.get(0).getBilling_country_code();
            state_code = userDataList.get(0).getBilling_state_code();

        } else if (key.equals("shipping") || key.equals("g_ship")) {

            userDataList = dataWrite.FetchUserData();

            et_fname.setText(userDataList.get(0).getShipping_first_name());
            et_lname.setText(userDataList.get(0).getShipping_last_name());
            et_address.setText(userDataList.get(0).getShipping_address_1());
            et_company.setText(userDataList.get(0).getShipping_company());
            et_country.setText(userDataList.get(0).getShipping_country());
            et_city.setText(userDataList.get(0).getShipping_city());
            et_state.setText(userDataList.get(0).getShipping_state());
            et_postcode.setText(userDataList.get(0).getShipping_postcode());
            country_code = userDataList.get(0).getShipping_country_code();
            state_code = userDataList.get(0).getShipping_state_code();
        }


        if (country_code.length() == 0) {

        } else {
            getState(country_code);
        }
        btn_save.setOnClickListener(onClickMethod);
        et_country.setOnClickListener(onClickMethod);
        et_state.setOnClickListener(onClickMethod);
        ll_back.setOnClickListener(onClickMethod);

    }

    public View.OnClickListener onClickMethod = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.etCountry_update:
                    getCountry("country");
                    break;
                case R.id.etState_update:
                    if (et_country.getText().length() == 0) {
                        // Toast.makeText(UpdateAddressActivity.this,"Please select country",Toast.LENGTH_SHORT).show();
                        dataWrite.showToast(getString(R.string.please_select_country));
                    } else {
                        //getState("state");
                        statedisplay();
                    }
                    break;


                case R.id.btnUpdate:

                    if (key.equals("billing") || key.equals("g_bill")) {

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
                            savebilling("saveBill");
                        }
                    } else {


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
                            saveShippingAddress("saveShip");
                        }

                    }

                    break;

                case R.id.ll_back:
                    if (g_key.equals("guest")) {
                        Intent i = new Intent(UpdateAddressActivity.this, CheckOutActivity.class);
                        i.putExtra("GUEST", g_key);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(UpdateAddressActivity.this, CheckOutActivity.class);
                        i.putExtra("USER", "user");
                        startActivity(i);
                        finish();
                    }

                    break;
            }
        }
    };

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


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateAddressActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.country_city_listview, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("List");
        ListView lv = (ListView) convertView.findViewById(R.id.lv_countrycity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateAddressActivity.this, android.R.layout.simple_list_item_1, list);
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


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateAddressActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.country_city_listview, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("List");
        ListView lv = (ListView) convertView.findViewById(R.id.lv_countrycity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateAddressActivity.this, android.R.layout.simple_list_item_1, list);
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
                //bill_country_code = countriesList.get(position).getCountry_code();
                et_state.setText("");
                getState(country_code);
                alert.dismiss();

            }
        });
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

    private void saveShippingAddress(String saveShip) {
        progressDialog.show();


        fname = et_fname.getText().toString();
        lname = et_lname.getText().toString();
        address = et_address.getText().toString();
        company = et_company.getText().toString();
        city = et_city.getText().toString();
        state = et_state.getText().toString();
        country = et_country.getText().toString();
        postcode = et_postcode.getText().toString();


        if (status.equals("1")) {
            //dataWrite.insertShipping(user_id,fname,lname,address,country,state,city,postcode,country_code,state_code, company);
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("shipping_first_name", fname);
                jsonObject.put("shipping_last_name", lname);
                jsonObject.put("shipping_address_1", address);
                jsonObject.put("shipping_company", company);
                jsonObject.put("shipping_city", city);
                jsonObject.put("shipping_state", state);
                jsonObject.put("shipping_country", country);
                jsonObject.put("shipping_postcode", postcode);
                jsonObject.put("shipping_country_code", country_code);
                jsonObject.put("shipping_state_code", state_code);
                jsonObject.put("user_id", user_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String requestURL = Webservice.BASE_URL + "" + Webservice.URL_SAVE_SHIPPING_ADDRESS;
            AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
            RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
            requestTask.delegate = this;
            requestTask.execute(requestURL, saveShip, jsonObject.toString());


        } else {
            user_id = "0.5";
            dataWrite.insertShipping(user_id, fname, lname, address, country, state, city, postcode, country_code, state_code, company);
            Intent i = new Intent(UpdateAddressActivity.this, CheckOutActivity.class);
            i.putExtra("GUEST", "guest");
            startActivity(i);
            finish();
            progressDialog.dismiss();
        }

    }


    private void savebilling(String saveBill) {
        progressDialog.show();
        userDataList = new ArrayList<BeanUserData>();

        fname = et_fname.getText().toString();
        lname = et_lname.getText().toString();
        address = et_address.getText().toString();
        city = et_city.getText().toString();
        state = et_state.getText().toString();
        company = et_company.getText().toString();
        country = et_country.getText().toString();
        email = et_email.getText().toString();
        phone = et_phone.getText().toString();
        postcode = et_postcode.getText().toString();


        if (status.equals("1")) {

            // dataWrite.insertBilling(user_id,fname,lname,address,country,state,city,postcode,email,phone,country_code,state_code, company);

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("billing_first_name", fname);
                jsonObject.put("billing_last_name", lname);
                jsonObject.put("billing_address_1", address);
                jsonObject.put("billing_company", company);
                jsonObject.put("billing_city", city);
                jsonObject.put("billing_state", state);
                jsonObject.put("billing_country", country);
                jsonObject.put("billing_email", email);
                jsonObject.put("billing_phone", phone);
                jsonObject.put("billing_postcode", postcode);
                jsonObject.put("billing_country_code", country_code);
                jsonObject.put("billing_state_code", state_code);
                jsonObject.put("user_id", user_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String requestURL = Webservice.BASE_URL + "" + Webservice.URL_SAVE_BILLINGADDRESS;
            AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
            RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
            requestTask.delegate = this;
            requestTask.execute(requestURL, saveBill, jsonObject.toString());


        } else {
            user_id = "0.5";
            dataWrite.insertBilling(user_id, fname, lname, address, country, state, city, postcode, email, phone, country_code, state_code, company);
            Intent i = new Intent(UpdateAddressActivity.this, CheckOutActivity.class);
            i.putExtra("GUEST", "guest");
            startActivity(i);
            finish();
            progressDialog.dismiss();
        }

    }

/*
    BroadcastReceiver receive=new BroadcastReceiver() {
        JSONObject object;
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            Bundle bundle = intent.getExtras();
            String data=bundle.getString(Intent_Class_Data.FILENAME);
            countriesList = new ArrayList<BeanCountry>();
            statelist = new ArrayList<BeanState>();
            if(bundle!=null){

                if (bundle.getString("type").equalsIgnoreCase("country")) {
                    Log.d("TEST", "country is== " + data);

                    try {
                        object = new JSONObject(data);
                        JSONObject jsonObject1 =object.optJSONObject("data");

                        if (object.getInt("success")==1){
                            JSONArray array = jsonObject1.optJSONArray("country_list");
                            for (int i =0;i<array.length();i++){
                                JSONObject jsonObject = array.optJSONObject(i);
                                BeanCountry country = new BeanCountry();
                                country.setCountry_code(jsonObject.getString("country_code"));
                                country.setCountry_name(jsonObject.getString("country_name"));
                                countriesList.add(country);

                            }

                        }else{
                           // Toast.makeText(UpdateAddressActivity.this, "Unknown error", Toast.LENGTH_LONG).show();
                            dataWrite.showToast("Unknown error");
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.d("TEST", "error =" + e);
                    }

                    countrydata();

                }else if (bundle.getString("type").equalsIgnoreCase("state")) {
                    Log.d("TEST", "state is== " + data);

                    try {
                        object = new JSONObject(data);
                        JSONObject jsonObject = object.optJSONObject("data");

                        if (object.getInt("success")==1){
                            JSONArray array = jsonObject.optJSONArray("state_list");
                            for (int i =0;i<array.length();i++){
                                JSONObject jsonObject1 = array.optJSONObject(i);
                                BeanState state = new BeanState();
                                state.setState_name(jsonObject1.getString("state_name"));
                                state.setState_code(jsonObject1.getString("state_code"));
                                statelist.add(state);
                            }
                            progressDialog.dismiss();
                        }else{

                            dataWrite.showToast( "Unknown error");
                            progressDialog.dismiss();
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                        progressDialog.dismiss();
                        Log.d("TEST", "error =" + e);
                    }

                *//*    if (statelist.size()==0){
                        et_state.setVisibility(View.GONE);

                    }else {
                        et_state.setVisibility(View.VISIBLE);
                    }*//*

                }else if (bundle.getString("type").equalsIgnoreCase("saveBill")){
                    Log.d("TEST", "saveBill is== " + data);

                    try {
                        object = new JSONObject(data);
                        JSONObject jsonObject = object.optJSONObject("data");

                        if (object.getInt("success")==1){

                            Intent i = new Intent(UpdateAddressActivity.this,CheckOutActivity.class);
                            i.putExtra("USER","user");
                            startActivity(i);
                            finish();
                        }else{

                            dataWrite.showToast( "Unknown error");
                        }

                        progressDialog.dismiss();
                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.d("TEST", "error =" + e);
                    }


                }else if (bundle.getString("type").equalsIgnoreCase("saveShip")){
                    Log.d("TEST", "saveShip is== " + data);
                    progressDialog.dismiss();
                    try {
                        object = new JSONObject(data);
                        JSONObject jsonObject = object.optJSONObject("data");

                        if (object.getInt("success")==1){

                            Intent i = new Intent(UpdateAddressActivity.this, CheckOutActivity.class);
                            i.putExtra("USER1","user");
                            startActivity(i);
                            finish();
                        }else{

                            dataWrite.showToast( "Unknown error");
                        }

                        progressDialog.dismiss();
                    } catch (Exception e) {
                        // TODO: handle exception
                        Log.d("TEST", "error =" + e);
                    }


                }
            }
        }
    };*/

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
        if (g_key.equals("guest")) {
            Intent i = new Intent(UpdateAddressActivity.this, CheckOutActivity.class);
            i.putExtra("GUEST", g_key);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(UpdateAddressActivity.this, CheckOutActivity.class);
            i.putExtra("USER", "user");
            startActivity(i);
            finish();
        }
    }

    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("product detail responseCode : " + responseCode);

        switch (responseCode) {
            case Country:
                //progressDialog.dismiss();
                countrydata();
                break;

            case State:
                progressDialog.dismiss();
                break;

            case SaveBill:
                Intent i = new Intent(UpdateAddressActivity.this, CheckOutActivity.class);
                i.putExtra("USER", "user");
                startActivity(i);
                finish();
                break;
            case SaveShip:

                Intent i1 = new Intent(UpdateAddressActivity.this, CheckOutActivity.class);
                i1.putExtra("USER1", "user");
                startActivity(i1);
                finish();
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
