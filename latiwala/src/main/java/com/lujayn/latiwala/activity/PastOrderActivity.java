package com.lujayn.latiwala.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.bean.BeanPastOrders;
import com.lujayn.latiwala.bean.Orders;
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

import java.lang.reflect.Type;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class PastOrderActivity extends AppCompatActivity implements RequestTaskDelegate, View.OnClickListener {
    ListView listView;
    SessionManager manager;
    String user_id;
    BeanPastOrders bean_data;
    private ProgressDialog progressDialog;
    DataWrite dataWrite;
    SettingOption settingOption;
    private ApplicationData appData;
    String orderdata, color_statusbar;
    Activity activity;
    LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_order);
        activity = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        manager = new SessionManager();
        appData = ApplicationData.getSharedInstance();
        settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(activity, AppConstant.SETTING_OPTION);
        String color_back = settingOption.getData().getOptions().getCate_shape_color();
        color_statusbar = settingOption.getData().getOptions().getStatus_bar_color();
        user_id = manager.getPreferences(activity, AppConstant.USER_ID);
        dataWrite = new DataWrite(activity);
        dataWrite.open();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color_statusbar));
        }

        listView = (ListView) findViewById(R.id.lvPastorder);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        RelativeLayout rl_header = (RelativeLayout) findViewById(R.id.rl_header);

        rl_header.setBackgroundColor(Color.parseColor(color_back));


        Pastorder_webservice("pastorder");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //   Log.d("shailesh", "data  is==="+ bean_data.getData().getOrders()[i].getItems());

                Intent intent = new Intent(activity, OrderDetailActivity.class);
                intent.putExtra("orderid", bean_data.getData().getOrders().get(i).getOrderid());
                startActivity(intent);

            }

        });
        ll_back.setOnClickListener(this);

    }

    private void Pastorder_webservice(String pastorder) {
        progressDialog.show();


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("user_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_PAST_ORDER;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, pastorder, jsonObject.toString());


    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // getActivity().registerReceiver(receive, new IntentFilter(Intent_Class_Data.NOTIFICATION));

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //  getActivity().unregisterReceiver(receive);

    }

    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("order responseCode : " + responseCode);

        switch (responseCode) {
            case Success:
                orderdata = dataWrite.fetchOrders(user_id);
                orders();
                break;

            case ServerError:
                appData.showUserAlert(activity, getString(R.string.alert_title_message),
                        getString(R.string.alert_body_server_error), null);
                break;

        }

    }

    private void orders() {
        try {
            bean_data = new BeanPastOrders();

            Gson gson = new Gson();
            if (orderdata != null) {
                Type type_one = new TypeToken<BeanPastOrders>() {
                }.getType();
                bean_data = gson.fromJson(orderdata, type_one);

                //     Log.d("TEST", "orders count is " + bean_data.getCount());
                if (bean_data.getCount().equals("0")) {
                    //Toast.makeText(ApplicationContext.getAppContext(),"Order not found",Toast.LENGTH_SHORT).show();
                    dataWrite.showToast(getString(R.string.order_not_found));
                    progressDialog.dismiss();
                } else {
                    //dataWrite.showToast("Order found");
                    PastOrderAdapter pastOrderAdapter = new PastOrderAdapter(bean_data.getData().getOrders());
                    listView.setAdapter(pastOrderAdapter);
                    progressDialog.dismiss();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();

            AppDebugLog.println("Error = " + e);

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

    public class PastOrderAdapter extends BaseAdapter {
        Context context;
        ArrayList<Orders> orderses_list;
        LayoutInflater inflater;


        public PastOrderAdapter(ArrayList<Orders> beanPastorder) {
            this.orderses_list = beanPastorder;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return orderses_list.size();
        }

        @Override
        public Object getItem(int i) {
            return orderses_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder vh;

            if (view == null) {
                vh = new ViewHolder();
                view = inflater.inflate(R.layout.inflater_pastorder, null);
                vh.tv_orderid = (TextView) view.findViewById(R.id.tvOrderId);
                vh.tv_dateTime = (TextView) view.findViewById(R.id.tvOrderdate);
                //vh.tv_title = (TextView)view.findViewById(R.id.tvTitlePastOrder);
                vh.tv_total = (TextView) view.findViewById(R.id.tvPricePastOrder);
                vh.tv_status = (TextView) view.findViewById(R.id.tvStatus);
                //vh.imageView = (ImageView)view.findViewById(R.id.ivPastOrder);
                view.setTag(vh);
            } else {

                vh = (ViewHolder) view.getTag();
            }
            vh.tv_orderid.setText(orderses_list.get(i).getOrderid());
            vh.tv_dateTime.setText(orderses_list.get(i).getDate());
            //vh.tv_title.setText(orderses_list.get(i).getTitle());
            vh.tv_total.setText(settingOption.getData().getOptions().getCurrency_symbol() + orderses_list.get(i).getTotal());
            vh.tv_status.setText(orderses_list.get(i).getStatus());


            return view;
        }

        public class ViewHolder {
            TextView tv_orderid, tv_status, tv_dateTime, tv_title, tv_total;
            ImageView imageView;

        }
    }
}
