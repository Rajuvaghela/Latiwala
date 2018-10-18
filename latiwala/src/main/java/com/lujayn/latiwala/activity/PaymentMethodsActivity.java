package com.lujayn.latiwala.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.lujayn.latiwala.R;


public class PaymentMethodsActivity extends AppCompatActivity {
    RadioGroup rg_paymentMethod;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentmethods);
        rg_paymentMethod = (RadioGroup)findViewById(R.id.rgPaymentMethod);

       // paymentmethods_call("method");

    }
/*
    private void paymentmethods_call(String method) {
        Intent intent=new Intent(PaymentMethodsActivity.this, Intent_Class_Data.class);
        intent.putExtra("url", Webservice.BASE_URL+""+Webservice.URL_SHIPPIN_METHOD);
        intent.putExtra("type", method);
        startService(intent);
    }*/



/*    BroadcastReceiver receive=new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            Bundle bundle = intent.getExtras();
            String data = bundle.getString(Intent_Class_Data.FILENAME);
            if (bundle != null) {

                if (bundle.getString("type").equalsIgnoreCase("method")) {
                    Log.d("TEST", "method is== " + data);
                }
            }
        }
    };*/
}
