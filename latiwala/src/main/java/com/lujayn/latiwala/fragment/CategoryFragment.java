package com.lujayn.latiwala.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.activity.MainActivity;
import com.lujayn.latiwala.activity.ProductActivity;
import com.lujayn.latiwala.activity.SubCategoryActivity;
import com.lujayn.latiwala.adapter.GridSpacingItemDecoration;
import com.lujayn.latiwala.bean.BeanHomeCategory;
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
public class CategoryFragment extends Fragment implements MainActivity.OnBackPressedListener,
        RequestTaskDelegate, ViewPager.OnPageChangeListener, View.OnClickListener {
    View rootView;
    private Boolean exit = false;
    GridView gridView;
    private RecyclerView recyclerView;
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
    ArrayList<BeanHomeCategory> beanHomeCategories = new ArrayList<>();

    Context activity;


    RequestQueue queue;
    private boolean isPageRefress = false;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getContext();
        queue = Volley.newRequestQueue(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            Bundle bundle = this.getArguments();
            rootView = inflater.inflate(R.layout.fragment_category_old, container, false);
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

            gridView = (GridView) rootView.findViewById(R.id.gvCategory);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerCategory);


            if (cate_viewtype == 0) {
                gridView.setVisibility(View.GONE);

                mLayoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                recyclerView.setVisibility(View.VISIBLE);

            } else {
                gridView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL) {
                    @Override
                    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                        // Do not draw the divider
                    }
                });
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }

            displaycategory();

            AppConstant.counter = "" + dataWrite.getCartCount();
            AppConstant.wish_counter = "" + dataWrite.getWishCount();

            ((MainActivity) getActivity()).CartVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).WishVisibility(View.VISIBLE);
            MainActivity.setCounter(AppConstant.counter);
            MainActivity.setWishCounter(AppConstant.wish_counter);

            mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
            mSwipeRefreshLayout.setSoundEffectsEnabled(true);
            //mSwipeRefreshLayout.setDistanceToTriggerSync(200);
            //  mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.color_grey_light));
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    //  mSwipeRefreshLayout.setRefreshing(false);

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


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (beanHomeCategories.get(i).child.equals("1")) {


                        AppConstant.catID = beanHomeCategories.get(i).categoryId;
                        AppConstant.catName = beanHomeCategories.get(i).categoryName;


                        Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                        intent.putExtra("beanSearchProducts", beanSearchProducts);
                        startActivity(intent);


                    } else {


                        AppConstant.catID = beanHomeCategories.get(i).categoryId;
                        AppConstant.catName = beanHomeCategories.get(i).categoryName;
                        AppConstant.catslug = beanHomeCategories.get(i).slug;
                        AppConstant._Activty = "category";
                        AppConstant.page_view = "category";

                        Intent intent = new Intent(getActivity(), ProductActivity.class);
                        intent.putExtra("beanSearchProducts", beanSearchProducts);
                        getActivity().startActivity(intent);

                    }

                }
            });
            initialize();


        } catch (Exception e) {
            Log.e("CategoryFragment", ":Exception+++++++++++1");
            e.printStackTrace();
        }


        return rootView;
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

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void displaycategory() {
        CategoryRecyAdapter productCategoryAdapter = new CategoryRecyAdapter(beanHomeCategories);
        recyclerView.setAdapter(productCategoryAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        // getActivity().unregisterReceiver(receive);

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


    public class CategoryRecyAdapter extends RecyclerView.Adapter<CategoryRecyAdapter.MainViewHolder> {
        Context context;

        ArrayList<BeanHomeCategory> bean_list;
        private static final int TYPE_0 = 0;
        private static final int TYPE_1 = 1;
        private static final int TYPE_2 = 2;
        private static final int TYPE_3 = 3;
        private static final int TYPE_4 = 4;
        private static final int TYPE_5 = 5;

        public CategoryRecyAdapter(ArrayList<BeanHomeCategory> bean_list) {

            this.bean_list = bean_list;

        }


        @Override
        public int getItemViewType(int position) {

            switch (cate_viewtype) {

                case 0:
                    return TYPE_0;
                case 1:
                    return TYPE_1;
                case 2:
                    return TYPE_2;
                case 3:
                    return TYPE_3;
                case 4:
                    return TYPE_4;
                case 5:
                    return TYPE_5;

                default:
                    return -1;
            }

        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            switch (viewType) {

                case TYPE_0:

                    View leftview1 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category, parent, false);


                    return new MainViewHolder(leftview1, viewType);
                case TYPE_1:


                    View leftview = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category_list, parent, false);


                    return new MainViewHolder(leftview, viewType);

                case TYPE_2:
                    View leftview2 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category_list2, parent, false);

                    //ViewHolder2 viewHolder2 = new ViewHolder2(leftview2);

                    return new MainViewHolder(leftview2, viewType);


                case TYPE_3:
                    View leftview3 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category_list3, parent, false);

                    //ViewHolder2 viewHolder2 = new ViewHolder2(leftview2);

                    return new MainViewHolder(leftview3, viewType);

                case TYPE_4:
                    View leftview4 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category_list5, parent, false);

                    //ViewHolder1 viewHolder3 = new ViewHolder1(leftview3);

                    return new MainViewHolder(leftview4, viewType);

                case TYPE_5:
                    View leftview5 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category_list6, parent, false);

                    //ViewHolder1 viewHolder4 = new ViewHolder1(leftview4);

                    return new MainViewHolder(leftview5, viewType);

            }

            return null;
        }

        @Override
        public void onBindViewHolder(final CategoryRecyAdapter.MainViewHolder vh, final int i) {

            try {
                vh.categoryName.setText(bean_list.get(i).categoryName);
                Glide.with(getActivity()).load(bean_list.get(i).image)
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
                        .into(vh.categoryImage);

                vh.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (bean_list.get(i).child.equals("1")) {
                            AppConstant.catID = bean_list.get(i).categoryId;
                            AppConstant.catName = bean_list.get(i).categoryName;
                            Intent intent = new Intent(getActivity(), SubCategoryActivity.class);
                            intent.putExtra("beanSearchProducts", beanSearchProducts);
                            startActivity(intent);

                        } else {

                            AppConstant.catID = bean_list.get(i).categoryId;
                            AppConstant.catName = bean_list.get(i).categoryName;
                            AppConstant.catslug = bean_list.get(i).slug;
                            AppConstant._Activty = "category";
                            AppConstant.page_view = "category";
                            Intent intent = new Intent(getActivity(), ProductActivity.class);
                            intent.putExtra("beanSearchProducts", beanSearchProducts);
                            getActivity().startActivity(intent);

                        }

                    }
                });
            } catch (Exception e) {
                Log.e("CategoryFragment", ":Exception+++++++++++2");
                e.printStackTrace();
            }


        }


        @Override
        public int getItemCount() {
            return bean_list.size();
        }


        public class MainViewHolder extends RecyclerView.ViewHolder {
            TextView categoryName;
            ImageView categoryImage;
            CardView cardView;
            ProgressBar progressBar;
            RelativeLayout rlcate7;
            View vcate3;
            RelativeLayout llCtName;
            ImageView catback;
            TextView textViewStartPrice;

            public MainViewHolder(View itemView, int viewType) {
                super(itemView);

                switch (viewType) {

                    case TYPE_0:
                        textViewStartPrice = (TextView) itemView.findViewById(R.id.textViewStartPrice);
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        llCtName = itemView.findViewById(R.id.llCtName);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);

                        break;

                    case TYPE_1:
                        textViewStartPrice = (TextView) itemView.findViewById(R.id.textViewStartPrice);
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);
                        catback = (ImageView) itemView.findViewById(R.id.rlCatlist);


                        Drawable myIcon = getResources().getDrawable(R.drawable.shape_left_corner);
                        //ColorFilter filter = new LightingColorFilter( Color.TRANSPARENT, Color.TRANSPARENT );
                        myIcon.setColorFilter(Color.parseColor(color_cate_shape), PorterDuff.Mode.SRC_ATOP);
                        //catback.setImageDrawable(myIcon);
                        break;

                    case TYPE_2:
                        textViewStartPrice = (TextView) itemView.findViewById(R.id.textViewStartPrice);
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);

                        ShapeDrawable badge2 = new ShapeDrawable(new RectShape());
                        badge2.getPaint().setColor(Color.parseColor("#99".concat(color_cate_shape.replace("#", ""))));
                        //badge3.getPaint().setColor(Color.parseColor(color_back));
                        categoryName.setBackground(badge2);
                        break;
                    case TYPE_3:

                        textViewStartPrice = (TextView) itemView.findViewById(R.id.textViewStartPrice);
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);
                        vcate3 = (View) itemView.findViewById(R.id.viewCategory3);


                        ShapeDrawable badge3 = new ShapeDrawable(new RectShape());
                        badge3.getPaint().setColor(Color.parseColor("#99".concat(color_cate_shape.replace("#", ""))));
                        //badge3.getPaint().setColor(Color.parseColor(color_back));
                        vcate3.setBackground(badge3);

                        break;

                    case TYPE_4:
                        textViewStartPrice = (TextView) itemView.findViewById(R.id.textViewStartPrice);
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);
                        catback = (ImageView) itemView.findViewById(R.id.rlCatlist);


                        Drawable myIcon5 = getResources().getDrawable(R.drawable.shape_middle_angle);
                        //ColorFilter filter5 = new LightingColorFilter( Color.TRANSPARENT, Color.TRANSPARENT);
                        myIcon5.setColorFilter(Color.parseColor(color_cate_shape), PorterDuff.Mode.SRC_ATOP);

                        break;
                    case TYPE_5:
                        textViewStartPrice = (TextView) itemView.findViewById(R.id.textViewStartPrice);
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);
                        catback = (ImageView) itemView.findViewById(R.id.rlCatlist);

                        Drawable myIcon6 = getResources().getDrawable(R.drawable.shape_right_corner);
                        //ColorFilter filter6 = new LightingColorFilter(Color.parseColor(color_back), Color.TRANSPARENT);
                        myIcon6.setColorFilter(Color.parseColor(color_cate_shape), PorterDuff.Mode.SRC_ATOP);
                        //catback.setBackgroundDrawable(myIcon6);
                        break;

                    default:
                        break;

                }
            }

        }

    }


    private void getHomeCategory() {
        progressDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Webservice.BASE_URL + Webservice.URL_ProductCategory
                , new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res2", "" + response.toString());
                        try {
                            JSONObject object = new JSONObject(response.toString());
                            JSONObject jsonObject = object.optJSONObject("data");

                            if (jsonObject.has("product_categories")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("product_categories");
                                if (jsonArray.length() != 0) {
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        beanHomeCategories.clear();
                                        beanHomeCategories.addAll((Collection<? extends BeanHomeCategory>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanHomeCategory>>() {
                                        }.getType()));
                                    }
                                    displaycategory();
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
            getHomeCategory();
        } else {
            showNoInternetPromptDlg();
        }
    }

}
