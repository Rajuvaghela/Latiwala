package com.lujayn.latiwala.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.activity.ProductDetailActivity;
import com.lujayn.latiwala.bean.BeanHomeProduct;
import com.lujayn.latiwala.bean.BeanHomeProduct;
import com.lujayn.latiwala.bean.BeanProduct;
import com.lujayn.latiwala.common.AppConstant;

import java.util.ArrayList;


public class AdapterHomeProduct extends RecyclerView.Adapter<AdapterHomeProduct.MyViewHolder> {
    ArrayList<BeanHomeProduct> beanHomeProducts = new ArrayList<>();
    ArrayList<BeanProduct> productlist = new ArrayList<>();
    Context activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView productName, sale_price, regular_price, qty, offers, description;
        ImageView productImage, icWish;
        RatingBar ratingbar;
        RelativeLayout rlProductmain;
        ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            sale_price = (TextView) view.findViewById(R.id.tvPrice_product);
            regular_price = (TextView) view.findViewById(R.id.tvRegularPrice);
            description = (TextView) view.findViewById(R.id.tvProductdescription);
            offers = (TextView) view.findViewById(R.id.tvOffers_product);
            productImage = (ImageView) view.findViewById(R.id.ivProductImage);
            progressBar = (ProgressBar) view.findViewById(R.id.pbarProduct);
            ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
            rlProductmain = (RelativeLayout) view.findViewById(R.id.rlProduct);
            icWish = (ImageView) view.findViewById(R.id.icWish_product);
        }
    }

    public AdapterHomeProduct(ArrayList<BeanProduct> productlist, ArrayList<BeanHomeProduct> beanHomeProducts, Context activity) {
        this.beanHomeProducts = beanHomeProducts;
        this.productlist = productlist;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflater_product2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            holder.offers.setVisibility(View.VISIBLE);
            holder.regular_price.setVisibility(View.VISIBLE);
            holder.regular_price.setPaintFlags(holder.regular_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.regular_price.setText(beanHomeProducts.get(position).regularPrice);
            holder.sale_price.setText(beanHomeProducts.get(position).salePrice);
            holder.offers.setText(beanHomeProducts.get(position).onSale);

            String rate = beanHomeProducts.get(position).rating;

            if (rate.trim().length() == 0) {

            } else {
                String[] rating = rate.split(" ");
                float finalrate = Float.parseFloat(rating[0]);
                holder.ratingbar.setRating(finalrate);
            }
            holder.description.setText(beanHomeProducts.get(position).title);


            Glide.with(activity).load(beanHomeProducts.get(position).image)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model,
                                                   Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.productImage);


            holder.rlProductmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.PRODUCT_ID = beanHomeProducts.get(position).productId;
                    AppConstant.productname = beanHomeProducts.get(position).title;
                    Intent intent = new Intent(activity, ProductDetailActivity.class);
                    intent.putExtra("beanHomeProductList", productlist);
                    intent.putExtra("parentClassName", "AdapterHomeProduct");
                    intent.putExtra("position", position);
                    activity.startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e("AdapterHomeProduct", ":Exception+++++++++++1");
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return beanHomeProducts.size();
    }


}
