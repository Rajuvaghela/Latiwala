package com.lujayn.latiwala.activity;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.lujayn.latiwala.adapter.AdapterSearch;
import com.lujayn.latiwala.adapter.HomePageTabAdapter;
import com.lujayn.latiwala.bean.BeanCart;
import com.lujayn.latiwala.bean.BeanSearchProduct;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationContext;
import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.common.Webservice;
import com.lujayn.latiwala.database.DataWrite;
import com.lujayn.latiwala.fragment.CategoryFragment;
import com.lujayn.latiwala.fragment.ContactUsFragment;
import com.lujayn.latiwala.fragment.FragmentDrawer;
import com.lujayn.latiwala.fragment.MyAccountFragment;
import com.lujayn.latiwala.fragment.TermAndConditionFragment;
import com.lujayn.latiwala.fragment.WishlistFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.PromptDialog;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, TabLayout.OnTabSelectedListener {
    private static TextView tv_Title, tv_counter, tv_counter_wish;
    private Boolean exit = false;
    private static String TAG = MainActivity.class.getSimpleName();
    TextView tv_logout;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    SessionManager manager;
    DataWrite dataWrite;
    String status;
    ArrayList<BeanCart> cartlist;
    static RelativeLayout rl_counterCart, rl_counterWish;
    static ImageView iv_search, iv_cart, iv_wish;
    String page_view;
    String color_statusbar, color_tool_bg, color_tool_title, color_tool_icon;
    Dialog search_dialog;
    EditText et_search;
    AdapterSearch adapterSearch;
    RequestQueue queue;
    Activity activity;

    RecyclerView rv_product_list;
    RecyclerView.LayoutManager layoutManager;
    ViewPager viewPager;
    TabLayout tabLayout;
    HomePageTabAdapter homePageTabAdapter;
    CoordinatorLayout main_content;
    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_category,
            R.drawable.ic_product,
            R.drawable.ic_offers,
            R.drawable.ic_beauty
    };
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    protected OnBackPressedListener onBackPressedListener;

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }


    private View.OnClickListener onClickMethod = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.tvLogout:

                    if (tv_logout.getText().toString().equals("Login")) {

                        Intent loginInt = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginInt);
                        finish();

                    } else {

                        manager.setPreferences(MainActivity.this, "status", "0");
                        dataWrite.deletedData();

                        Intent in = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(in);
                        finish();
                    }

                    break;
                case R.id.rlCounter_toolbar:
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                    iv_search.setVisibility(View.VISIBLE);
                    CartVisibility(View.VISIBLE);
                    WishVisibility(View.VISIBLE);

                    break;

                case R.id.iv_right_search:
                    SearchDialogOpen();
                    break;
                case R.id.rlwish_toolbar:
                    Fragment fragment2 = new WishlistFragment();
                    String title2 = getString(R.string.title_wishlist);
                    FragmentManager fragmentManager2 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                    Fragment current2 = fragmentManager2.findFragmentByTag(title2);
                    if (current2 != null) {
                        fragmentTransaction2.remove(current2);
                        fragmentTransaction2.addToBackStack(title2);
                    } else {
                        fragmentTransaction2.addToBackStack(title2);
                    }

                    fragmentTransaction2.replace(R.id.container_body, fragment2);
                    fragmentTransaction2.commit();
                    // set the toolbar title

                    iv_search.setVisibility(View.VISIBLE);
                    CartVisibility(View.VISIBLE);
                    break;

            }

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        switch (position) {

            case 0:
                main_content.setVisibility(View.VISIBLE);
                Fragment fragment2 = new CategoryFragment();
                String title = getString(R.string.title_category);
                manager.setPreferences(ApplicationContext.getAppContext(), AppConstant.LAST_FRAG, 0);
                AppConstant.page_view = "category";
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                Fragment current2 = fragmentManager2.findFragmentByTag(title);
                if (current2 != null) {
                    fragmentTransaction2.remove(current2);
                    fragmentTransaction2.addToBackStack(title);
                } else {
                    fragmentTransaction2.addToBackStack(title);
                }

                fragmentTransaction2.replace(R.id.container_body, fragment2);
                fragmentTransaction2.commit();
                break;

            case 1:

                Intent intent = new Intent(activity, ProductActivity.class);
                startActivity(intent);
                AppConstant.page_view = "shop";
                AppConstant._Activty = "shop";

                break;

            case 2:
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                break;

            case 3:

                main_content.setVisibility(View.GONE);
                Fragment fragment3 = new ContactUsFragment();
                title = getString(R.string.title_contactus);
                manager.setPreferences(ApplicationContext.getAppContext(), AppConstant.LAST_FRAG, 3);
                AppConstant.page_view = "category";
                FragmentManager fragmentManager3 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                Fragment current3 = fragmentManager3.findFragmentByTag(title);
                if (current3 != null) {
                    fragmentTransaction3.remove(current3);
                    fragmentTransaction3.addToBackStack(title);
                } else {
                    fragmentTransaction3.addToBackStack(title);
                }

                fragmentTransaction3.replace(R.id.container_body, fragment3);
                fragmentTransaction3.commit();
                break;
            case 4:

                main_content.setVisibility(View.GONE);
                Fragment fragment4 = new TermAndConditionFragment();
                title = getString(R.string.title_terms_condition);
                manager.setPreferences(ApplicationContext.getAppContext(), AppConstant.LAST_FRAG, 4);
                AppConstant.page_view = "category";
                FragmentManager fragmentManager4 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                Fragment current4 = fragmentManager4.findFragmentByTag(title);
                if (current4 != null) {
                    fragmentTransaction4.remove(current4);
                    fragmentTransaction4.addToBackStack(title);
                } else {
                    fragmentTransaction4.addToBackStack(title);
                }

                fragmentTransaction4.replace(R.id.container_body, fragment4);
                fragmentTransaction4.commit();
                break;

            case 5:

                status = manager.getPreferences(MainActivity.this, AppConstant.STATUS);
                if (status.equals("1")) {

                    main_content.setVisibility(View.GONE);
                    Fragment fragment5 = new MyAccountFragment();
                    manager.setPreferences(ApplicationContext.getAppContext(), AppConstant.LAST_FRAG, 5);
                    title = getString(R.string.title_account);
                    FragmentManager fragmentManager5 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction5 = fragmentManager5.beginTransaction();
                    Fragment current5 = fragmentManager5.findFragmentByTag(title);
                    if (current5 != null) {
                        fragmentTransaction5.remove(current5);
                        fragmentTransaction5.addToBackStack(title);
                    } else {
                        fragmentTransaction5.addToBackStack(title);
                    }

                    fragmentTransaction5.replace(R.id.container_body, fragment5);
                    fragmentTransaction5.commit();
                } else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    i.putExtra("LOGIN", "login");
                    startActivity(i);
                    finish();
                }

                break;
            default:
                main_content.setVisibility(View.VISIBLE);
                break;
        }

    }

    public static void imageviewVisibility(int visible) {
        iv_search.setVisibility(visible);
    }

    public static void CartVisibility(int visible) {
        rl_counterCart.setVisibility(visible);
    }

    public static void WishVisibility(int visible) {
        //rl_counterWish.setVisibility(visible);
    }


    public static void setCounter(String counter) {
        if (counter.equals("0")) {
            tv_counter.setVisibility(View.GONE);
        } else {
            tv_counter.setVisibility(View.VISIBLE);
            tv_counter.setText(counter);
        }

    }

    public static void setWishCounter(String counter) {

    }

    @Override
    public void onBackPressed() {
        main_content.setVisibility(View.VISIBLE);
        if (onBackPressedListener != null) {
            onBackPressedListener.doBack();
        } else {
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getSearchData() {
        progressDialog.show();
        Map<String, String> postParam = new HashMap<String, String>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Webservice.BASE_URL + Webservice.get_search_products
                , new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("res2", "" + response.toString());
                        try {
                            JSONObject object1 = new JSONObject(response.toString());
                            JSONObject object = object1.getJSONObject("data");
                            if (object.has("products")) {
                                JSONArray jsonArray = object.getJSONArray("products");
                                if (jsonArray.length() != 0) {
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        ApplicationContext.beanSearchProducts.clear();
                                        ApplicationContext.beanSearchProducts.addAll((Collection<? extends BeanSearchProduct>) new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<BeanSearchProduct>>() {
                                        }.getType()));

                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        homePageTabAdapter = new HomePageTabAdapter(getSupportFragmentManager(), activity);
                        tabLayout.setupWithViewPager(viewPager);
                        viewPager.setAdapter(homePageTabAdapter);
                        viewPager.setOffscreenPageLimit(4);
                        setupTabIcons();

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
        jsonObjReq.setTag(MainActivity.this);
        queue.add(jsonObjReq);

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
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
        et_search = (EditText) search_dialog.findViewById(R.id.et_search);
        rv_product_list = (RecyclerView) search_dialog.findViewById(R.id.rv_product_list);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_product_list.setLayoutManager(gridLayoutManager);


        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    if (et_search.getText().toString().length() > ApplicationContext.searchCharacterLength) {
                        adapterSearch = new AdapterSearch(ApplicationContext.beanSearchProducts, activity);
                        rv_product_list.setAdapter(adapterSearch);
                        adapterSearch.getFilter().filter(s.toString());
                    } else {
                        adapterSearch = new AdapterSearch(ApplicationContext.beanSearchProductsTemp, activity);
                        rv_product_list.setAdapter(adapterSearch);
                        adapterSearch.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Log.e("MainActicvity",":Exception+++++++++++1");
                    e.printStackTrace();
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
        activity = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        queue = Volley.newRequestQueue(this);
        manager = new SessionManager();
        status = manager.getPreferences(MainActivity.this, AppConstant.STATUS);

        SettingOption settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(MainActivity.this, AppConstant.SETTING_OPTION);
        color_statusbar = settingOption.getData().getOptions().getStatus_bar_color();
        color_tool_bg = settingOption.getData().getOptions().getToolbar_back_color();
        color_tool_title = settingOption.getData().getOptions().getToolbar_title_color();
        color_tool_icon = settingOption.getData().getOptions().getToolbar_icon_color();
        page_view = settingOption.getData().getOptions().getPage_view();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color_statusbar));
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar1);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color_tool_bg)));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        main_content = findViewById(R.id.main_content);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        //  tabLayout.setBackgroundColor(Color.parseColor(color_tool_bg));
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        tv_Title = (TextView) mToolbar.findViewById(R.id.tvTitle1);
        tv_counter = (TextView) mToolbar.findViewById(R.id.tvCounter);
        tv_counter_wish = (TextView) mToolbar.findViewById(R.id.tvCounter_wish);

        rl_counterCart = (RelativeLayout) mToolbar.findViewById(R.id.rlCounter_toolbar);
        rl_counterWish = (RelativeLayout) mToolbar.findViewById(R.id.rlwish_toolbar);
        iv_search = (ImageView) mToolbar.findViewById(R.id.iv_right_search);
        iv_cart = (ImageView) mToolbar.findViewById(R.id.iv_cart_toolbar);
        iv_wish = (ImageView) mToolbar.findViewById(R.id.iv_wish_toolbar);

        tv_Title.setTextColor(Color.parseColor(color_tool_title));

        Drawable myIcon = getResources().getDrawable(R.drawable.ic_search);
        myIcon.setColorFilter(Color.parseColor(color_tool_icon), PorterDuff.Mode.SRC_ATOP);

        Drawable myIcon1 = getResources().getDrawable(R.drawable.ic_cart_white);
        myIcon1.setColorFilter(Color.parseColor(color_tool_icon), PorterDuff.Mode.SRC_ATOP);

        Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_heart_white);
        myIcon2.setColorFilter(Color.parseColor(color_tool_icon), PorterDuff.Mode.SRC_ATOP);


        dataWrite = new DataWrite(this);
        dataWrite.open();

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        tv_logout = (TextView) findViewById(R.id.tvLogout);


        tv_logout.setOnClickListener(onClickMethod);
        rl_counterCart.setOnClickListener(onClickMethod);
        //rl_counterWish.setOnClickListener(onClickMethod);
        iv_search.setOnClickListener(onClickMethod);

        if (page_view.equals("shop")) {
            displayView(1);
        } else {
            displayView(0);
        }
        if (isNetworkAvailable()) {
            getSearchData();
        } else {
            showNoInternetPromptDlg();
        }
    }

}