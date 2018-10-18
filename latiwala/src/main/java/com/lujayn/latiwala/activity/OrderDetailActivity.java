package com.lujayn.latiwala.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.bean.BeanOrderDetail;
import com.lujayn.latiwala.bean.Billing_address;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.bean.Shipping_address;
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

import java.lang.reflect.Type;

import dmax.dialog.SpotsDialog;

public class OrderDetailActivity extends AppCompatActivity implements RequestTaskDelegate, View.OnClickListener {

    LinearLayout productlist;
    LayoutInflater inflater;
    TextView textView;
    String orderID;
    BeanOrderDetail bean_data;
    DataWrite dataWrite;
    SettingOption settingOption;
    SessionManager manager;
    private ApplicationData appData;
    String orderdetail, color_statusbar;
    private ProgressDialog progressDialog;
    Activity activity;
    LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        activity = this;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        inflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(activity);


        Bundle bundle = getIntent().getExtras();
        orderID = bundle.getString("orderid");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        dataWrite = new DataWrite(activity);
        dataWrite.open();
        appData = ApplicationData.getSharedInstance();
        productlist = (LinearLayout) findViewById(R.id.llOrderDtail);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        manager = new SessionManager();
        settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(activity, AppConstant.SETTING_OPTION);
        String color_back = settingOption.getData().getOptions().getCate_shape_color();
        color_statusbar = settingOption.getData().getOptions().getStatus_bar_color();
        RelativeLayout rl_header = (RelativeLayout) findViewById(R.id.rl_header);
        rl_header.setBackgroundColor(Color.parseColor(color_back));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color_statusbar));
        }

        orderdetail = dataWrite.fetchOrderDetail(orderID);
        if (orderdetail.length() > 0) {
            progressDialog.show();
            OrderDetail();
        } else {
            DisplayItmes("orderdetail");
        }
        ll_back.setOnClickListener(this);

    }

    private void DisplayItmes(String orderdetail) {

        progressDialog.show();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("orderid", orderID);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_ORDER_DETAIL;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, orderdetail, jsonObject.toString());


    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //getActivity().registerReceiver(receive, new IntentFilter(Intent_Class_Data.NOTIFICATION));

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // getActivity().unregisterReceiver(receive);

    }


    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("order responseCode : " + responseCode);

        switch (responseCode) {
            case Success:
                orderdetail = dataWrite.fetchOrderDetail(orderID);
                OrderDetail();
                break;

            case ServerError:
                progressDialog.dismiss();
                appData.showUserAlert(activity, getString(R.string.alert_title_message),
                        getString(R.string.alert_body_server_error), null);
                break;


        }
    }

    private void OrderDetail() {


        try {
            bean_data = new BeanOrderDetail();

            Gson gson = new Gson();
            if (orderdetail != null) {
                Type type_one = new TypeToken<BeanOrderDetail>() {
                }.getType();
                bean_data = gson.fromJson(orderdetail, type_one);

                //     Log.d("TEST", "orders count is " + bean_data.getCount());
                if (bean_data.getCount().equals("0")) {
                    // Toast.makeText(ApplicationContext.getAppContext(),"Order not found",Toast.LENGTH_SHORT).show();
                    dataWrite.showToast(getString(R.string.order_not_found));

                } else {

                    TextView tv_name, tv_amount;

                    final View viewLable = inflater.inflate(R.layout.inflater_title, null);
                    TextView tv_lable = (TextView) viewLable.findViewById(R.id.tvTitleorder);
                    tv_lable.setText(R.string.order_detail);
                    productlist.addView(viewLable);

                    for (int i = 0; i < bean_data.getData().getOrders_detail().getProducts().size(); i++) {
                        final View view = inflater.inflate(R.layout.inflater_itemslist, null);
                        tv_name = (TextView) view.findViewById(R.id.tvName);
                        tv_amount = (TextView) view.findViewById(R.id.tvAmount);
                        tv_name.setText(bean_data.getData().getOrders_detail().getProducts().get(i).getName() + " x " + bean_data.getData().getOrders_detail().getProducts().get(i).getQty());

                        double price = Double.parseDouble(bean_data.getData().getOrders_detail().getProducts().get(i).getPrice());
                        int qnt = Integer.parseInt(bean_data.getData().getOrders_detail().getProducts().get(i).getQty());

                        //double amount  =price*qnt;
                        tv_amount.setText(settingOption.getData().getOptions().getCurrency_symbol() + "" + price);
                        productlist.addView(view);
                    }


                    TextView tv_subtotal, address, address_label, amount;


                    if (bean_data.getData().getOrders_detail().getOrder_detail().getSubtotal() != null) {
                        final View view = inflater.inflate(R.layout.inflater_itemslist, null);
                        tv_subtotal = (TextView) view.findViewById(R.id.tvName);
                        amount = (TextView) view.findViewById(R.id.tvAmount);
                        tv_subtotal.setText(R.string.subtotal);
                        amount.setText(settingOption.getData().getOptions().getCurrency_symbol() + "" + bean_data.getData().getOrders_detail().getOrder_detail().getSubtotal());

                        productlist.addView(view);
                    }


                    if (bean_data.getData().getOrders_detail().getOrder_detail().getTotal_discount() != null) {
                        final View view = inflater.inflate(R.layout.inflater_itemslist, null);
                        tv_subtotal = (TextView) view.findViewById(R.id.tvName);
                        amount = (TextView) view.findViewById(R.id.tvAmount);
                        tv_subtotal.setText(R.string.discount);
                        amount.setText("-" + settingOption.getData().getOptions().getCurrency_symbol() + "" + bean_data.getData().getOrders_detail().getOrder_detail().getTotal_discount());
                        productlist.addView(view);
                    }

                    if (bean_data.getData().getOrders_detail().getOrder_detail().getShipping_methods() != null) {
                        final View view = inflater.inflate(R.layout.inflater_itemslist, null);
                        tv_subtotal = (TextView) view.findViewById(R.id.tvName);
                        amount = (TextView) view.findViewById(R.id.tvAmount);
                        tv_subtotal.setText(R.string.shipping);
                        amount.setText(bean_data.getData().getOrders_detail().getOrder_detail().getShipping_methods());

                        productlist.addView(view);
                    }

                    if (bean_data.getData().getOrders_detail().getOrder_detail().getPayment_details().getMethod_title() != null) {
                        final View view = inflater.inflate(R.layout.inflater_itemslist, null);
                        tv_subtotal = (TextView) view.findViewById(R.id.tvName);
                        amount = (TextView) view.findViewById(R.id.tvAmount);
                        tv_subtotal.setText(R.string.payment_details);
                        amount.setText(bean_data.getData().getOrders_detail().getOrder_detail().getPayment_details().getMethod_title());

                        productlist.addView(view);
                    }

                    if (bean_data.getData().getOrders_detail().getOrder_detail().getTotal() != null) {
                        final View view = inflater.inflate(R.layout.inflater_itemslist, null);
                        tv_subtotal = (TextView) view.findViewById(R.id.tvName);
                        amount = (TextView) view.findViewById(R.id.tvAmount);
                        tv_subtotal.setText(getString(R.string.total));
                        amount.setText(settingOption.getData().getOptions().getCurrency_symbol() + "" + bean_data.getData().getOrders_detail().getOrder_detail().getTotal());
                        productlist.addView(view);

                    }

                    if (bean_data.getData().getOrders_detail().getOrder_detail().getBilling_address().getEmail() != null) {
                        final View viewLable1 = inflater.inflate(R.layout.inflater_title, null);
                        TextView tv_lable1 = (TextView) viewLable1.findViewById(R.id.tvTitleorder);
                        tv_lable1.setText(R.string.customer_details);
                        productlist.addView(viewLable1);

                        final View view = inflater.inflate(R.layout.inflater_itemslist, null);
                        tv_subtotal = (TextView) view.findViewById(R.id.tvName);
                        amount = (TextView) view.findViewById(R.id.tvAmount);
                        tv_subtotal.setText(R.string.email);
                        amount.setText(bean_data.getData().getOrders_detail().getOrder_detail().getBilling_address().getEmail());
                        productlist.addView(view);

                    }

                    if (bean_data.getData().getOrders_detail().getOrder_detail().getBilling_address().getPhone() != null) {
                        final View view = inflater.inflate(R.layout.inflater_itemslist, null);
                        tv_subtotal = (TextView) view.findViewById(R.id.tvName);
                        amount = (TextView) view.findViewById(R.id.tvAmount);
                        tv_subtotal.setText(getString(R.string.phone));
                        amount.setText(bean_data.getData().getOrders_detail().getOrder_detail().getBilling_address().getPhone());
                        productlist.addView(view);

                    }

                    if (bean_data.getData().getOrders_detail().getOrder_detail().getBilling_address().getFirst_name() != null) {
                        Billing_address address1 = new Billing_address();
                        address1 = bean_data.getData().getOrders_detail().getOrder_detail().getBilling_address();

                        final View view = inflater.inflate(R.layout.inflater_address_orders, null);
                        address_label = (TextView) view.findViewById(R.id.tvLabelAddress);
                        address = (TextView) view.findViewById(R.id.tvAddress);
                        address_label.setText(getString(R.string.billing_address));
                        String bill = address1.getCompany() + "\n" + address1.getFirst_name() + " " + address1.getLast_name() + "\n" +
                                address1.getAddress_1() + "\n" + address1.getCity() + " " + address1.getPostcode() + "\n" + address1.getState()
                                + " " + address1.getCountry();
                        address.setText(bill.trim());
                        productlist.addView(view);
                    }

                    if (bean_data.getData().getOrders_detail().getOrder_detail().getShipping_address().getFirst_name() != null) {
                        Shipping_address address1 = new Shipping_address();
                        address1 = bean_data.getData().getOrders_detail().getOrder_detail().getShipping_address();

                        final View view = inflater.inflate(R.layout.inflater_address_orders, null);
                        address_label = (TextView) view.findViewById(R.id.tvLabelAddress);
                        address = (TextView) view.findViewById(R.id.tvAddress);
                        address_label.setText(getString(R.string.shipping_address));
                        String shipp = address1.getCompany() + "\n" + address1.getFirst_name() + " " + address1.getLast_name() + "\n" +
                                address1.getAddress_1() + "\n" + address1.getCity() + "-" + address1.getPostcode() + "\n" + address1.getState()
                                + " " + address1.getCountry();
                        address.setText(shipp.trim());
                        productlist.addView(view);

                    }

                }

            }

            progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            AppDebugLog.println("error = " + e);

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
