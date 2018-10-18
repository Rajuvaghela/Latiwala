package com.lujayn.latiwala.fragment;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.activity.MainActivity;
import com.lujayn.latiwala.activity.ProductDetailActivity;
import com.lujayn.latiwala.adapter.GridSpacingItemDecoration;
import com.lujayn.latiwala.bean.BeanProduct;
import com.lujayn.latiwala.bean.BeanSearchProduct;
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

import cn.refactor.lib.colordialog.PromptDialog;
import dmax.dialog.SpotsDialog;

/**
 * Created by Shailesh on 28/07/16.
 */
public class BeautyFragment extends Fragment implements MainActivity.OnBackPressedListener,
        RequestTaskDelegate {
    View rootView;
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
    Context activity;
    //  LinearLayout ll_back;
    // TextView tvCounter;
    //  RelativeLayout rlCounter_toolbar;
    ArrayList<BeanSearchProduct> beanSearchProducts = new ArrayList<>();
    //  Dialog search_dialog;
    //  AdapterSearch adapterSearch;
    //  ImageView iv_right_search;


    public BeautyFragment() {

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
        try {
            activity = getActivity();
            manager = new SessionManager();
            settingOption = new SettingOption();
            settingOption = manager.getPreferencesOption(activity, AppConstant.SETTING_OPTION);
            tax_based_on = settingOption.getData().getOptions().getTax_based_on();
            page_view = settingOption.getData().getOptions().getPage_view();
            color_back = settingOption.getData().getOptions().getToolbar_back_color();
            color_font = settingOption.getData().getOptions().getToolbar_title_color();
            VIEWTYPE = Integer.parseInt(settingOption.getData().getOptions().getProduct_view());


            if (VIEWTYPE == 0) {

                rootView = inflater.inflate(R.layout.fragment_product, container, false);
                tvShowItems = (TextView) rootView.findViewById(R.id.tvshowItems);
                tvShowItems.setTextColor(Color.parseColor(color_font));

            } else if (VIEWTYPE == 1) {
                rootView = inflater.inflate(R.layout.fragment_product, container, false);
                tvShowItems = (TextView) rootView.findViewById(R.id.tvshowItems);

            } else if (VIEWTYPE == 2) {

                rootView = inflater.inflate(R.layout.fragment_product, container, false);
                tvShowItems = (TextView) rootView.findViewById(R.id.tvshowItems);

            } else if (VIEWTYPE == 3) {
                rootView = inflater.inflate(R.layout.fragment_product, container, false);
                tvShowItems = (TextView) rootView.findViewById(R.id.tvshowItems);

            }

            appData = ApplicationData.getSharedInstance();
            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_product);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
            progressDialog = new ProgressDialog(activity);
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
                    //Log.d("TAG","viewtype ==== "+VIEWTYPE);

                    // mLayoutManager = new GridLayoutManager(getActivity(),2);
                    //recyclerView.setLayoutManager(mLayoutManager);
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


            // gridLayout =(GridLayout) findViewById(R.id.gridlayout);
            empty_view = (LinearLayout) rootView.findViewById(R.id.empty_list_view);
            //_sGridLayoutManager= new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            tvReload = (TextView) rootView.findViewById(R.id.tvReload);
            tvBacktoCategory = (TextView) rootView.findViewById(R.id.tvBackCategory);
            final TextView tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            RelativeLayout rl_header = (RelativeLayout) rootView.findViewById(R.id.rl_header);
            rl_header.setBackgroundColor(Color.parseColor(color_back));


            tvReload.setTextColor(Color.parseColor(color_font));
            tvReload.setBackgroundColor(Color.parseColor(color_back));

            tvBacktoCategory.setTextColor(Color.parseColor(color_font));
            tvBacktoCategory.setBackgroundColor(Color.parseColor(color_back));

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);

            if (VIEWTYPE == 0) {
                Log.d("TAG", "viewtype ==== " + VIEWTYPE);
                mLayoutManager = new GridLayoutManager(activity, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                RelativeLayout rlRootview = (RelativeLayout) rootView.findViewById(R.id.rlRootproduct2);
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
            //mSwipeRefreshLayout.setDistanceToTriggerSync(200);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isNetworkAvailable()) {
                                refreshPorductList("products");
                            } else {
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


                tv_title.setText(getString(R.string.all_product));
                if (isNetworkAvailable()) {
                    refreshPorductList("products");
                } else {
                    showNoInternetPromptDlg();
                }

            } else {

                AppDebugLog.println("catID======catName  " + AppConstant.catID + "     " + AppConstant.catName);
                tv_title.setText(AppConstant.catName);
                if (isNetworkAvailable()) {
                    refreshPorductList("products");
                } else {
                    showNoInternetPromptDlg();
                }
            }


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
        } catch (Exception e) {
            Log.e("BeautyFragment", ":Exception++++++++++++++++++1");
            e.printStackTrace();
        }


        return rootView;
    }

    private void refreshPorductList(String products) {

        progressDialog.show();
        JSONObject jsonObject = new JSONObject();

        try {

            if (AppConstant.page_view.equals("shop")) {

            } else {
                jsonObject.put("catslug", "");
            }

            jsonObject.put("country", "");
            jsonObject.put("state", "");
            jsonObject.put("postcode", "");
            jsonObject.put("city", "");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        String requestURL = Webservice.BASE_URL + "" + Webservice.get_beauty;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, "products", jsonObject.toString());

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
//                    JSONObject jsonObject = object.optJSONObject("data");
                        JSONObject jsonObject = object.optJSONObject("products");
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
//                        bean.setCount(object.getString("count"));
                            bean.setStatus(object1.getString("status"));


                            bean.setParant_category_id(object1.getString("parent_category_id"));
                            bean.setRating(object1.getString("rating"));
                            bean.setCurrency_symbol(object1.getString("currency_symbol"));
                            bean.setWish("0");


                       /* if (object1.has("product_url")){
                            bean.setProduct_url(object1.getString("product_url"));
                        }
                        if (object1.has("button_text")){
                            bean.setButton_text(object1.getString("button_text"));
                        }*/

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
            Log.e("BeautyFragment", ":Exception++++++++++++++++++2");
            e.printStackTrace();
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
    public void doBack() {

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
        public ProdutAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            //Log.d("ItemID", "========="+viewType);
            switch (viewType) {

                case LIST_TYPE_0:

                    View itemView0 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product2, parent, false);


                    //MyViewHolder viewHolder = new MyViewHolder(itemView);
                    return new ProdutAdapter.MyViewHolder(itemView0, viewType);
                case LIST_TYPE_1:
                    View itemView1 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product2, parent, false);

                    //MyViewHolder viewHolder = new MyViewHolder(itemView);
                    return new ProdutAdapter.MyViewHolder(itemView1, viewType);

                case LIST_TYPE_2:
                    final View itemView2 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product2, parent, false);

                    return new ProdutAdapter.MyViewHolder(itemView2, viewType);
                case LIST_TYPE_3:
                    View itemView3 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product2, parent, false);

                    return new ProdutAdapter.MyViewHolder(itemView3, viewType);
                case SMALLVIEW:
                    final View itemView4 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_product2, parent, false);


                    return new ProdutAdapter.MyViewHolder(itemView4, viewType);

            }

            return null;
        }

        @Override
        public void onBindViewHolder(final ProdutAdapter.MyViewHolder vh, final int i) {
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
                        intent.putExtra("beanJewelleryProductList", bean_list);
                        intent.putExtra("parentClassName", "BeautyFragment");
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

                        } else {

                            vh.icWish.setImageResource(R.drawable.ic_heart_red);
                            dataWrite.Insert_wish(id, product_name, description, price, image);
                        }

                    }
                });
            } catch (Exception e) {
                Log.e("BeautyFragment", ":Exception++++++++++++++++++3");
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return bean_list.size();
        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showNoInternetPromptDlg() {
        new PromptDialog(getContext())
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