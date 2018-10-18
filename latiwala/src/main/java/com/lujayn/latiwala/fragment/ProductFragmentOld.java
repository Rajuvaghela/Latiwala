package com.lujayn.latiwala.fragment;/*
package com.lujayn.wootouch_V2.fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
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
import R;
import com.lujayn.wootouch_V2.activity.MainActivity;
import com.lujayn.wootouch_V2.activity.ProductDetailActivity;
import com.lujayn.wootouch_V2.adapter.GridSpacingItemDecoration;
import com.lujayn.wootouch_V2.bean.BeanProduct;
import com.lujayn.wootouch_V2.bean.BeanUserData;
import com.lujayn.wootouch_V2.bean.SettingOption;
import com.lujayn.wootouch_V2.common.AppConstant;
import com.lujayn.wootouch_V2.common.ApplicationData;

import com.lujayn.wootouch_V2.common.SessionManager;
import com.lujayn.wootouch_V2.common.Webservice;
import com.lujayn.wootouch_V2.custom.AppDebugLog;
import com.lujayn.wootouch_V2.database.DataWrite;
import com.lujayn.wootouch_V2.network.RequestTask;
import com.lujayn.wootouch_V2.network.RequestTaskDelegate;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

*/
/**
 * Created by Shailesh on 28/07/16.
 *//*

public class ProductFragment extends Fragment implements PayUmoneyActivity.OnBackPressedListener,
        RequestTaskDelegate {
    View rootView;
    GridLayout gridLayout;
    String catslug, catName, user_id, catID, _Activity, parentID, parentName;
    private ArrayList<BeanProduct> productlist;
    LayoutInflater inflater;
    SessionManager manager;
    private RecyclerView recyclerView;
    private AlertDialog progressDialog;
    TextView tvShowItems, tvReload, tvBacktoCategory;
    LinearLayout empty_view;
    DataWrite dataWrite;
    String version = "0";
    String status, country, state, postcode, city;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ApplicationData appData;
    SettingOption settingOption;
    String tax_based_on, page_view, baseAddress_country, baseAddress_state;
    int VIEWTYPE = 0;
    String color_back, color_font;
    RecyclerView.LayoutManager mLayoutManager;
    StaggeredGridLayoutManager _sGridLayoutManager;

    public ProductFragment() {
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
        manager = new SessionManager();
        settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(getActivity(), AppConstant.SETTING_OPTION);
        tax_based_on = settingOption.getData().getOptions().getTax_based_on();
        page_view = settingOption.getData().getOptions().getPage_view();
        color_back = settingOption.getData().getOptions().getToolbar_back_color();
        color_font = settingOption.getData().getOptions().getToolbar_title_color();
        VIEWTYPE = Integer.parseInt(settingOption.getData().getOptions().getProduct_view());

        if (VIEWTYPE == 0) {

            rootView = inflater.inflate(R.layout.activity_product2, container, false);
            tvShowItems = (TextView) rootView.findViewById(R.id.tvshowItems);
            tvShowItems.setTextColor(Color.parseColor(color_font));

        } else if (VIEWTYPE == 1) {
            rootView = inflater.inflate(R.layout.activity_product, container, false);
            tvShowItems = (TextView) rootView.findViewById(R.id.tvshowItems);

        } else if (VIEWTYPE == 2) {

            rootView = inflater.inflate(R.layout.activity_product, container, false);
            tvShowItems = (TextView) rootView.findViewById(R.id.tvshowItems);

        } else if (VIEWTYPE == 3) {
            rootView = inflater.inflate(R.layout.activity_product, container, false);
            tvShowItems = (TextView) rootView.findViewById(R.id.tvshowItems);

        }

        ((PayUmoneyActivity) getActivity()).setOnBackPressedListener(this);

        appData = ApplicationData.getSharedInstance();
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_product);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        progressDialog = new SpotsDialog(getActivity(), R.style.Custom);
        user_id = manager.getPreferences(getActivity(), AppConstant.USER_ID);
        status = manager.getPreferences(getActivity(), AppConstant.STATUS);


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
            // if (tax_based_on.equals("base")){
            country = baseAddress_country;
            state = baseAddress_state;
            postcode = "";
            city = "";
            */
