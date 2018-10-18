package com.lujayn.latiwala.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.activity.MainActivity;
import com.lujayn.latiwala.adapter.AdapterHomeProduct;
import com.lujayn.latiwala.adapter.GridSpacingItemDecoration;
import com.lujayn.latiwala.adapter.HomeSliderAdapter;
import com.lujayn.latiwala.bean.BeanHomeProduct;
import com.lujayn.latiwala.bean.BeanProduct;
import com.lujayn.latiwala.bean.BeanSearchProduct;
import com.lujayn.latiwala.bean.BeanSliderImage;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationData;
import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.common.Webservice;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.database.DataWrite;
import com.lujayn.latiwala.network.RequestTaskDelegate;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;
import dmax.dialog.SpotsDialog;

/**
 * Created by Shailesh on 27/07/16.
 */
public class HomeFragment extends Fragment implements MainActivity.OnBackPressedListener,
        RequestTaskDelegate, ViewPager.OnPageChangeListener, View.OnClickListener {
    View rootView;
    private Boolean exit = false;
    private ProgressDialog progressDialog;
    DataWrite dataWrite;
    String color_cate_shape, color_cate_name;
    int cate_viewtype = 0;
    SessionManager manager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ApplicationData appData;
    int screen_width, screen_height;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<BeanSearchProduct> beanSearchProducts = new ArrayList<>();
    ArrayList<BeanSliderImage> beanSliderImages = new ArrayList<>();
    ArrayList<BeanHomeProduct> beanHomeProducts = new ArrayList<>();

    //slider variable are as below
    ViewPager viewPagerTopSlider;
    HomeSliderAdapter homeSliderAdapter;
    Context activity;
    CirclePageIndicator circlePageIndicator;
    public int pos = 0;
    private final Handler handler = new Handler();
    public Runnable mRun;

    RequestQueue queue;
    private boolean isPageRefress = false;
    RecyclerView recyclerViewHomeProductList;
    private ArrayList<BeanProduct> productlist;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getContext();
        queue = Volley.newRequestQueue(activity);
        productlist = new ArrayList<BeanProduct>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        // beanSearchProducts = bundle.getParcelableArrayList("beanSearchProducts");
        rootView = inflater.inflate(R.layout.fragment_category, container, false);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        manager = new SessionManager();
        appData = ApplicationData.getSharedInstance();
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        (getActivity()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        screen_height = displayMetrics.heightPixels;
        screen_width = displayMetrics.widthPixels;


        SettingOption settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(getActivity(), AppConstant.SETTING_OPTION);
        color_cate_shape = settingOption.getData().getOptions().getCate_shape_color();
        color_cate_name = settingOption.getData().getOptions().getCate_name_color();
        cate_viewtype = Integer.parseInt(settingOption.getData().getOptions().getCategory_view());
        AppDebugLog.println("CatView= : " + settingOption.getData().getOptions().getCategory_view());
        dataWrite = new DataWrite(getActivity());
        dataWrite.open();

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading..");
        viewPagerTopSlider = rootView.findViewById(R.id.viewPagerTopSlider);
        circlePageIndicator = rootView.findViewById(R.id.circlePageIndicator);

        recyclerViewHomeProductList = rootView.findViewById(R.id.recyclerViewHomeProductList);
        recyclerViewHomeProductList.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(activity, 2);
        recyclerViewHomeProductList.setLayoutManager(mLayoutManager);
        recyclerViewHomeProductList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerViewHomeProductList.setItemAnimator(new DefaultItemAnimator());

        AppConstant.counter = "" + dataWrite.getCartCount();
        AppConstant.wish_counter = "" + dataWrite.getWishCount();

        ((MainActivity) getActivity()).CartVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).WishVisibility(View.VISIBLE);
        MainActivity.setCounter(AppConstant.counter);
        MainActivity.setWishCounter(AppConstant.wish_counter);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mSwipeRefreshLayout.setSoundEffectsEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dataWrite.deletedCategory();
                        appData.getCategoryList().clear();
                        isPageRefress = true;
                        if (isNetworkAvailable()) {
                            getHomeCategory();
                        } else {
                            mSwipeRefreshLayout.setRefreshing(false);
                            showNoInternetPromptDlg();
                        }


                    }
                }, 2500);
            }


        });


        viewPagerTopSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("onPageScrolled", "" + position);
                pos = position;
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("onPageSelected", "" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("onStateChanged", "" + state);

            }
        });
        getHomeSliderImage();
        getHomeCategory();

        return rootView;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void setTopSlider(ArrayList<BeanSliderImage> beanSliderImages) {
        homeSliderAdapter = new HomeSliderAdapter(activity, beanSliderImages);
        viewPagerTopSlider.setAdapter(homeSliderAdapter);
        viewPagerTopSlider.setCurrentItem(0);
        viewPagerTopSlider.setOnPageChangeListener(this);
        viewPagerTopSlider.setCurrentItem(pos, true);
        circlePageIndicator.setViewPager(viewPagerTopSlider);

        mRun = new Runnable() {
            @Override
            public void run() {
                if (pos < homeSliderAdapter.getCount()) {
                    viewPagerTopSlider.setCurrentItem(pos, true);
                    handler.postDelayed(this, 4000);
                    pos++;
                } else {
                    pos = 0;
                    viewPagerTopSlider.setCurrentItem(pos, true);
                    handler.postDelayed(this, 4000);
                    circlePageIndicator.setViewPager(viewPagerTopSlider);
                    pos++;
                }
            }
        };
        handler.post(mRun);


    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void doBack() {
        if (exit) {
            getActivity().finish(); // finish activity
        } else {

            dataWrite.showToast(getString(R.string.press_back_again_to_exit));
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("responseCode : " + responseCode);

        switch (responseCode) {
            case CategoryUpdate:

                break;
            case NetworkError:
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

    private void getHomeSliderImage() {
        progressDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Webservice.BASE_URL + Webservice.get_home_image_slider
                , new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res2", "" + response.toString());
                        try {
                            JSONObject object = new JSONObject(response.toString());
                            if (object.has("slider_data")) {
                                JSONArray jsonArray = object.getJSONArray("slider_data");
                                if (jsonArray.length() != 0) {
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        beanSliderImages.clear();
                                        beanSliderImages.addAll((Collection<? extends BeanSliderImage>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanSliderImage>>() {
                                        }.getType()));

                                    }
                                    setTopSlider(beanSliderImages);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error2", "" + error.toString());
                progressDialog.dismiss();
            }
        }) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonObjReq.setTag(activity);
        queue.add(jsonObjReq);
    }

    private void getHomeCategory() {
        progressDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Webservice.BASE_URL + Webservice.get_home_category
                , new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res2", "" + response.toString());
                        try {
                            JSONObject object = new JSONObject(response.toString());
                            JSONObject jsonObject = object.optJSONObject("products");

                            if (jsonObject.has("products")) {
                                JSONArray jsonArray = jsonObject.optJSONArray("products");
                                if (jsonArray.length() != 0) {
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        beanHomeProducts.clear();
                                        beanHomeProducts.addAll((Collection<? extends BeanHomeProduct>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanHomeProduct>>() {
                                        }.getType()));

                                    }
                                    for (int i = 0; i < beanHomeProducts.size(); i++) {
                                        BeanProduct bean = new BeanProduct();
                                        bean.setProduct_id(beanHomeProducts.get(i).productId);
                                        bean.setTitle(beanHomeProducts.get(i).title);
                                        bean.setQty(beanHomeProducts.get(i).qty);
                                        bean.setDescription(beanHomeProducts.get(i).description);
                                        bean.setImage(beanHomeProducts.get(i).image);
                                        bean.setStatus(beanHomeProducts.get(i).status);


                                        bean.setParant_category_id(beanHomeProducts.get(i).parentCategoryId);
                                        bean.setRating(beanHomeProducts.get(i).rating);
                                        bean.setCurrency_symbol(beanHomeProducts.get(i).currencySymbol);
                                        bean.setWish("0");

                                        bean.setRegular_price(beanHomeProducts.get(i).regularPrice);


                                        bean.setSale_price(beanHomeProducts.get(i).salePrice);

                                        bean.setOn_sale(beanHomeProducts.get(i).onSale);


                                        bean.setTax_product_with_price_regula_max("");

                                        bean.setTax_product_with_price_regula_min("");
                                        bean.setTax_product_with_price_sale_max("");

                                        bean.setTax_product_with_price_sale_min("");


                                        bean.setRegular_tax_price("");


                                        bean.setSale_tax_price("");

                                        bean.setRegular_tax_price(beanHomeProducts.get(i).taxProductWithPriceRegula);

                                        bean.setSale_tax_price(beanHomeProducts.get(i).taxProductWithPriceSale);

                                        productlist.add(bean);
                                    }
                                    AdapterHomeProduct adapterHomeProduct = new AdapterHomeProduct(productlist, beanHomeProducts, activity);
                                    recyclerViewHomeProductList.setAdapter(adapterHomeProduct);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        if (isPageRefress) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            isPageRefress = false;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error2", "" + error.toString());
                progressDialog.dismiss();
                if (isPageRefress) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    isPageRefress = false;
                }

            }
        }) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        jsonObjReq.setTag(activity);
        queue.add(jsonObjReq);
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
            getHomeSliderImage();
            getHomeCategory();
        } else {
            showNoInternetPromptDlg();
        }

    }
}
