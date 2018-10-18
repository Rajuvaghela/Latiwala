package com.lujayn.latiwala.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.lujayn.latiwala.R;
import com.lujayn.latiwala.activity.MainActivity;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.SessionManager;

/**
 * Created by Shailesh on 01/08/16.
 */
public class ContactUsFragment extends Fragment implements MainActivity.OnBackPressedListener {

    View rootView;
    WebView webView;
    SessionManager manager;
    String contact_string;
    public ContactUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contactus, container, false);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        webView = (WebView) rootView.findViewById(R.id.wv_contact);
        manager = new SessionManager();
        SettingOption settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(getActivity(), AppConstant.SETTING_OPTION);
        contact_string = settingOption.getData().getOptions().getContact_info();


       // textView.setText(Html.fromHtml(contact_string));
        webView.loadData(contact_string, "text/html", "UTF-8");
        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void doBack() {
        Fragment fr=new CategoryFragment();
        String title = getString(R.string.title_category);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.container_body, fr);
        ft.commit();
    }
}