/*}else {
                country = "";
                state= "";
                postcode= "";
                city = "";
            }*//*

        }


        // gridLayout =(GridLayout) rootView.findViewById(R.id.gridlayout);
        empty_view = (LinearLayout) rootView.findViewById(R.id.empty_list_view);
        //_sGridLayoutManager= new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        tvReload = (TextView) rootView.findViewById(R.id.tvReload);
        tvBacktoCategory = (TextView) rootView.findViewById(R.id.tvBackCategory);

        tvReload.setTextColor(Color.parseColor(color_font));
        tvReload.setBackgroundColor(Color.parseColor(color_back));

        tvBacktoCategory.setTextColor(Color.parseColor(color_font));
        tvBacktoCategory.setBackgroundColor(Color.parseColor(color_back));

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        if (VIEWTYPE == 0) {
            Log.d("TAG", "viewtype ==== " + VIEWTYPE);
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            RelativeLayout rlRootview = (RelativeLayout) rootView.findViewById(R.id.rlRootproduct2);
            rlRootview.setBackgroundColor(Color.parseColor(color_back));

        } else if (VIEWTYPE == 1) {

            mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL) {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    // Do not draw the divider
                }
            });
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        } else if (VIEWTYPE == 2) {
            //Log.d("TAG","viewtype ==== "+VIEWTYPE);
          */
/* GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
           layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
               @Override
               public int getSpanSize(int position) {
                   return position == 0 ? 2 : 1;
               }
           });
           recyclerView.setLayoutManager(layoutManager);*//*



            // mLayoutManager = new GridLayoutManager(getActivity(),2);
            //recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(5), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        } else if (VIEWTYPE == 3) {

            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

        }

        //catslug = getArguments().getString("catslug");
        //catName = getArguments().getString("catName");
        //catID = getArguments().getString("catID");
        //parentID = getArguments().getString("parentID");
        //parentName = getArguments().getString("parentName");
        //_Activity = getArguments().getString("activity");

        mSwipeRefreshLayout.setSoundEffectsEnabled(true);
        //mSwipeRefreshLayout.setDistanceToTriggerSync(200);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //  mSwipeRefreshLayout.setRefreshing(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //dataWrite.deletedProductList();

                        refreshPorductList("products");

                    }
                }, 2500);
            }


        });


        AppConstant.counter = "" + dataWrite.getCartCount();
        AppConstant.wish_counter = "" + dataWrite.getWishCount();

        ((PayUmoneyActivity) getActivity()).CartVisibility(View.VISIBLE);
        ((PayUmoneyActivity) getActivity()).WishVisibility(View.VISIBLE);

        PayUmoneyActivity.setCounter(AppConstant.counter);
        PayUmoneyActivity.setWishCounter(AppConstant.wish_counter);

        if (AppConstant.page_view.equals("shop")) {

            //AppDebugLog.println("catID======catName  "+AppConstant.catID+"     "+AppConstant.catName);
            ((PayUmoneyActivity) getActivity()).Title(getString(R.string.all_product));
            fetchProductList();

        } else {

            AppDebugLog.println("catID======catName  " + AppConstant.catID + "     " + AppConstant.catName);
            ((PayUmoneyActivity) getActivity()).Title(AppConstant.catName);
            fetchProductList();
        }


        //prodcutlistnew("products");

        // displayProduct("product");


        tvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshPorductList("products");
            }
        });

        tvBacktoCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AppConstant._Activty.equals("subcategory")) {

                    AppConstant.catID = AppConstant.sub_catID;
                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        getFragmentManager().popBackStack();
                    } else {

                    }


                    String title = AppConstant.catName;
                    ((PayUmoneyActivity) getActivity()).Title(title);

                } else if (AppConstant._Activty.equals("category")) {

                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        getFragmentManager().popBackStack();
                    } else {

                    }

                    String title = getString(R.string.title_category);
                    ((PayUmoneyActivity) getActivity()).Title(title);

                }
            }
        });

        return rootView;
    }


    private void refreshPorductList(String products) {

        progressDialog.show();
        JSONObject jsonObject = new JSONObject();

        try {

            if (AppConstant.page_view.equals("shop")) {

            } else {
                jsonObject.put("catslug", AppConstant.catslug);
            }

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
        requestTask.execute(requestURL, "products", jsonObject.toString());

    }


    private void fetchProductList() {

        if (VIEWTYPE == 2) {
            _sGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(_sGridLayoutManager);

        }

        mSwipeRefreshLayout.setRefreshing(false);
        appData.getResponseproduct().clear();
        String data;
        if (AppConstant.page_view.equals("shop")) {
            data = dataWrite.fetchProductList(AppConstant.shopview);
        } else {
            data = dataWrite.fetchProductList(AppConstant.catslug);
        }

        // AppDebugLog.println("appData.getResponseproduct().get(0) : " + appData.getResponseproduct().get(0));

        if (data.length() == 0) {

            refreshPorductList("products");

        } else {

            productlist = new ArrayList<BeanProduct>();
            JSONObject object;
            try {
                object = new JSONObject(data);

                if (object.getInt("success") == 1) {
                    JSONObject jsonObject = object.optJSONObject("data");
                    JSONArray jsonArray = jsonObject.optJSONArray("products");

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject object1 = jsonArray.optJSONObject(j);

                        // if (AppConstant.catID.equals(object1.getString("parent_category_id"))){

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
                        bean.setWish("0");


                       */
