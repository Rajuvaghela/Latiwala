package com.lujayn.latiwala.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lujayn.latiwala.R;

/**
 * Created by Shailesh on 19/08/16.
 */
public class PaypalFragment extends Fragment {
    View rootView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_paypal, container, false);
        Toast.makeText(getActivity(),"paypal", Toast.LENGTH_SHORT).show();
        return rootView;
    }
}
