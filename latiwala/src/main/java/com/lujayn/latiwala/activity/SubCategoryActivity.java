package com.lujayn.latiwala.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.lujayn.latiwala.bean.BeanSearchProduct;
import com.lujayn.latiwala.bean.BeanSubCategory;
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

import java.util.ArrayList;

import cn.refactor.lib.colordialog.PromptDialog;
import dmax.dialog.SpotsDialog;

public class SubCategoryActivity extends AppCompatActivity implements RequestTaskDelegate, View.OnClickListener {

    GridView gridView;
    private Boolean exit = false;
    ArrayList<BeanSubCategory> subcategorylist;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    String color_cate_shape, color_cate_name;
    SessionManager manager;
    DataWrite dataWrite;
    int cate_viewtype = 0;
    int screen_width, screen_height;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView.LayoutManager mLayoutManager;
    private ApplicationData appData;
    Activity activity;
    LinearLayout ll_back;
    TextView tvCounter;
    String color_back;
    RelativeLayout rlCounter_toolbar;
    Dialog search_dialog;
    AdapterSearch adapterSearch;
    ImageView iv_right_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        try {
            activity = this;
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading..");
            manager = new SessionManager();
            appData = ApplicationData.getSharedInstance();
            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            (activity).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            screen_height = displayMetrics.heightPixels;
            screen_width = displayMetrics.widthPixels;

            SettingOption settingOption = new SettingOption();
            settingOption = manager.getPreferencesOption(activity, AppConstant.SETTING_OPTION);
            color_cate_shape = settingOption.getData().getOptions().getCate_shape_color();
            color_cate_name = settingOption.getData().getOptions().getCate_name_color();
            color_back = settingOption.getData().getOptions().getToolbar_back_color();
            cate_viewtype = Integer.parseInt(settingOption.getData().getOptions().getCategory_view());
            dataWrite = new DataWrite(activity);
            dataWrite.open();
            gridView = (GridView) findViewById(R.id.gvCategory);


            recyclerView = (RecyclerView) findViewById(R.id.recyclerCategory);
            TextView tv_title = (TextView) findViewById(R.id.tv_title);
            ll_back = (LinearLayout) findViewById(R.id.ll_back);
            tvCounter = (TextView) findViewById(R.id.tvCounter);
            RelativeLayout rl_header = (RelativeLayout) findViewById(R.id.rl_header);
            rlCounter_toolbar = (RelativeLayout) findViewById(R.id.rlCounter_toolbar);
            iv_right_search = (ImageView) findViewById(R.id.iv_right_search);
            rl_header.setBackgroundColor(Color.parseColor(color_back));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor(color_back));
            }

            if (cate_viewtype == 0) {
                gridView.setVisibility(View.GONE);

                mLayoutManager = new GridLayoutManager(activity, 2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                recyclerView.setVisibility(View.VISIBLE);

            } else {
                gridView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL) {
                    @Override
                    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                        // Do not draw the divider
                    }
                });
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }


            AppConstant.sub_catName = AppConstant.catName;
            AppConstant.sub_catID = AppConstant.catID;
            tv_title.setText(AppConstant.sub_catName);

            displaysubcategory();
            mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
            mSwipeRefreshLayout.setSoundEffectsEnabled(true);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dataWrite.deletedSubCategory();
                            if (isNetworkAvailable()) {
                                refreshSubCategory("subcategory");
                            } else {
                                showNoInternetPromptDlg();
                            }


                        }
                    }, 2500);
                }
            });

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    if (subcategorylist.get(i).getChild().equals("1")) {
                        AppConstant.catID = subcategorylist.get(i).getSubcategory_id();
                        displaysubcategory(AppConstant.catID);
                        gridView.deferNotifyDataSetChanged();
                    } else {
                        AppConstant.catslug = subcategorylist.get(i).getSlug();
                        AppConstant.catID = subcategorylist.get(i).getSubcategory_id();
                        AppConstant.catName = subcategorylist.get(i).getSubcategory_name();
                        AppConstant._Activty = "subcategory";
                        AppConstant.page_view = "category";

                        String title = getString(R.string.app_name);
                        Intent intent = new Intent(activity, ProductActivity.class);
                        startActivity(intent);

                    }

                }
            });
            ll_back.setOnClickListener(this);
            rlCounter_toolbar.setOnClickListener(this);
            iv_right_search.setOnClickListener(this);
        } catch (Exception e) {
            Log.e("SubCategoryActivity", ":Exception++++++++++++++++++1");
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

    private void refreshSubCategory(String subcategory) {
        progressDialog.show();
        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_ProductSubCategory;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, "subcategory");

    }

    private void displaysubcategory() {
        mSwipeRefreshLayout.setRefreshing(false);
        subcategorylist = dataWrite.fetchSubCategorys(AppConstant.catID);

        if (subcategorylist.size() == 0) {
            if (isNetworkAvailable()) {
                refreshSubCategory("subcategory");
            } else {
                showNoInternetPromptDlg();
            }


        } else {
            progressDialog.dismiss();
            CategoryRecyAdapter productCategoryAdapter = new CategoryRecyAdapter(subcategorylist);
            recyclerView.setAdapter(productCategoryAdapter);

        }

    }

    private void displaysubcategory(String catid) {
        mSwipeRefreshLayout.setRefreshing(false);
        subcategorylist = dataWrite.fetchSubCategorys(catid);

        if (subcategorylist.size() == 0) {
            // refreshSubCategory("subcategory");
            dataWrite.showToast(getString(R.string.subcategory_not_found));
            progressDialog.dismiss();
            CategoryRecyAdapter productCategoryAdapter = new CategoryRecyAdapter(subcategorylist);
            recyclerView.setAdapter(productCategoryAdapter);

        } else {

            progressDialog.dismiss();
            CategoryRecyAdapter productCategoryAdapter = new CategoryRecyAdapter(subcategorylist);
            recyclerView.setAdapter(productCategoryAdapter);

        }

    }


    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("responseCode : " + responseCode);

        switch (responseCode) {

            case Subcategory:

                //Log.d("TEST", "Sub CATEGORY is ");
                displaysubcategory();
                break;

            case NetworkError:
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


    public class CategoryRecyAdapter extends RecyclerView.Adapter<CategoryRecyAdapter.MainViewHolder> {
        Context context;
        ArrayList<BeanSubCategory> bean_list;
        private static final int TYPE_0 = 0;
        private static final int TYPE_1 = 1;
        private static final int TYPE_2 = 2;
        private static final int TYPE_3 = 3;
        private static final int TYPE_4 = 4;
        private static final int TYPE_5 = 5;


        @Override
        public CategoryRecyAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            switch (viewType) {

                case TYPE_0:

                    View leftview1 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category, parent, false);


                    return new CategoryRecyAdapter.MainViewHolder(leftview1, viewType);
                case TYPE_1:


                    View leftview = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category_list, parent, false);


                    return new CategoryRecyAdapter.MainViewHolder(leftview, viewType);

                case TYPE_2:
                    View leftview2 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category_list2, parent, false);

                    //ViewHolder2 viewHolder2 = new ViewHolder2(leftview2);

                    return new CategoryRecyAdapter.MainViewHolder(leftview2, viewType);


                case TYPE_3:
                    View leftview3 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category_list3, parent, false);

                    //ViewHolder2 viewHolder2 = new ViewHolder2(leftview2);

                    return new CategoryRecyAdapter.MainViewHolder(leftview3, viewType);

                case TYPE_4:
                    View leftview4 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category_list5, parent, false);

                    //ViewHolder1 viewHolder3 = new ViewHolder1(leftview3);

                    return new CategoryRecyAdapter.MainViewHolder(leftview4, viewType);

                case TYPE_5:
                    View leftview5 = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.inflater_category_list6, parent, false);

                    //ViewHolder1 viewHolder4 = new ViewHolder1(leftview4);

                    return new CategoryRecyAdapter.MainViewHolder(leftview5, viewType);

            }

            return null;
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
        public void onBindViewHolder(final CategoryRecyAdapter.MainViewHolder vh, final int i) {
            try {

                vh.categoryName.setText(bean_list.get(i).getSubcategory_name());
                Glide.with(activity).load(bean_list.get(i).getImage())
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

                        if (subcategorylist.get(i).getChild().equals("1")) {

                            // AppConstant.catslug = subcategorylist.get(i).getSlug();
                            AppConstant.catID = bean_list.get(i).getSubcategory_id();
                            //AppConstant.catName = subcategorylist.get(i).getSubcategory_name();
                            //  AppConstant._Activty = "subcategory";
                            //Log.d("catslug","AppConstant.catID==== " +AppConstant.catID);
                            displaysubcategory(AppConstant.catID);


                        } else {

                            AppConstant.catslug = bean_list.get(i).getSlug();
                            AppConstant.catID = bean_list.get(i).getSubcategory_id();
                            AppConstant.catName = bean_list.get(i).getSubcategory_name();
                            AppConstant._Activty = "subcategory";
                            AppConstant.page_view = "category";
                            //AppConstant.sub_catID = AppConstant.catID;
                            //AppConstant.sub_catName = AppConstant.catName;

                            String title = getString(R.string.app_name);
                            Intent intent = new Intent(activity, ProductActivity.class);
                            startActivity(intent);

                        }
                    }
                });
            } catch (Exception e) {
                Log.e("SubCategoryActivity", ":Exception++++++++++++++++++2");
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return bean_list.size();
        }

        public CategoryRecyAdapter(ArrayList<BeanSubCategory> beanProductCategories_list) {

            this.bean_list = beanProductCategories_list;

        }


        public class MainViewHolder extends RecyclerView.ViewHolder {
            TextView categoryName;
            ImageView categoryImage;
            CardView cardView;
            ProgressBar progressBar;
            View vcate3;
            RelativeLayout llCtName;
            ImageView catback;

            public MainViewHolder(View itemView, int viewType) {
                super(itemView);

                switch (viewType) {
                    case TYPE_0:
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        llCtName = (RelativeLayout) itemView.findViewById(R.id.llCtName);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);
                        categoryName.setTextColor(Color.parseColor(color_cate_name));


                        break;

                    case TYPE_1:
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);
                        catback = (ImageView) itemView.findViewById(R.id.rlCatlist);


                        categoryName.setTextColor(Color.parseColor(color_cate_name));
                        Drawable myIcon = getResources().getDrawable(R.drawable.shape_left_corner);
                        //ColorFilter filter = new LightingColorFilter( Color.TRANSPARENT, Color.TRANSPARENT );
                        myIcon.setColorFilter(Color.parseColor(color_cate_shape), PorterDuff.Mode.SRC_ATOP);
                        //catback.setImageDrawable(myIcon);
                        break;
                    case TYPE_2:
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);


                        categoryName.setTextColor(Color.parseColor(color_cate_name));
                        ShapeDrawable badge = new ShapeDrawable(new RectShape());
                        badge.getPaint().setColor(Color.parseColor("#99".concat(color_cate_shape.replace("#", ""))));
                        //badge.getPaint().setColor(Color.parseColor(color_back));
                        categoryName.setBackground(badge);

                        break;
                    case TYPE_3:
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);
                        vcate3 = (View) itemView.findViewById(R.id.viewCategory3);

                        categoryName.setTextColor(Color.parseColor(color_cate_name));
                        ShapeDrawable badge3 = new ShapeDrawable(new RectShape());
                        badge3.getPaint().setColor(Color.parseColor("#99".concat(color_cate_shape.replace("#", ""))));
                        //badge3.getPaint().setColor(Color.parseColor(color_back));
                        vcate3.setBackground(badge3);
                        break;
                    case TYPE_4:
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);
                        catback = (ImageView) itemView.findViewById(R.id.rlCatlist);


                        categoryName.setTextColor(Color.parseColor(color_cate_name));
                        Drawable myIcon5 = getResources().getDrawable(R.drawable.shape_middle_angle);
                        //ColorFilter filter5 = new LightingColorFilter( Color.TRANSPARENT, Color.TRANSPARENT);
                        myIcon5.setColorFilter(Color.parseColor(color_cate_shape), PorterDuff.Mode.SRC_ATOP);

                        break;

                    case TYPE_5:
                        categoryName = (TextView) itemView.findViewById(R.id.tvCategoryname);
                        categoryImage = (ImageView) itemView.findViewById(R.id.ivCategoryImage);
                        progressBar = (ProgressBar) itemView.findViewById(R.id.pbarCategory);
                        cardView = (CardView) itemView.findViewById(R.id.cardCategory2);
                        catback = (ImageView) itemView.findViewById(R.id.rlCatlist);

                        categoryName.setTextColor(Color.parseColor(color_cate_name));
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

    @Override
    protected void onStart() {
        super.onStart();
        setCounter(AppConstant.counter);
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
        search_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Window window = search_dialog.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        LinearLayout ll_back = (LinearLayout) search_dialog.findViewById(R.id.ll_back);
        final EditText et_search = (EditText) search_dialog.findViewById(R.id.et_search);
        final RecyclerView rv_product_list = (RecyclerView) search_dialog.findViewById(R.id.rv_product_list);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_product_list.setLayoutManager(gridLayoutManager);


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_search.getText().toString().length() > ApplicationContext.searchCharacterLength) {
                    adapterSearch = new AdapterSearch(ApplicationContext.beanSearchProducts, activity);
                    rv_product_list.setAdapter(adapterSearch);
                    adapterSearch.getFilter().filter(s.toString());
                } else {
                    adapterSearch = new AdapterSearch(ApplicationContext.beanSearchProductsTemp, activity);
                    rv_product_list.setAdapter(adapterSearch);
                    adapterSearch.notifyDataSetChanged();
                }
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
            refreshSubCategory("subcategory");
        } else {
            showNoInternetPromptDlg();
        }
    }

}