/* if (object1.has("product_url")){
                            bean.setProduct_url(object1.getString("product_url"));
                        }
                        if (object1.has("button_text")){
                            bean.setButton_text(object1.getString("button_text"));
                        }*//*


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

                        */
/*}else {

                        }*//*



                    }
*/
/*
                            TextView productName, sale_price, regular_price, qty, offers, description;
                            ImageView productImage, iv_wish;
                            RatingBar ratingbar;
                            RelativeLayout rlProductmain;
                            boolean wish_flag = false;

                            if (productlist.size() == 0) {
                                Toast.makeText(ApplicationContext.getAppContext(), "product is empty", Toast.LENGTH_SHORT).show();

                            } else {

                            for (int i = 0; i < productlist.size(); i++) {
                                final View view = inflater.inflate(R.layout.inflater_product, null);
                                sale_price = (TextView) view.findViewById(R.id.tvPrice_product);
                                regular_price = (TextView) view.findViewById(R.id.tvRegularPrice);
                                description = (TextView) view.findViewById(R.id.tvProductdescription);
                                offers = (TextView) view.findViewById(R.id.tvOffers_product);
                                productImage = (ImageView) view.findViewById(R.id.ivProductImage);
                                iv_wish = (ImageView) view.findViewById(R.id.ivWish);
                                ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
                                rlProductmain = (RelativeLayout)view.findViewById(R.id.rlProduct);

                                regular_price.setPaintFlags(regular_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                imageViews.add(iv_wish);

                                final int index = i;
                                if (productlist.get(i).getRegular_price().equals("")) {
                                    regular_price.setVisibility(View.INVISIBLE);
                                    offers.setVisibility(View.INVISIBLE);
                                } else {
                                    offers.setVisibility(View.VISIBLE);
                                    regular_price.setVisibility(View.VISIBLE);
                                    regular_price.setText("$ " + productlist.get(i).getRegular_price());

                                    double regularprice = Double.parseDouble(productlist.get(i).getRegular_price());

                                    // same like that getting value from ePercent and parsing to double
                                    double salePrice = Double.parseDouble(productlist.get(i).getSale_price());

                                    double p = regularprice - salePrice;
                                    int percentage = (int) ((p / regularprice) * 100);

                                    offers.setText(String.valueOf(percentage) + "% OFF");
                                }


                                if (productlist.get(i).getWishlist_id().equals("1")) {

                                    iv_wish.setImageResource(R.drawable.ic_heart_red);


                                } else {
                                    iv_wish.setImageResource(R.drawable.iv_heart);

                                }

                                String rate = productlist.get(i).getRating();


                                if (rate.trim().length() == 0) {

                                } else {
                                    String[] rating = rate.split(" ");
                                    float finalrate = Float.parseFloat(rating[0]);
                                    ratingbar.setRating(finalrate);
                                }

                                sale_price.setText("$ " + productlist.get(i).getSale_price());
                                description.setText(productlist.get(i).getDescription());


                                URL image_string;
                                try {
                                    image_string = new URL(productlist.get(i).getImage());
                                    Log.d("TEST", "image_string is ==" + image_string);
                                    ImageLoader imageLoader = new ImageLoader(getActivity());
                                    imageLoader.DisplayImage(image_string.toString(), productImage);

                                } catch (MalformedURLException e) {
                                    Log.d("TEST", "ERROR is ==" + e);
                                    e.printStackTrace();
                                }

                                final ImageView finalIv_wish = iv_wish;


                                iv_wish.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("TEST", "onclick is ==" + index);
                                        Log.d("TEST", "onclick productlist is ==" + productlist.get(index).getTitle());
                                        if (productlist.get(index).getWishlist_id().equals("0")) {
                                            finalIv_wish.setImageResource(R.drawable.ic_heart_red);
                                            imageViews.get(index).setImageResource(R.drawable.ic_heart_red);
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("user_id", user_id);
                                                jsonObject.put("product_id", productlist.get(index).getProduct_id());

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            Intent intent = new Intent(getActivity(), Intent_Class_Data.class);
                                            intent.putExtra("url", Webservice.URL_WISHLIST_ADD);
                                            intent.putExtra("type", "wishlist");
                                            intent.putExtra("Data", jsonObject.toString());
                                            getActivity().startService(intent);

                                        } else {
                                            finalIv_wish.setImageResource(R.drawable.iv_heart);
                                            imageViews.get(index).setImageResource(R.drawable.iv_heart);
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("user_id", user_id);
                                                jsonObject.put("product_id", productlist.get(index).getProduct_id());

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                            Intent intent = new Intent(getActivity(), Intent_Class_Data.class);
                                            intent.putExtra("url", Webservice.URL_WISHLIST_REMOVE);
                                            intent.putExtra("type", "wishlist_remove");
                                            intent.putExtra("Data", jsonObject.toString());
                                            getActivity().startService(intent);

                                        }

                                    }

                                });


                                rlProductmain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Log.d("TEST", "onClick gridLayout====== "+index);
                                        Log.d("TEST", "onClick gridLayout====== "+productlist.get(index).getTitle());

                                        Fragment fr=new ProductDetailFragment();
                                        String title = productlist.get(index).getTitle();
                                        FragmentManager fm=getFragmentManager();
                                        FragmentTransaction ft=fm.beginTransaction();
                                        Bundle args = new Bundle();
                                        args.putString("product_id", productlist.get(index).getProduct_id());
                                        args.putString("product_name",productlist.get(index).getTitle());
                                        fr.setArguments(args);
                                        ft.replace(R.id.container_body, fr);
                                        ft.commit();
                                        ((PayUmoneyActivity) getActivity()).Title(title);
                                    }
                                });

                                gridLayout.addView(view);
                            }
                        }*//*


                            */
