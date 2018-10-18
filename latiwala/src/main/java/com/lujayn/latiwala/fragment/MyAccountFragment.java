package com.lujayn.latiwala.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lujayn.latiwala.R;
import com.lujayn.latiwala.activity.ChangePasswordActivity;
import com.lujayn.latiwala.activity.EditProfileActivity;
import com.lujayn.latiwala.activity.LoginActivity;
import com.lujayn.latiwala.activity.MainActivity;
import com.lujayn.latiwala.activity.PastOrderActivity;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationContext;
import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.database.DataWrite;


/**
 * Created by Shailesh on 10/08/16.
 */
public class MyAccountFragment extends Fragment implements MainActivity.OnBackPressedListener {
    View rootView;
    LinearLayout rl_profile, rl_changepass, rl_logout, rl_orders, rl_myaccount;
    SessionManager manager;
    DataWrite dataWrite;
    TextView tv_logout;

    public MyAccountFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments(); //change
        rootView = inflater.inflate(R.layout.inflater_myaccount, container, false);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        manager = new SessionManager();
        dataWrite = new DataWrite(getActivity());
        dataWrite.open();


        rl_changepass =  rootView.findViewById(R.id.ll_changepass);
        rl_profile =  rootView.findViewById(R.id.ll_profile);
        rl_logout =  rootView.findViewById(R.id.ll_logout);
        rl_orders =  rootView.findViewById(R.id.ll_orders);
        rl_myaccount =  rootView.findViewById(R.id.ll_myAccount);
        tv_logout =  rootView.findViewById(R.id.tvLogout1);


        String status = manager.getPreferences(getActivity(), AppConstant.STATUS);

        if (status.equals("1")) {
            tv_logout.setText(getString(R.string.logout));
        } else {
            tv_logout.setText(getString(R.string.login));
        }

        SettingOption settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(getActivity(), AppConstant.SETTING_OPTION);
        String color_back = settingOption.getData().getOptions().getStatus_bar_color();

        // rl_myaccount.setBackgroundColor(Color.parseColor(color_back));

        rl_changepass.setOnClickListener(onClickMethod);
        rl_profile.setOnClickListener(onClickMethod);
        rl_logout.setOnClickListener(onClickMethod);
        rl_orders.setOnClickListener(onClickMethod);
        return rootView;
    }


    private View.OnClickListener onClickMethod = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment fragment = null;
            String title = getString(R.string.app_name);
            switch (view.getId()) {
                case R.id.ll_profile:
                    startActivity(new Intent(getActivity(), EditProfileActivity.class));
                    break;
                case R.id.ll_changepass:

                    startActivity(new Intent(getActivity(), ChangePasswordActivity.class));

                    break;
                case R.id.ll_orders:
                    startActivity(new Intent(getActivity(), PastOrderActivity.class));

                    break;
                case R.id.ll_logout:

                    if (tv_logout.getText().toString().equals("Login")) {

                        Intent loginInt = new Intent(ApplicationContext.getAppContext(), LoginActivity.class);
                        startActivity(loginInt);
                        getActivity().finish();

                    } else {

                        manager.setPreferences(getActivity(), "status", "0");
                        dataWrite.deletedData();
                        Intent in = new Intent(ApplicationContext.getAppContext(), MainActivity.class);
                        startActivity(in);
                        getActivity().finish();

                    }

                    break;
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();

                // set the toolbar title
            }

        }
    };

    @Override
    public void doBack() {

        Fragment fr = new CategoryFragment();
        String title = getString(R.string.title_category);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_body, fr);
        ft.commit();
    }
}
