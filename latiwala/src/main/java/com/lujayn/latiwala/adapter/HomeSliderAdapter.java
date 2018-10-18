package com.lujayn.latiwala.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.bean.BeanSliderImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raju vaghela on 20-09-2018.
 */
public class HomeSliderAdapter extends PagerAdapter {

    private Context activity;

    public  ArrayList<BeanSliderImage> beanSliderImages;

    public HomeSliderAdapter(Context activity, ArrayList<BeanSliderImage> beanSliderImages) {
        this.activity = activity;
        this.beanSliderImages = beanSliderImages;
    }

    @Override
    public int getCount() {
        return beanSliderImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        Glide.with(activity).load(beanSliderImages.get(position).image).into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
    public int getImage(String imageName) {
        int drawableResourceId = activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName());
        return drawableResourceId;
    }
}