/*ProductAdapter productAdapter = new ProductAdapter(productlist);
                            recyclerView.setAdapter(productAdapter);*//*


                    if (productlist.size() == 0) {

                        dataWrite.showToast(getString(R.string.product_not_found));
                        empty_view.setVisibility(View.VISIBLE);
                    } else {
                        //AppDebugLog.println("appData.getResponseproduct().get(0)===================="+productlist.size());
                        empty_view.setVisibility(View.GONE);
                        tvShowItems.setText(getString(R.string.showing) + " " + productlist.size() + " " + getString(R.string.items));
                        ProdutAdapter albumsAdapter = new ProdutAdapter(productlist);
                        recyclerView.setAdapter(albumsAdapter);
                        albumsAdapter.notifyDataSetChanged();

                    }

                    progressDialog.dismiss();

                } else {

                    //dataWrite.showToast("unknown error");
                    progressDialog.dismiss();
                }


            } catch (JSONException e) {
                // TODO: handle exception

                AppDebugLog.println("error =" + e);
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // getActivity().registerReceiver(receive, new IntentFilter(Intent_Class_Data.NOTIFICATION));

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //  getActivity().unregisterReceiver(receive);

    }

    @Override
    public void doBack() {


        if (AppConstant._Activty.equals("subcategory")) {

            AppConstant.catID = AppConstant.sub_catID;
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {

            }

            String title = AppConstant.catName;
            ((PayUmoneyActivity) getActivity()).Title(title);

        } else if (AppConstant._Activty.equals("category")) {

            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            } else {

            }

            String title = getString(R.string.title_category);
            ((PayUmoneyActivity) getActivity()).Title(title);

        } else {


            Fragment fr = new CategoryFragment();
            String title = getString(R.string.title_category);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container_body, fr);
            ft.commit();
            ((PayUmoneyActivity) getActivity()).Title(title);
        }


    }

    */
