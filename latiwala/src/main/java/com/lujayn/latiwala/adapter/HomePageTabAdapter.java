package com.lujayn.latiwala.adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.lujayn.latiwala.R;
import com.lujayn.latiwala.activity.ProductActivity;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.fragment.BeautyFragment;
import com.lujayn.latiwala.fragment.CategoryFragment;
import com.lujayn.latiwala.fragment.HomeFragment;
import com.lujayn.latiwala.fragment.OffersFragments;
import com.lujayn.latiwala.fragment.ProductFragment;

public class HomePageTabAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    Activity activity;

    //Constructor to the class
    public HomePageTabAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.activity = activity;
        //Initializing tab count
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                HomeFragment tab0 = new HomeFragment();
                return tab0;
            case 1:
                CategoryFragment tab1 = new CategoryFragment();
                return tab1;
            case 2:
                ProductFragment tab2 = new ProductFragment();
                return tab2;
            case 3:
                OffersFragments tab3 = new OffersFragments();
                return tab3;
            case 4:
                BeautyFragment tab4 = new BeautyFragment();
                return tab4;
            default:
                return new CategoryFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Category";
            case 2:
                return "Products";
            case 3:
                return "Offers";
            case 4:
                return "Jewellery";

            default:
                return "Home";
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}