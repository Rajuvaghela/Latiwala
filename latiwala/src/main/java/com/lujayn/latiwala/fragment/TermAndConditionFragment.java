package com.lujayn.latiwala.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.lujayn.latiwala.R;
import com.lujayn.latiwala.activity.MainActivity;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.SessionManager;

/**
 * Created by Shailesh on 01/08/16.
 */
public class TermAndConditionFragment extends Fragment implements MainActivity.OnBackPressedListener {

    WebView webView;
    View rootView;
    String url;
    SessionManager manager;
    TextView textView;
    String terms_string;
    public TermAndConditionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_termcondition, container, false);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        webView = (WebView) rootView.findViewById(R.id.tv_terms);
        manager = new SessionManager();
        SettingOption settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(getActivity(), AppConstant.SETTING_OPTION);
        terms_string = settingOption.getData().getOptions().getTerms_and_condition();


       // textView.setText(Html.fromHtml(terms_string));


        manager = new SessionManager();
        //url = getArguments().getString("url");
       // webView.loadUrl(url);
        webView.loadData(terms_string, "text/html", "UTF-8");
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