/**
     * Converting dp to pixel
     *//*

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("responseCode : " + responseCode);

        switch (responseCode) {
            case product:

                //Log.d("TEST", "product list==== ");

                if (AppConstant.page_view.equals("shop")) {
                    dataWrite.Insert_ProductList(AppConstant.shopview, appData.getResponseproduct().get(0));
                } else {
                    dataWrite.Insert_ProductList(AppConstant.catslug, appData.getResponseproduct().get(0));
                }

                // dataWrite.Insert_ProductList1(appData.getResponseproduct().get(0));
                fetchProductList();
                break;

            case NetworkError:
                progressDialog.dismiss();
                appData.showUserAlert(getActivity(), getString(R.string.alert_title_message),
                        getString(R.string.alert_body_network_error), null);
                break;

            case ServerError:
                progressDialog.dismiss();
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
        private static final int LIST_TYPE_0 = 0;
        private static final int LIST_TYPE_1 = 1;
        private static final int LIST_TYPE_2 = 2;
        private static final int LIST_TYPE_3 = 3;
        private static final int SMALLVIEW = 4;
        List<Integer> mItems;
        int mCurrentItemId = 0;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView productName, sale_price, regular_price, qty, offers, description;
            ImageView productImage, icWish;
            RatingBar ratingbar;
            RelativeLayout rlProductmain;
            ProgressBar progressBar;


            public MyViewHolder(View view, int viewType) {
                super(view);
                //Log.d("ItemID", "========="+viewType);
                switch (viewType) {

                    case LIST_TYPE_0:
                        sale_price = (TextView) view.findViewById(R.id.tvPrice_product);
                        regular_price = (TextView) view.findViewById(R.id.tvRegularPrice);
                        description = (TextView) view.findViewById(R.id.tvProductdescription);
                        offers = (TextView) view.findViewById(R.id.tvOffers_product);
                        productImage = (ImageView) view.findViewById(R.id.ivProductImage);
                        progressBar = (ProgressBar) view.findViewById(R.id.pbarProduct);
                        ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
                        rlProductmain = (RelativeLayout) view.findViewById(R.id.rlProduct);

                        icWish = (ImageView) view.findViewById(R.id.icWish_product);

                        break;
                    case LIST_TYPE_1:

                        sale_price = (TextView) view.findViewById(R.id.tvPrice_product);
                        regular_price = (TextView) view.findViewById(R.id.tvRegularPrice);
                        description = (TextView) view.findViewById(R.id.tvProductdescription);
                        offers = (TextView) view.findViewById(R.id.tvOffers_product);
                        productImage = (ImageView) view.findViewById(R.id.ivProductImage);
                        progressBar = (ProgressBar) view.findViewById(R.id.pbarProduct);
                        ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
                        rlProductmain = (RelativeLayout) view.findViewById(R.id.rlProduct);

                        icWish = (ImageView) view.findViewById(R.id.icWish_product);

                        break;

                    case LIST_TYPE_2:

                        sale_price = (TextView) view.findViewById(R.id.tvPrice_product);
                        regular_price = (TextView) view.findViewById(R.id.tvRegularPrice);
                        description = (TextView) view.findViewById(R.id.tvProductdescription);
                        offers = (TextView) view.findViewById(R.id.tvOffers_product);
                        productImage = (ImageView) view.findViewById(R.id.ivProductImage);
                        progressBar = (ProgressBar) view.findViewById(R.id.pbarProduct);
                        ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
                        rlProductmain = (RelativeLayout) view.findViewById(R.id.rlProduct);
                        icWish = (ImageView) view.findViewById(R.id.icWish_product);

                        //rlProductmain.getLayoutParams().height = rlProductmain.getHeight()-30;

                        break;
                    case LIST_TYPE_3:
                        sale_price = (TextView) view.findViewById(R.id.tvPrice_product);
                        regular_price = (TextView) view.findViewById(R.id.tvRegularPrice);
                        description = (TextView) view.findViewById(R.id.tvProductdescription);
                        offers = (TextView) view.findViewById(R.id.tvOffers_product);
                        productImage = (ImageView) view.findViewById(R.id.ivProductImage);
                        progressBar = (ProgressBar) view.findViewById(R.id.pbarProduct);
                        ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
                        rlProductmain = (RelativeLayout) view.findViewById(R.id.rlProduct);

                        icWish = (ImageView) view.findViewById(R.id.icWish_product);

                        break;

                    case SMALLVIEW:

                        sale_price = (TextView) view.findViewById(R.id.tvPrice_product);
                        regular_price = (TextView) view.findViewById(R.id.tvRegularPrice);
                        description = (TextView) view.findViewById(R.id.tvProductdescription);
                        offers = (TextView) view.findViewById(R.id.tvOffers_product);
                        productImage = (ImageView) view.findViewById(R.id.ivProductImage);
                        progressBar = (ProgressBar) view.findViewById(R.id.pbarProduct);
                        ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
                        rlProductmain = (RelativeLayout) view.findViewById(R.id.rlProduct);
                        icWish = (ImageView) view.findViewById(R.id.icWish_product);

                        //rlProductmain.getLayoutParams().height = rlProductmain.getHeight()-30;

                        break;

                }


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
        public int getItemViewType(int position) {
            //Log.d("ItemID", "========="+VIEWTYPE);
            switch (VIEWTYPE) {
                case 0:
                    return LIST_TYPE_0;
                case 1:
                    return LIST_TYPE_1;
                case 2:

                    if (mItems.get(position) == 0) {
                        return SMALLVIEW;
                    } else {
                        return LIST_TYPE_2;
                    }

                case 3:
                    return LIST_TYPE_3;

                default:
                    return -1;
            }

        }

        @Override
        public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            //Log.d("ItemID", "========="+viewType);
            switch (viewType) {

                case LIST_TYPE_0:

                    View itemView0 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product2, parent, false);


                    //MyViewHolder viewHolder = new MyViewHolder(itemView);
                    return new MyViewHolder(itemView0, viewType);
                case LIST_TYPE_1:
                    View itemView1 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product3, parent, false);

                    //MyViewHolder viewHolder = new MyViewHolder(itemView);
                    return new MyViewHolder(itemView1, viewType);

                case LIST_TYPE_2:
                    final View itemView2 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product, parent, false);
                    */
