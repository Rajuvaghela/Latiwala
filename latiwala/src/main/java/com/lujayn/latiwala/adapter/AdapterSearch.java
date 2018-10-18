package com.lujayn.latiwala.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.lujayn.latiwala.R;
import com.lujayn.latiwala.activity.ProductDetailActivity;
import com.lujayn.latiwala.bean.BeanSearchProduct;
import com.lujayn.latiwala.common.AppConstant;

import java.util.ArrayList;


public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.MyViewHolder> implements Filterable {
    ArrayList<BeanSearchProduct> beanSearchProducts = new ArrayList<>();
    ArrayList<BeanSearchProduct> array = new ArrayList<>();
    Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tv_search_product;

        public MyViewHolder(View view) {
            super(view);
            tv_search_product = (TextView) view.findViewById(R.id.tv_search_product);

        }
    }

    public AdapterSearch(ArrayList<BeanSearchProduct> beanSearchProducts, Activity activity) {
        this.beanSearchProducts = beanSearchProducts;
        this.array = beanSearchProducts;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        try {
            holder.tv_search_product.setText(beanSearchProducts.get(position).Title);
            holder.tv_search_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppConstant.PRODUCT_ID = beanSearchProducts.get(position).Product_id;
                    AppConstant.productname = beanSearchProducts.get(position).Title;
                    Intent intent = new Intent(activity, ProductDetailActivity.class);
                    intent.putExtra("parentClassName", "AdapterSearch");
                    intent.putExtra("position", position);
                    activity.startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e("AdapterSearch", ":Exception++++++++++++++++++1");
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return beanSearchProducts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                String s = String.valueOf(charSequence);

                if (s != null && s.length() > 0) {
                    ArrayList<BeanSearchProduct> temp = new ArrayList<>();
                    for (BeanSearchProduct category : array) {
                        if (category.Title.toLowerCase().contains(s.toLowerCase())) {
                            temp.add(category);
                        }
                    }
                    results.values = temp;
                    results.count = temp.size();
                } else {
                    results.values = array;
                    results.count = array.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                beanSearchProducts = (ArrayList<BeanSearchProduct>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
