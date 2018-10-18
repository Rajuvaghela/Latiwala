package com.lujayn.latiwala.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.adapter.AdapterSearch;
import com.lujayn.latiwala.adapter.GridSpacingItemDecoration;
import com.lujayn.latiwala.bean.BeanProduct;
import com.lujayn.latiwala.bean.BeanUserData;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationContext;
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

import cn.refactor.lib.colordialog.PromptDialog;

public class ProductActivity extends AppCompatActivity implements RequestTaskDelegate, View.OnClickListener {


    GridLayout gridLayout;
    String catslug, catName, user_id, catID, _Activity, parentID, parentName;
    private ArrayList<BeanProduct> productlist;
    LayoutInflater inflater;
    SessionManager manager;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
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
    Activity activity;
    LinearLayout ll_back;
    TextView tvCounter;
    RelativeLayout rlCounter_toolbar;
    Dialog search_dialog;
    AdapterSearch adapterSearch;
    ImageView iv_right_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            activity = this;
            manager = new SessionManager();
            settingOption = new SettingOption();
            settingOption = manager.getPreferencesOption(activity, AppConstant.SETTING_OPTION);
            tax_based_on = settingOption.getData().getOptions().getTax_based_on();
            page_view = settingOption.getData().getOptions().getPage_view();
            color_back = settingOption.getData().getOptions().getToolbar_back_color();
            color_font = settingOption.getData().getOptions().getToolbar_title_color();
            VIEWTYPE = Integer.parseInt(settingOption.getData().getOptions().getProduct_view());

            if (VIEWTYPE == 0) {
                setContentView(R.layout.activity_product2);
                tvShowItems = (TextView) findViewById(R.id.tvshowItems);
                tvShowItems.setTextColor(Color.parseColor(color_font));

            } else if (VIEWTYPE == 1) {
                setContentView(R.layout.activity_product2);
                tvShowItems = (TextView) findViewById(R.id.tvshowItems);

            } else if (VIEWTYPE == 2) {
                setContentView(R.layout.activity_product2);
                tvShowItems = (TextView) findViewById(R.id.tvshowItems);

            } else if (VIEWTYPE == 3) {
                setContentView(R.layout.activity_product2);
                tvShowItems = (TextView) findViewById(R.id.tvshowItems);

            }