/*itemView2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            final ViewGroup.LayoutParams lp = itemView2.getLayoutParams();

                            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                                StaggeredGridLayoutManager.LayoutParams sglp =
                                        (StaggeredGridLayoutManager.LayoutParams) lp;

                                itemView2.setLayoutParams(sglp);
                                final StaggeredGridLayoutManager lm =
                                        (StaggeredGridLayoutManager) ((RecyclerView) parent).getLayoutManager();
                                lm.invalidateSpanAssignments();
                            }


                            itemView2.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        }
                    });*//*

                    return new MyViewHolder(itemView2, viewType);
                case LIST_TYPE_3:
                    View itemView3 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product, parent, false);

                    return new MyViewHolder(itemView3, viewType);
                case SMALLVIEW:
                    final View itemView4 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product_smallview, parent, false);

                  */
/*  itemView4.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            final ViewGroup.LayoutParams lp = itemView4.getLayoutParams();

                            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                                StaggeredGridLayoutManager.LayoutParams sglp =
                                        (StaggeredGridLayoutManager.LayoutParams) lp;
                                //sglp.width = itemView4.getHeight() / 2;
                                itemView4.setLayoutParams(sglp);

                                final StaggeredGridLayoutManager lm =
                                        (StaggeredGridLayoutManager) ((RecyclerView) parent).getLayoutManager();
                                lm.invalidateSpanAssignments();
                            }


                            itemView4.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        }
                    });*//*


                    return new MyViewHolder(itemView4, viewType);

            }

            return null;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder vh, final int i) {
            //Log.d("ItemID", "========="+ i);

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

                // dataWrite.showToast(""+e);
            }


            String rate = bean_list.get(i).getRating();


            if (rate.trim().length() == 0) {

            } else {
                String[] rating = rate.split(" ");
                float finalrate = Float.parseFloat(rating[0]);
                vh.ratingbar.setRating(finalrate);
            }
            vh.description.setText(bean_list.get(i).getTitle());

            */
