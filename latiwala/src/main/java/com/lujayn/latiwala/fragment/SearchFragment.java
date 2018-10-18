package com.lujayn.latiwala.fragment;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lujayn.latiwala.activity.MainActivity;
import com.lujayn.latiwala.activity.ProductDetailActivity;
import com.lujayn.latiwala.adapter.GridSpacingItemDecoration;
import com.lujayn.latiwala.bean.BeanProduct;
import com.lujayn.latiwala.bean.BeanUserData;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationData;

import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.common.Webservice;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.database.DataWrite;
import com.lujayn.latiwala.network.RequestTask;
import com.lujayn.latiwala.network.RequestTaskDelegate;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by Shailesh on 19/09/16.
 */
public class SearchFragment extends Fragment implements MainActivity.OnBackPressedListener,
        RequestTaskDelegate {

    View rootView;
    //GridLayout gridLayout;
    String catid, catName, user_id;
    ArrayList<BeanProduct> productlist;
    LayoutInflater inflater;
    SessionManager manager;
    EditText et_search;
    ImageView iv_search;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    DataWrite dataWrite;
    private ApplicationData appData;
    String status, country, state, postcode, city, tax_based_on, baseAddress_country, baseAddress_state;
    SettingOption settingOption;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        inflater = (LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_search, container, false);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        manager = new SessionManager();
        appData = ApplicationData.getSharedInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading..");
        user_id = manager.getPreferences(getActivity(), AppConstant.USER_ID);
        status = manager.getPreferences(getActivity(), AppConstant.STATUS);

        settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(getActivity(), AppConstant.SETTING_OPTION);
        tax_based_on = settingOption.getData().getOptions().getTax_based_on();


        String b = settingOption.getData().getOptions().getBase_location().getWoocommerce_default_country();


        String base[] = b.split(":");
        if (base.length > 1) {
            baseAddress_country = base[0];
            baseAddress_state = base[1];
        } else {
            baseAddress_country = base[0];
        }
        dataWrite = new DataWrite(getActivity());
        dataWrite.open();


        if (status.equals("1")) {
            ArrayList<BeanUserData> userDatas = dataWrite.FetchUserData();

            if (tax_based_on.equals("shipping")) {

                country = userDatas.get(0).getShipping_country_code();
                state = userDatas.get(0).getShipping_state_code();
                postcode = userDatas.get(0).getShipping_postcode();
                city = userDatas.get(0).getShipping_city();

            } else if (tax_based_on.equals("billing")) {

                country = userDatas.get(0).getBilling_country_code();
                state = userDatas.get(0).getBilling_state_code();
                postcode = userDatas.get(0).getBilling_postcode();
                city = userDatas.get(0).getBilling_city();

            } else {
                country = baseAddress_country;
                state = baseAddress_state;
                postcode = "";
                city = "";
            }

        } else {
            if (tax_based_on.equals("base")) {
                country = baseAddress_country;
                state = baseAddress_state;
                postcode = "";
                city = "";
            } else {
                country = "";
                state = "";
                postcode = "";
                city = "";
            }
        }


        // gridLayout =(GridLayout) rootView.findViewById(R.id.gridlayout);
        et_search = (EditText) rootView.findViewById(R.id.etSearch);
        iv_search = (ImageView) rootView.findViewById(R.id.ivSearch);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_search);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // catid = getArguments().getString("catID");
        //  catName = getArguments().getString("catName");
        // Log.d("TEST", "catID======catName  "+catid+"     "+catName);

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayProduct("search");
            }
        });

        return rootView;
    }

    private void displayProduct(String search) {

        if (et_search.getText().length() == 0) {
            et_search.setError(getString(R.string.please_Enter_Keywords));
        } else {
            progressDialog.show();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("search", et_search.getText().toString());
                //jsonObject.put("user_id",user_id);
                jsonObject.put("country", country);
                jsonObject.put("state", state);
                jsonObject.put("postcode", postcode);
                jsonObject.put("city", city);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String requestURL = Webservice.BASE_URL + "" + Webservice.URL_Products;
            AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
            RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
            requestTask.delegate = this;
            requestTask.execute(requestURL, search, jsonObject.toString());

        }

    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //getActivity().registerReceiver(receive, new IntentFilter(Intent_Class_Data.NOTIFICATION));

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //getActivity().unregisterReceiver(receive);

    }

    @Override
    public void doBack() {
        Fragment fr = new CategoryFragment();
        String title = getString(R.string.title_category);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container_body, fr);
        ft.commit();
        ((MainActivity) getActivity()).imageviewVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).CartVisibility(View.VISIBLE);
        // ((PayUmoneyActivity) getActivity()).WishVisibility(View.VISIBLE);
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("responseCode : " + responseCode);
        productlist = new ArrayList<BeanProduct>();
        JSONObject object;
        switch (responseCode) {
            case Success:

                try {
                    object = new JSONObject(AppConstant.SEARCH_LIST);

                    if (object.getInt("count") == 0) {
                        dataWrite.showToast(getString(R.string.product_not_found));
                        progressDialog.dismiss();

                    } else {

                        JSONObject jsonObject = object.optJSONObject("data");
                        JSONArray jsonArray = jsonObject.optJSONArray("products");

                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object1 = jsonArray.optJSONObject(j);

                            BeanProduct bean = new BeanProduct();
                            bean.setProduct_id(object1.getString("product_id"));
                            bean.setTitle(object1.getString("title"));
                            bean.setQty(object1.getString("qty"));
                            bean.setDescription(object1.getString("description"));
                            bean.setImage(object1.getString("image"));
                            bean.setCount(object.getString("count"));
                            bean.setStatus(object1.getString("status"));

                            bean.setParant_category_id(object1.getString("parent_category_id"));
                            bean.setRating(object1.getString("rating"));
                            bean.setCurrency_symbol(object1.getString("currency_symbol"));

                            if (object1.has("regular_price")) {
                                bean.setRegular_price(object1.getString("regular_price"));
                            }
                            if (object1.has("sale_price")) {
                                bean.setSale_price(object1.getString("sale_price"));
                            }
                            if (object1.has("on_sale")) {
                                bean.setOn_sale(object1.getString("on_sale"));
                            }


                            if (object1.has("tax_product_with_price_regula_max")) {
                                bean.setTax_product_with_price_regula_max(object1.getString("tax_product_with_price_regula_max"));
                            }
                            if (object1.has("tax_product_with_price_regula_min")) {
                                bean.setTax_product_with_price_regula_min(object1.getString("tax_product_with_price_regula_min"));
                            }
                            if (object1.has("tax_product_with_price_sale_max")) {
                                bean.setTax_product_with_price_sale_max(object1.getString("tax_product_with_price_sale_max"));
                            }

                            if (object1.has("tax_product_with_price_sale_min")) {
                                bean.setTax_product_with_price_sale_min(object1.getString("tax_product_with_price_sale_min"));
                            }

                            if (object1.has("regular_tax_price")) {
                                bean.setRegular_tax_price(object1.getString("regular_tax_price"));
                            }

                            if (object1.has("sale_tax_price")) {
                                bean.setSale_tax_price(object1.getString("sale_tax_price"));
                            }


                            if (object1.has("tax_product_with_price_regula")) {
                                bean.setRegular_tax_price(object1.getString("tax_product_with_price_regula"));
                            }

                            if (object1.has("tax_product_with_price_sale")) {
                                bean.setSale_tax_price(object1.getString("tax_product_with_price_sale"));
                            }


                            productlist.add(bean);

                        }


                        ProdutAdapter albumsAdapter = new ProdutAdapter(productlist);
                        recyclerView.setAdapter(albumsAdapter);
                        progressDialog.dismiss();
                    }


                } catch (JSONException e) {
                    // TODO: handle exception

                    AppDebugLog.println("error =" + e);
                    progressDialog.dismiss();
                }

                break;

            case ServerError:
                appData.showUserAlert(getActivity(), getString(R.string.alert_title_message),
                        getString(R.string.alert_body_server_error), null);
                break;
        }
    }

    @Override
    public void timeOut() {

    }

    @Override
    public void codeError(int code) {

    }

    @Override
    public void percentageDownloadCompleted(int percentage, Object record) {

    }


    public class ProdutAdapter extends RecyclerView.Adapter<ProdutAdapter.MyViewHolder> {

        private Context mContext;
        ArrayList<BeanProduct> bean_list;
        List<Integer> mItems;
        int mCurrentItemId = 0;

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
                regular_price.setPaintFlags(regular_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }
        }


        public ProdutAdapter(ArrayList<BeanProduct> productlist) {

            this.bean_list = productlist;
            mItems = new ArrayList<Integer>(productlist.size());
            for (int i = 0; i < productlist.size(); i++) {
                addItem(i);
            }
        }

        public void addItem(int position) {
            //Log.d("ItemID", "========="+position);
            final int id = mCurrentItemId++;
            mItems.add(position, id);
            notifyItemInserted(position);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inflater_product, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder vh, int i) {
            final int index = i;
            //final int itemId = bean_list.get(i).getProduct_id().indexOf;
            final int itemId = mItems.get(i);


            if (dataWrite.isWishExist(bean_list.get(i).getProduct_id())) {
                vh.icWish.setImageResource(R.drawable.ic_heart_red);
            } else {
                vh.icWish.setImageResource(R.drawable.ic_heart);
            }

            try {
                if (bean_list.get(i).getRegular_tax_price().equals("")) {

                    if (bean_list.get(i).getSale_price().equals("")) {
                        vh.regular_price.setVisibility(View.INVISIBLE);
                        vh.offers.setVisibility(View.INVISIBLE);
                        vh.sale_price.setText(bean_list.get(i).getRegular_price());

                    } else {

                        vh.offers.setVisibility(View.VISIBLE);
                        vh.regular_price.setVisibility(View.VISIBLE);
                        vh.regular_price.setPaintFlags(vh.regular_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        vh.regular_price.setText(bean_list.get(i).getRegular_price());
                        vh.sale_price.setText(bean_list.get(i).getSale_price());
                        vh.offers.setText(bean_list.get(i).getOn_sale());

                    }

                } else {

                    if (bean_list.get(i).getSale_tax_price().equals("")) {
                        vh.regular_price.setVisibility(View.INVISIBLE);
                        vh.offers.setVisibility(View.INVISIBLE);
                        vh.sale_price.setText(bean_list.get(i).getRegular_tax_price());


                    } else {

                        vh.offers.setVisibility(View.VISIBLE);
                        vh.regular_price.setVisibility(View.VISIBLE);
                        vh.regular_price.setPaintFlags(vh.regular_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        vh.regular_price.setText(bean_list.get(i).getRegular_tax_price());
                        vh.sale_price.setText(bean_list.get(i).getSale_tax_price());
                        vh.offers.setText(bean_list.get(i).getOn_sale());

                    }

                }
            } catch (NullPointerException e) {

                //dataWrite.showToast(""+e);
            }


            String rate = bean_list.get(i).getRating();


            if (rate.trim().length() == 0) {

            } else {
                String[] rating = rate.split(" ");
                float finalrate = Float.parseFloat(rating[0]);
                vh.ratingbar.setRating(finalrate);
            }


            vh.description.setText(bean_list.get(i).getTitle());


/*
            Picasso.with(getActivity())
                    .load(bean_list.get(i).getImage())
                    .into(vh.productImage);*/

            Glide.with(getActivity()).load(bean_list.get(i).getImage())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            vh.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            vh.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(vh.productImage);

            vh.rlProductmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AppConstant.PRODUCT_ID = productlist.get(index).getProduct_id();
                    AppConstant.productname = productlist.get(index).getTitle();
                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                    intent.putExtra("position", index);
                    startActivity(intent);
                }
            });


            vh.icWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id, product_name, description, price, image;
                    id = bean_list.get(index).getProduct_id();
                    product_name = bean_list.get(index).getTitle();
                    description = bean_list.get(index).getDescription();
                    price = vh.sale_price.getText().toString();
                    image = bean_list.get(index).getImage();

                    if (dataWrite.isWishExist(bean_list.get(index).getProduct_id())) {
                        vh.icWish.setImageResource(R.drawable.ic_heart);
                        dataWrite.deleteWish(bean_list.get(index).getProduct_id());
                        //remove_wishlist(bean_list.get(index).getProduct_id());

                    } else {

                        vh.icWish.setImageResource(R.drawable.ic_heart_red);
                        dataWrite.Insert_wish(id, product_name, description, price, image);
                        // wishlist(bean_list.get(index).getProduct_id());
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return bean_list.size();
        }
    }
}
