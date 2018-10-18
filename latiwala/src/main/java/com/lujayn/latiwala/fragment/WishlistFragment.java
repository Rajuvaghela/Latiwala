package com.lujayn.latiwala.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.activity.MainActivity;
import com.lujayn.latiwala.activity.ProductDetailActivity;
import com.lujayn.latiwala.bean.BeanWishlist;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.database.DataWrite;


import java.util.ArrayList;

/**
 * Created by Shailesh on 10/06/17.
 */

public class WishlistFragment extends Fragment implements MainActivity.OnBackPressedListener {

    View rootView;
    ArrayList<BeanWishlist> wishlists;
    DataWrite dataWrite;
    private RecyclerView recyclerView;
    LinearLayout ll_noproduct;
    TextView btn_cntShopping;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_wishlist, container, false);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        ((MainActivity) getActivity()).WishVisibility(View.GONE);
        initialize();
        return rootView;
    }

    private void initialize() {

        dataWrite = new DataWrite(getActivity());
        dataWrite.open();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_wish);
        ll_noproduct = (LinearLayout) rootView.findViewById(R.id.llNoproduct_wish);
        btn_cntShopping = (TextView) rootView.findViewById(R.id.btnCtnueShopping_wish);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btn_cntShopping.setOnClickListener(onClickMethod);
        fetchwishlist();
    }

    private View.OnClickListener onClickMethod = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.btnCtnueShopping_wish:
                    Fragment fr = new CategoryFragment();
                    String title = getString(R.string.title_category);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.container_body, fr);
                    ft.commit();
                    ((MainActivity) getActivity()).CartVisibility(View.VISIBLE);

                    break;

            }
        }
    };

    private void fetchwishlist() {
        wishlists = dataWrite.fetchWishlist();

        if (wishlists.size() == 0) {
            ll_noproduct.setVisibility(View.VISIBLE);
        } else {
            ll_noproduct.setVisibility(View.GONE);
            WishAdapter wishAdapter = new WishAdapter(wishlists);
            recyclerView.setAdapter(wishAdapter);
        }
    }

    @Override
    public void doBack() {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();

        } else {
        }

        ((MainActivity) getActivity()).WishVisibility(View.VISIBLE);

    }


    public class WishAdapter extends RecyclerView.Adapter<WishAdapter.MyViewHolder> {

        private Context mContext;
        ArrayList<BeanWishlist> bean_list;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView productName, price, description;
            ImageView productImage, icremove;
            CardView cardView;

            public MyViewHolder(View view) {
                super(view);

                price = (TextView) view.findViewById(R.id.tvPriceCart_wish);
                productImage = (ImageView) view.findViewById(R.id.ivCartImage_wish);
                productName = (TextView) view.findViewById(R.id.tvNameCart_wish);
                icremove = (ImageView) view.findViewById(R.id.ivRemove_wish);
                cardView = (CardView) view.findViewById(R.id.card_wish);


            }
        }


        public WishAdapter(ArrayList<BeanWishlist> wishlist) {

            this.bean_list = wishlist;
        }

        @Override
        public WishAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inflater_wishlist, parent, false);


            WishAdapter.MyViewHolder viewHolder = new WishAdapter.MyViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final WishAdapter.MyViewHolder vh, final int i) {
            // Log.d("ItemID", "========="+ i);
            final int index = i;

            vh.productName.setText(bean_list.get(i).getTitle());
            vh.price.setText(bean_list.get(i).getSale_price());

           /* Picasso.with(getActivity())
                    .load(bean_list.get(i).getImage())
                    .into(vh.productImage);*/

            Glide.with(getActivity()).load(bean_list.get(i).getImage())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(vh.productImage);

            vh.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AppConstant.PRODUCT_ID = bean_list.get(index).getProduct_id();
                    AppConstant.productname = bean_list.get(index).getTitle();
                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                    intent.putExtra("position", index);
                    startActivity(intent);

                }
            });

            vh.icremove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dataWrite.deleteWish(bean_list.get(index).getProduct_id());
                    wishlists.clear();
                    fetchwishlist();
                    notifyDataSetChanged();

                }
            });
        }

        @Override
        public int getItemCount() {
            return bean_list.size();
        }

    }
}