/*if (bean_list.get(index).getProduct_url()!=null){
                vh.description.setText(bean_list.get(i).getButton_text());
            }else {
                vh.description.setText(bean_list.get(i).getTitle());
            }*//*


           */
/* Picasso.with(getActivity())
                    .load(bean_list.get(i).getImage())
                    .into(vh.productImage);*//*


            Glide.with(getActivity()).load(bean_list.get(i).getImage())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model,
                                                   Target<GlideDrawable> target, boolean isFirstResource) {
                            vh.progressBar.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            vh.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(vh.productImage);


            vh.rlProductmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                */
/*    if (bean_list.get(index).getProduct_url()!=null){

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bean_list.get(index).getProduct_url()));
                        startActivity(browserIntent);

                    }*//*


                    AppConstant.PRODUCT_ID = bean_list.get(index).getProduct_id();
                    AppConstant.productname = bean_list.get(index).getTitle();

                    startActivity(new Intent(getActivity(), ProductDetailActivity.class));
                    */
/*
                    Fragment fr=new ProductDetailFragment();
                    String title = bean_list.get(index).getTitle();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    Fragment current = fm.findFragmentByTag(getString(R.string.title_product_detail));
                    if (current!=null){
                        ft.remove(current);

                    }
                    ft.addToBackStack(getString(R.string.title_product_detail));
                    ft.replace(R.id.container_body, fr);
                    ft.commit();
                    ((PayUmoneyActivity) getActivity()).Title("");*//*


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

   */
/*     @Override
        public int getItemViewType(int position) {
            return position % 2 * 2;
        }*//*



      */
/*  @Override
        public long getItemId(int position) {
            Log.d("ItemID", "========="+position);
            return position;
        }*//*

    }

    private void wishlist(String product_id) {

    }

    private void remove_wishlist(String product_id) {

    }

}
*/