            appData = ApplicationData.getSharedInstance();
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_product);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading..");
            user_id = manager.getPreferences(activity, AppConstant.USER_ID);
            status = manager.getPreferences(activity, AppConstant.STATUS);


            String b = settingOption.getData().getOptions().getBase_location().getWoocommerce_default_country();

            String base[] = b.split(":");
            if (base.length > 1) {
                baseAddress_country = base[0];
                baseAddress_state = base[1];
            } else {
                baseAddress_country = base[0];
            }

            dataWrite = new DataWrite(activity);
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
            }


            empty_view = (LinearLayout) findViewById(R.id.empty_list_view);
            tvReload = (TextView) findViewById(R.id.tvReload);
            tvBacktoCategory = (TextView) findViewById(R.id.tvBackCategory);
            final TextView tv_title = (TextView) findViewById(R.id.tv_title);
            tvCounter = (TextView) findViewById(R.id.tvCounter);
            RelativeLayout rl_header = (RelativeLayout) findViewById(R.id.rl_header);
            ll_back = (LinearLayout) findViewById(R.id.ll_back);
            rlCounter_toolbar = (RelativeLayout) findViewById(R.id.rlCounter_toolbar);
            rl_header.setBackgroundColor(Color.parseColor(color_back));
            iv_right_search = (ImageView) findViewById(R.id.iv_right_search);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor(color_back));
            }

            tvReload.setTextColor(Color.parseColor(color_font));
            tvReload.setBackgroundColor(Color.parseColor(color_back));

            tvBacktoCategory.setTextColor(Color.parseColor(color_font));
            tvBacktoCategory.setBackgroundColor(Color.parseColor(color_back));

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            if (VIEWTYPE == 0) {
                Log.d("TAG", "viewtype ==== " + VIEWTYPE);
                mLayoutManager = new GridLayoutManager(activity, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                RelativeLayout rlRootview = (RelativeLayout) findViewById(R.id.rlRootproduct2);
                rlRootview.setBackgroundColor(Color.parseColor(color_back));

            } else if (VIEWTYPE == 1) {
                mLayoutManager = new GridLayoutManager(activity, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            } else if (VIEWTYPE == 2) {
                mLayoutManager = new GridLayoutManager(activity, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

            } else if (VIEWTYPE == 3) {

                mLayoutManager = new GridLayoutManager(activity, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }


            mSwipeRefreshLayout.setSoundEffectsEnabled(true);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isNetworkAvailable()) {
                                refreshPorductList("products");
                            } else {
                                mSwipeRefreshLayout.setRefreshing(false);
                                showNoInternetPromptDlg();
                            }


                        }
                    }, 2500);
                }


            });


            AppConstant.counter = "" + dataWrite.getCartCount();
            AppConstant.wish_counter = "" + dataWrite.getWishCount();

            MainActivity.setCounter(AppConstant.counter);
            MainActivity.setWishCounter(AppConstant.wish_counter);

            if (AppConstant.page_view.equals("shop")) {

                //AppDebugLog.println("catID======catName  "+AppConstant.catID+"     "+AppConstant.catName);
                //((PayUmoneyActivity) getActivity()).Title(getString(R.string.all_product));
                tv_title.setText(getString(R.string.all_product));
                fetchProductList();

            } else {

                AppDebugLog.println("catID======catName  " + AppConstant.catID + "     " + AppConstant.catName);
                tv_title.setText(AppConstant.catName);
                fetchProductList();
            }


            //prodcutlistnew("products");

            // displayProduct("product");


            tvReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isNetworkAvailable()) {
                        refreshPorductList("products");
                    } else {
                        showNoInternetPromptDlg();
                    }

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
                        tv_title.setText(title);
                        //   ((PayUmoneyActivity) getActivity()).Title(title);

                    } else if (AppConstant._Activty.equals("category")) {

                        if (getFragmentManager().getBackStackEntryCount() > 0) {
                            getFragmentManager().popBackStack();
                        } else {

                        }

                        String title = getString(R.string.title_category);
                        tv_title.setText(title);
                        //    ((PayUmoneyActivity) getActivity()).Title(title);

                    }
                }
            });
            ll_back.setOnClickListener(this);
            rlCounter_toolbar.setOnClickListener(this);
            iv_right_search.setOnClickListener(this);
        } catch (Exception e) {
            Log.e("ProductActivity", ":Exception++++++++++++++++++1");
            e.printStackTrace();
        }


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


    @Override
    protected void onStart() {
        super.onStart();
        setCounter(AppConstant.counter);
    }

    private void fetchProductList() {

        try {
            if (VIEWTYPE == 2) {
                mLayoutManager = new GridLayoutManager(activity, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

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
                if (isNetworkAvailable()) {
                    refreshPorductList("products");
                } else {
                    showNoInternetPromptDlg();
                }


            } else {

                productlist = new ArrayList<BeanProduct>();
                JSONObject object;
                try {
                    object = new JSONObject(data);

                    if (object.getInt("success") == 1) {
                        JSONObject jsonObject = object.optJSONObject("products");
                        JSONArray jsonArray = jsonObject.optJSONArray("products");

                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject object1 = jsonArray.optJSONObject(j);


                            BeanProduct bean = new BeanProduct();
                            bean.setProduct_id(object1.getString("product_id"));
                            bean.setTitle(object1.getString("title"));
                            bean.setQty(object1.getString("qty"));
                            bean.setDescription(object1.getString("description"));
                            bean.setImage(object1.getString("image"));
                            bean.setStatus(object1.getString("status"));


                            bean.setParant_category_id(object1.getString("parent_category_id"));
                            bean.setRating(object1.getString("rating"));
                            bean.setCurrency_symbol(object1.getString("currency_symbol"));
                            bean.setWish("0");


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
        } catch (Exception e) {
            Log.e("ProductActivity", ":Exception++++++++++++++++++2");
            e.printStackTrace();
        }


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
                appData.showUserAlert(activity, getString(R.string.alert_title_message),
                        getString(R.string.alert_body_network_error), null);
                break;

            case ServerError:
                progressDialog.dismiss();
                appData.showUserAlert(activity, getString(R.string.alert_title_message),
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;

            case R.id.rlCounter_toolbar:
                startActivity(new Intent(activity, CartActivity.class));
                break;
            case R.id.iv_right_search:
                SearchDialogOpen();
                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                            .inflate(R.layout.inflater_product2, parent, false);

                    //MyViewHolder viewHolder = new MyViewHolder(itemView);
                    return new MyViewHolder(itemView1, viewType);

                case LIST_TYPE_2:
                    final View itemView2 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product2, parent, false);

                    return new MyViewHolder(itemView2, viewType);
                case LIST_TYPE_3:
                    View itemView3 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product2, parent, false);

                    return new MyViewHolder(itemView3, viewType);
                case SMALLVIEW:
                    final View itemView4 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product2, parent, false);


                    return new MyViewHolder(itemView4, viewType);

            }

            return null;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder vh, final int i) {

            try {

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


                Glide.with(activity).load(bean_list.get(i).getImage())
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
                        AppConstant.PRODUCT_ID = bean_list.get(index).getProduct_id();
                        AppConstant.productname = bean_list.get(index).getTitle();
                        Intent intent = new Intent(activity, ProductDetailActivity.class);
                        intent.putExtra("beanProductActivityList", bean_list);
                        intent.putExtra("parentClassName", "ProductActivity");
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
            } catch (Exception e) {
                Log.e("ProductActivity", ":Exception++++++++++++++++++3");
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return bean_list.size();
        }
    }


    public void setCounter(String counter) {
        if (counter.equals("0")) {
            tvCounter.setVisibility(View.GONE);
        } else {
            tvCounter.setVisibility(View.VISIBLE);
            tvCounter.setText(counter);
        }

    }

    private void SearchDialogOpen() {
        search_dialog = new Dialog(activity);
        search_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        search_dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        search_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        search_dialog.setContentView(R.layout.search_layout);

        Window window = search_dialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout ll_back = (LinearLayout) search_dialog.findViewById(R.id.ll_back);
        EditText et_search = (EditText) search_dialog.findViewById(R.id.et_search);
        RecyclerView rv_product_list = (RecyclerView) search_dialog.findViewById(R.id.rv_product_list);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_product_list.setLayoutManager(gridLayoutManager);
        adapterSearch = new AdapterSearch(ApplicationContext.beanSearchProducts, activity);
        rv_product_list.setAdapter(adapterSearch);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterSearch.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_dialog.dismiss();
            }
        });
        search_dialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showNoInternetPromptDlg() {
        new PromptDialog(this)
                .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setAnimationEnable(true)
                .setTitleText(getString(R.string.no_internet_connection))
                .setContentText(getString(R.string.please_try_againg))
                .setPositiveListener(getString(R.string.try_again), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                        initialize();
                    }
                }).show();
    }

    private void initialize() {
        if (isNetworkAvailable()) {
            refreshPorductList("products");
        } else {
            showNoInternetPromptDlg();
        }
    }

}