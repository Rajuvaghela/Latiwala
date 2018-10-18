package com.lujayn.latiwala.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.adapter.AdapterSearch;
import com.lujayn.latiwala.adapter.GridSpacingItemDecoration;
import com.lujayn.latiwala.bean.BeanAllReivews;
import com.lujayn.latiwala.bean.BeanDetailProduct;
import com.lujayn.latiwala.bean.BeanHomeProduct;
import com.lujayn.latiwala.bean.BeanProduct;
import com.lujayn.latiwala.bean.BeanUserData;
import com.lujayn.latiwala.bean.Cartproduct;
import com.lujayn.latiwala.bean.Grouped_products;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.bean.Variant;
import com.lujayn.latiwala.bean.Variations;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationContext;
import com.lujayn.latiwala.common.ApplicationData;
import com.lujayn.latiwala.common.Config;
import com.lujayn.latiwala.common.DynamicHeightImageView;
import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.common.TouchImageView;
import com.lujayn.latiwala.common.Webservice;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.database.DataWrite;
import com.lujayn.latiwala.network.RequestTask;
import com.lujayn.latiwala.network.RequestTaskDelegate;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.refactor.lib.colordialog.PromptDialog;


public class ProductDetailActivity extends AppCompatActivity implements RequestTaskDelegate, View.OnClickListener {

    String TAG = "ProductDetailActivity";

    String product_id, product_name, variations_id, image, variation_price, variations, qnty, subTotal,
            product_type, max_stock, ori_price, tax_status;
    TextView tv_shortDescripn, tv_salePrice, tv_regularPrice, tv_offer, tv_reviews, tv_fullDescription;
    TextView tv_stock, tv_tax, tv_reviewcount, textViewTitle;
    RatingBar ratingBar;
    CardView cardAtri, cardReview, cardAddreview, cardQnty, cardPDF;
    RatingBar addRatingbar;
    LinearLayout ll_reviewList, ll_attri, ll_gstreview, ll_ratingbar, ll_grouplist, ll_footer_detail;
    RelativeLayout rl_contaner;
    ArrayList<BeanAllReivews> reivewseslist;
    EditText et_review, et_gstName, et_gstEmail;
    TextView tv_empty, tv_attriWarn, tv_loadmore, tv_pdf;
    ImageView iv_add, iv_delete, iv_send, fullimage, ic_wish;
    TextView btn_addToCart, btn_BuyNow;
    TextView etQnty_detail;
    ImageView cartimage;
    //private ImageIndicatorView mAutoImageIndicatorView;
    ViewPager vp;
    CirclePageIndicator mIndicator;
    MyPagesAdapter myPagesAdapter;
    LayoutInflater inflater, inflater_review, inflater_attri;
    private ApplicationData appData;
    BeanDetailProduct beanDetailProduct;
    private static List<String> URL_LIST;
    List<String> URL_LIST2 = new ArrayList<>();
    boolean wish_flag = false;
    SessionManager manager;
    String user_id, name, email;
    DataWrite dataWrite;
    private ProgressDialog progressDialog;
    ArrayList<TextView> textAttri = new ArrayList<TextView>();
    final ArrayList<String> selectList = new ArrayList<String>();
    ArrayList<Cartproduct> cartproducts;
    String color_back, color_font, catid, baseAddress_country, baseAddress_state;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    int N = 2;
    int count = N;
    String status, country, state, postcode, city, tax_based_on, enabel_review_rating,
            prices_include_tax, tax_display_shop, tax_display_cart, color_statusbar;
    String dataproduct;
    String PRODUCTTYPE_SIMPLE = "simple";
    String PRODUCTTYPE_VARIABLE = "variable";
    String PRODUCTTYPE_GROUPED = "grouped";
    String PRODUCTTYPE_EXTERNAL = "external";
    boolean isSimple = false;
    Activity activity;
    static TextView tvCounter;
    LinearLayout ll_back;
    RelativeLayout rl_cart;
    ImageView iv_right_search;
    Dialog search_dialog;
    AdapterSearch adapterSearch;
    int position;
    /**
     * set similar product
     */
    RecyclerView recyclerViewSimilarProduct;
    private ArrayList<BeanProduct> productlist;
    int VIEWTYPE = 0;
    ArrayList<BeanProduct> beanHomeProducts = new ArrayList<>();
    ArrayList<BeanProduct> beautyProductList;
    ArrayList<BeanProduct> offerProductList;
    ArrayList<BeanProduct> beanProductActivityList;
    ArrayList<BeanProduct> beanProductFragmentList;
    ArrayList<BeanProduct> beanProductDetailList;
    ArrayList<BeanProduct> beanHomeProducts1 = new ArrayList<>();
    ArrayList<BeanProduct> beautyProductList1;
    ArrayList<BeanProduct> offerProductList1;
    ArrayList<BeanProduct> beanProductActivityList1;
    ArrayList<BeanProduct> beanProductFragmentList1;
    ArrayList<BeanProduct> beanProductDetailList1;
    private String parentClassName;
    ProdutAdapter albumsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        try {

            activity = this;
            Bundle bundle = getIntent().getExtras();
            position = bundle.getInt("position");
            parentClassName = bundle.getString("parentClassName");
            if (parentClassName.equalsIgnoreCase("AdapterHomeProduct")) {
                beanHomeProducts = (ArrayList<BeanProduct>) getIntent().getSerializableExtra("beanHomeProductList");
                beanHomeProducts1 = beanHomeProducts;
                Log.e("size", "++++++++++++" + beanHomeProducts.size());
            } else if (parentClassName.equalsIgnoreCase("AdapterSearch")) {

            } else if (parentClassName.equalsIgnoreCase("BeautyFragment")) {
                beautyProductList = (ArrayList<BeanProduct>) getIntent().getSerializableExtra("beanJewelleryProductList");
                beautyProductList1 = beautyProductList;
                Log.e("beautyProductList", "++++++++++++" + beautyProductList.size());
            } else if (parentClassName.equalsIgnoreCase("OffersFragment")) {
                offerProductList = (ArrayList<BeanProduct>) getIntent().getSerializableExtra("beanOfferProductList");
                offerProductList1 = offerProductList;
            } else if (parentClassName.equalsIgnoreCase("ProductActivity")) {
                beanProductActivityList = (ArrayList<BeanProduct>) getIntent().getSerializableExtra("beanProductActivityList");
                beanProductActivityList1 = beanProductActivityList;
            } else if (parentClassName.equalsIgnoreCase("ProductFragment")) {
                beanProductFragmentList = (ArrayList<BeanProduct>) getIntent().getSerializableExtra("beanProductFragmentList");
                beanProductFragmentList1 = beanProductFragmentList;
            } else if (parentClassName.equalsIgnoreCase("ProductDetailActivity")) {
                beanProductDetailList = (ArrayList<BeanProduct>) getIntent().getSerializableExtra("beanProductDetailList");
                beanProductDetailList1 = beanProductDetailList;
            } else {

            }
            inflater_review = (LayoutInflater) activity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            inflater_review = LayoutInflater.from(activity);

            inflater_attri = (LayoutInflater) activity.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            inflater_attri = LayoutInflater.from(activity);

            final AppCompatActivity act = (AppCompatActivity) activity;
            if (act.getSupportActionBar() != null) {
                Toolbar toolbar = (Toolbar) act.findViewById(R.id.toolbar1);
                cartimage = toolbar.findViewById(R.id.iv_cart_toolbar);
            }
            initialize();
            recyclerViewSimilarProduct.setNestedScrollingEnabled(false);
            recyclerViewSimilarProduct.setHasFixedSize(false);
            LinearLayoutManager mLayoutManager = new GridLayoutManager(activity, 2);
            recyclerViewSimilarProduct.setLayoutManager(mLayoutManager);
            recyclerViewSimilarProduct.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
            recyclerViewSimilarProduct.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSimilarProduct.setNestedScrollingEnabled(true);

            setSimilarProduct();
        } catch (Exception e) {
            Log.e("ProductDetailActivity", ":Exception++++++++++++++++++1");
            e.printStackTrace();
        }
    }


    private void initialize() {
        try {

            manager = new SessionManager();
            appData = ApplicationData.getSharedInstance();
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading..");
            vp = findViewById(R.id.viewPager);
            recyclerViewSimilarProduct = findViewById(R.id.recyclerViewSimilarProduct);
            mIndicator = findViewById(R.id.indicator);
            cartproducts = new ArrayList<Cartproduct>();
            mSwipeRefreshLayout = findViewById(R.id.productDetail_swipe_refresh_layout);
            SettingOption settingOption = new SettingOption();
            settingOption = manager.getPreferencesOption(activity, AppConstant.SETTING_OPTION);
            color_back = settingOption.getData().getOptions().getCate_shape_color();
            color_font = settingOption.getData().getOptions().getCate_name_color();

            tax_based_on = settingOption.getData().getOptions().getTax_based_on();
            prices_include_tax = settingOption.getData().getOptions().getPrices_include_tax();
            tax_display_shop = settingOption.getData().getOptions().getTax_display_shop();
            tax_display_cart = settingOption.getData().getOptions().getTax_display_cart();

            enabel_review_rating = settingOption.getData().getOptions().getEnable_ratings_on_reviews();
            color_statusbar = settingOption.getData().getOptions().getStatus_bar_color();

            String b = settingOption.getData().getOptions().getBase_location().getWoocommerce_default_country();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor(color_statusbar));
            }


            String base[] = b.split(":");
            if (base.length > 1) {
                baseAddress_country = base[0];
                baseAddress_state = base[1];
            } else {
                baseAddress_country = base[0];
            }
            dataWrite = new DataWrite(activity);
            dataWrite.open();

            user_id = manager.getPreferences(activity, AppConstant.USER_ID);
            status = manager.getPreferences(activity, AppConstant.STATUS);
            name = manager.getPreferences(activity, AppConstant.USER_NAME);
            email = manager.getPreferences(activity, AppConstant.USER_EMAIL);

            textViewTitle = findViewById(R.id.textViewTitle);
            iv_right_search = findViewById(R.id.iv_right_search);
            tvCounter = findViewById(R.id.tvCounter);
            tv_shortDescripn = findViewById(R.id.tvCellShortdescription);
            tv_salePrice = findViewById(R.id.tvCellPrice_product);
            tv_regularPrice = findViewById(R.id.tvCellRegularPrice);
            tv_reviewcount = findViewById(R.id.tvReview_conter);
            tv_attriWarn = findViewById(R.id.tvWarn);
            tv_stock = findViewById(R.id.tvStock);
            tv_loadmore = findViewById(R.id.tvLoadMore);
            et_review = findViewById(R.id.etYourReview);
            et_gstEmail = findViewById(R.id.etGstEmail);
            et_gstName = findViewById(R.id.etGstName);
            //tv_ViewAllreview = findViewById(R.id.tvReviewlabel);
            tv_reviews = findViewById(R.id.tvReviewlabel2);
            tv_tax = findViewById(R.id.tvTax);
            tv_pdf = findViewById(R.id.tvPDF);
            tv_fullDescription = findViewById(R.id.tvFullDescription);
            cardAtri = findViewById(R.id.card_viewatri);
            cardQnty = findViewById(R.id.card_qnty);
            cardAddreview = findViewById(R.id.cardview_addreview);
            cardReview = findViewById(R.id.cardview_reviews);
            cardPDF = findViewById(R.id.card_pdf);

            ll_back = findViewById(R.id.ll_back);
            ll_attri = findViewById(R.id.llattributes);
            ll_grouplist = findViewById(R.id.llGroupProducts);
            ll_reviewList = findViewById(R.id.lvReivew);
            ll_gstreview = findViewById(R.id.llGstReview);
            ll_ratingbar = findViewById(R.id.rlYourRating);
            ll_footer_detail = findViewById(R.id.llfooter_detail);
            rl_cart = (RelativeLayout) findViewById(R.id.rl_cart);
            rl_contaner = (RelativeLayout) findViewById(R.id.container);
            fullimage = findViewById(R.id.fullImage);
            iv_send = findViewById(R.id.ivSend);
            iv_add = findViewById(R.id.ivAdd_detail);
            iv_delete = findViewById(R.id.ivDelete_detail);
            ic_wish = findViewById(R.id.icWish_detail);
            etQnty_detail = findViewById(R.id.etQnty_detail);
            RelativeLayout rl_header = (RelativeLayout) findViewById(R.id.rl_header);
            RelativeLayout rl_add_to_cart = (RelativeLayout) findViewById(R.id.rl_add_to_cart);


            btn_addToCart = findViewById(R.id.btnAddtoCart);
            btn_addToCart.setBackgroundColor(Color.parseColor(color_back));
            btn_addToCart.setTextColor(Color.parseColor(color_font));

            rl_header.setBackgroundColor(Color.parseColor(color_back));
            rl_add_to_cart.setBackgroundColor(Color.parseColor(color_back));

            btn_BuyNow = findViewById(R.id.btnBuyNow);

            tv_empty = findViewById(R.id.empty_review);
            tv_empty.setVisibility(View.GONE);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            (activity).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            rl_contaner.getLayoutParams().height = width;

            ratingBar = findViewById(R.id.cellRatingbar);
            addRatingbar = findViewById(R.id.rtRatingbar);
            addRatingbar.setRating(0);

            tv_regularPrice.setPaintFlags(tv_regularPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            tv_offer = findViewById(R.id.tvOffers);


            if (status.equals("1")) {
                ll_gstreview.setVisibility(View.GONE);
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
                ll_gstreview.setVisibility(View.VISIBLE);

                country = baseAddress_country;
                state = baseAddress_state;
                postcode = "";
                city = "";
            }

            if (enabel_review_rating.equals("yes")) {
                ll_ratingbar.setVisibility(View.VISIBLE);
            } else {
                ll_ratingbar.setVisibility(View.GONE);
            }


            product_id = AppConstant.PRODUCT_ID;
            product_name = AppConstant.productname;
            textViewTitle.setText(product_name);

            dataproduct = dataWrite.fetchProductList(AppConstant.shopview);


            if (dataWrite.isProductExist(product_id)) {

                AppDebugLog.println("product_id is== " + product_id);
                ll_attri.removeAllViews();
                ll_reviewList.removeAllViews();
                tv_stock.setVisibility(View.GONE);
                tv_attriWarn.setVisibility(View.GONE);
                textAttri.clear();
                selectList.clear();
                fetch_productDetail(product_id);
                fetch_reviews(product_id);
                productQuantity(product_id);

            } else {
                if (isNetworkAvailable()) {
                    displayProductDetail("detailProduct");
                    allReivews_call("reviews");
                    productQuantity(product_id);
                } else {
                    showNoInternetPromptDlg();
                }

            }

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
                            ll_attri.removeAllViews();
                            ll_reviewList.removeAllViews();
                            reivewseslist.clear();
                            tv_stock.setVisibility(View.GONE);
                            tv_attriWarn.setVisibility(View.GONE);
                            textAttri.clear();
                            selectList.clear();
                            if (isNetworkAvailable()) {
                                displayProductDetail("detailProduct");
                                allReivews_call("reviews");
                                productQuantity(product_id);
                            } else {
                                mSwipeRefreshLayout.setRefreshing(false);
                                showNoInternetPromptDlg();
                            }
                        }
                    }, 2500);
                }
            });


            btn_addToCart.setOnClickListener(onClickMethod);
            btn_BuyNow.setOnClickListener(onClickMethod);
            //tv_ViewAllreview.setOnClickListener(onClickMethod);
            addRatingbar.setOnClickListener(onClickMethod);
            iv_send.setOnClickListener(onClickMethod);
            tv_loadmore.setOnClickListener(onClickMethod);
            fullimage.setOnClickListener(onClickMethod);
            ic_wish.setOnClickListener(onClickMethod);
            iv_delete.setOnClickListener(onClickMethod);
            iv_add.setOnClickListener(onClickMethod);
            tv_pdf.setOnClickListener(onClickMethod);
            ll_back.setOnClickListener(this);
            rl_cart.setOnClickListener(this);
            iv_right_search.setOnClickListener(this);

        } catch (Exception e) {
            Log.e("ProductDetailActivity", ":Exception++++++++++++++++++2");
            e.printStackTrace();
        }

    }

    private void productQuantity(String product_id) {
        int quntity = dataWrite.fetchproductQuantity(product_id);
        etQnty_detail.setText("" + quntity);
    }


    private View.OnClickListener onClickMethod = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.tvPDF:

//                    openPDF(); change
                    try {
                        Intent salesPdfIntent = new Intent(Intent.ACTION_VIEW);
                        salesPdfIntent.setData(Uri.parse(
                                beanDetailProduct.getData().getProduct_detail()
                                        .getPdf_file()));
                        startActivity(salesPdfIntent);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                    break;

                case R.id.btnAddtoCart:

                    addtoCart();
                    //makeFlyAnimation(cartimage);

                    break;

                case R.id.icWish_detail:
                    addtowishlist();
                    break;

                case R.id.btnBuyNow:
                    buynow("buynow");
                    //addtoCart();
                    break;

                case R.id.rtRatingbar:

                    break;

                case R.id.tvLoadMore:
                    allReivews_call("reviews");
                    break;

                case R.id.ivSend:

                    saveReivews();

                    break;

                case R.id.fullImage:
                    fullimageDisplay(0);
                    break;

                case R.id.ivAdd_detail:

                    int t = Integer.parseInt(etQnty_detail.getText().toString());
                    int count = Integer.parseInt(String.valueOf(t + 1));

                    if (beanDetailProduct.getData().getProduct_detail().getIs_in_stock().equals("false")) {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                        builder1.setMessage(R.string.sorry_this_product_not_availabler);
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    } else {

                        if (beanDetailProduct.getData().getProduct_detail().getMax_qty() == null) {
                            etQnty_detail.setText("" + count);

                        } else {
                            int stock = 0;
                            if (beanDetailProduct.getData().getProduct_detail().getMax_qty().equals("")) {
                                stock = 0;
                            } else {
                                stock = Integer.parseInt(beanDetailProduct.getData().getProduct_detail().getMax_qty());
                            }
                            if (count > stock) {
                                dataWrite.showToast(getString(R.string.there_is_no_more_product_available));
                            } else {
                                etQnty_detail.setText("" + count);
                            }
                        }
                    }


                    break;

                case R.id.ivDelete_detail:
                    int count1;
                    int t1 = 0;
                    t1 = Integer.parseInt(etQnty_detail.getText().toString());
                    count1 = t1 - 1;

                    if (count1 <= 1) {
                        count1 = 1;
                        etQnty_detail.setText("" + count1);

                    } else {
                        etQnty_detail.setText("" + count1);
                    }

                    break;
            }
        }

        private void addtoCart() {
            try {
                if (beanDetailProduct.getData().getProduct_detail().getProduct_type().equals(PRODUCTTYPE_GROUPED)) {
                    if (isSimple) {
                        AppDebugLog.println("product id in group size==" + cartproducts.size());
                        if (cartproducts.size() != 0) {
                            for (int j = 0; j < cartproducts.size(); j++) {
                                AppDebugLog.println("product id in group =" + cartproducts.get(j).getProduct_id());

                                Log.e(TAG, "setImage== " + cartproducts.get(j).getImage());
                                Log.e(TAG, "setMax_stock== " + cartproducts.get(j).getMax_stock());
                                Log.e(TAG, "setOri_price== " + cartproducts.get(j).getOri_price());
                                Log.e(TAG, "setProduct_id== " + cartproducts.get(j).getProduct_id());
                                Log.e(TAG, "setProduct_name== " + cartproducts.get(j).getProduct_name());
                                Log.e(TAG, "setSubTotal== " + "" + Float.parseFloat(cartproducts.get(j).getSubTotal()) * Integer.parseInt("" + cartproducts.get(j).getQnty()));
                                Log.e(TAG, "setTax_status== " + cartproducts.get(j).getTax_status());
                                Log.e(TAG, "setVariation_price== " + cartproducts.get(j).getVariation_price());
                                Log.e(TAG, "setVariation_id== " + cartproducts.get(j).getVariations_id());
                                Log.e(TAG, "setVariations== ");
                                Log.e(TAG, "setQnty== " + "" + cartproducts.get(j).getQnty());

                                dataWrite.insert_cart(cartproducts.get(j).getProduct_id(), cartproducts.get(j).getProduct_name(), cartproducts.get(j).getVariations(),
                                        cartproducts.get(j).getVariations_id(), cartproducts.get(j).getVariation_price(), cartproducts.get(j).getQnty(),
                                        cartproducts.get(j).getImage(), cartproducts.get(j).getSubTotal(), cartproducts.get(j).getMax_stock(), cartproducts.get(j).getOri_price(),
                                        cartproducts.get(j).getTax_status());
                            }
                        } else {

                            dataWrite.showToast(getString(R.string.please_choose_the_quantity_of_items_you_wish_to_add_to_your_cart));
                        }


                    }


                } else {


                    product_id = beanDetailProduct.getData().getProduct_detail().getId();
                    product_name = beanDetailProduct.getData().getProduct_detail().getTitle();
                    image = beanDetailProduct.getData().getProduct_detail().getImage();
                    product_type = beanDetailProduct.getData().getProduct_detail().getProduct_type().toString();
                    tax_status = beanDetailProduct.getData().getProduct_detail().getTax_status().toString();

                    //variation_price = beanDetailProduct.getData().getProduct_detail().getPrice().toString();
                    qnty = etQnty_detail.getText().toString();
                    //Log.d("shailesh", "max_stock===   "+max_stock);

                    if (product_type.equals(PRODUCTTYPE_SIMPLE)) {

                        variations_id = null;
                        variations = null;
                        subTotal = "" + Float.parseFloat(beanDetailProduct.getData().getProduct_detail().getCart_sale_price()) * Integer.parseInt(qnty);
                        Log.d("subTotal", "subTotal===   " + subTotal);
                        variation_price = beanDetailProduct.getData().getProduct_detail().getCart_sale_price().toString();
                        max_stock = beanDetailProduct.getData().getProduct_detail().getMax_qty();
                        Log.e(TAG, "setMax_stock== " + beanDetailProduct.getData().getProduct_detail().getMax_qty());
                        ori_price = beanDetailProduct.getData().getProduct_detail().getOrigional_price().toString();

                        dataWrite.insert_cart(product_id, product_name, variations, variations_id, variation_price, qnty, image, subTotal, max_stock, ori_price, tax_status);

                    } else if (product_type.equals(PRODUCTTYPE_VARIABLE)) {
                        String variations = "";
                        for (int i = 0; i < selectList.size(); i++) {

                            variations = variations.concat(" " + selectList.get(i));
                        }


                        if (variations_id == null || selectList.size() < beanDetailProduct.getData().getProduct_detail().getVariant().size()) {

                            dataWrite.showToast(getString(R.string.please_select_variations));
                        } else {

                            subTotal = "" + Float.parseFloat(subTotal) * Integer.parseInt(qnty);
                            dataWrite.insert_cart(product_id, product_name, variations, variations_id, variation_price, qnty, image, subTotal, max_stock, ori_price, tax_status);
                        }

                    } else if (product_type.equals(PRODUCTTYPE_EXTERNAL)) {

                        Log.d("Product", "new product type===   " + product_type);
                        if (beanDetailProduct.getData().getProduct_detail().getProduct_url().length() != 0) {

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(beanDetailProduct.getData().getProduct_detail().getProduct_url()));
                            startActivity(browserIntent);
                        }


                    }


                }
            } catch (Exception e) {
                Log.e("ProductDetailActivity", ":Exception++++++++++++++++++3");
                e.printStackTrace();
            }


        }
    };


    private void addtowishlist() {
        try {
            String id, product_name, description, price, image;
            id = beanDetailProduct.getData().getProduct_detail().getId();
            product_name = beanDetailProduct.getData().getProduct_detail().getTitle();
            description = beanDetailProduct.getData().getProduct_detail().getDescription();
            price = tv_salePrice.getText().toString();
            image = beanDetailProduct.getData().getProduct_detail().getImage();

            if (dataWrite.isWishExist(beanDetailProduct.getData().getProduct_detail().getId())) {
                ic_wish.setImageResource(R.drawable.ic_heart);
                dataWrite.deleteWish(beanDetailProduct.getData().getProduct_detail().getId());

            } else {

                ic_wish.setImageResource(R.drawable.ic_heart_red);
                dataWrite.Insert_wish(id, product_name, description, price, image);
            }
        } catch (Exception e) {
            Log.e("ProductDetailActivity", ":Exception++++++++++++++++++4");
            e.printStackTrace();
        }

    }

    private void fullimageDisplay(int position) {
        MyFullImagePagesAdapter myPagesAdapter;
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.full_image);

        ViewPager vp = dialog.findViewById(R.id.viewPager_full);
        CirclePageIndicator mIndicator = dialog.findViewById(R.id.indicator_full);
        ImageView backarrow = dialog.findViewById(R.id.icBackarro_full);

        List<String> URL_LIST = beanDetailProduct.getData().getProduct_detail().getGallery_img();
        if (URL_LIST.size() == 0) {
            URL_LIST.add(0, beanDetailProduct.getData().getProduct_detail().getImage());
            //initView();
            myPagesAdapter = new MyFullImagePagesAdapter(URL_LIST);
            vp.setAdapter(myPagesAdapter);
            vp.setCurrentItem(position);
            mIndicator.setViewPager(vp);
            myPagesAdapter.notifyDataSetChanged();
        } else {
            // initView();
            myPagesAdapter = new MyFullImagePagesAdapter(URL_LIST);
            vp.setAdapter(myPagesAdapter);
            vp.setCurrentItem(position);
            mIndicator.setViewPager(vp);
            myPagesAdapter.notifyDataSetChanged();
        }

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        Window window = dialog.getWindow();
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.show();

    }

    private void saveReivews() {
        try {
            String rating = String.valueOf(addRatingbar.getRating());
            String content = null;

            if (status.equals("1")) {

                if (enabel_review_rating.equals("yes")) {

                    if (et_review.getText().toString().trim().equalsIgnoreCase("")) {
                        et_review.setError(getString(R.string.please_enter_review));

                    } else if (rating.equals("0.0")) {
                        //Toast.makeText(activity, "please select rating", Toast.LENGTH_LONG).show();
                        dataWrite.showToast(getString(R.string.please_select_rating));

                    } else {
                        content = et_review.getText().toString();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("comment_post_id", product_id);
                            jsonObject.put("comment_author", name);
                            jsonObject.put("comment_author_email", email);
                            jsonObject.put("comment_content", content);
                            jsonObject.put("user_id", user_id);
                            jsonObject.put("rating", rating);

                            if (status.equals("1")) {
                                jsonObject.put("comment_approved", status);
                            } else {
                                jsonObject.put("comment_approved", status);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_SAVE_REVIEWS;
                        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
                        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
                        requestTask.delegate = this;
                        requestTask.execute(requestURL, "saveReview", jsonObject.toString());

                    }
                } else {

                    if (et_review.getText().length() == 0) {
                        et_review.setError(getString(R.string.please_enter_review));

                    } else {
                        content = et_review.getText().toString();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("comment_post_id", product_id);
                            jsonObject.put("comment_author", name);
                            jsonObject.put("comment_author_email", email);
                            jsonObject.put("comment_content", content);
                            jsonObject.put("user_id", user_id);
                            jsonObject.put("rating", "");

                            if (status.equals("1")) {
                                jsonObject.put("comment_approved", status);
                            } else {
                                jsonObject.put("comment_approved", status);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_SAVE_REVIEWS;
                        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
                        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
                        requestTask.delegate = this;
                        requestTask.execute(requestURL, "saveReview", jsonObject.toString());

                    }

                }


            } else {

                if (enabel_review_rating.equals("yes")) {

                    if (et_review.getText().toString().trim().equalsIgnoreCase("")) {
                        et_review.setError(getString(R.string.please_enter_review));

                    } else if (et_gstName.getText().toString().trim().equalsIgnoreCase("")) {
                        et_gstName.setError(getString(R.string.please_enter_name));

                    } else if (et_gstEmail.getText().toString().trim().equalsIgnoreCase("")) {
                        et_gstEmail.setError(getString(R.string.please_enter_email));

                    } else if (rating.equals("0.0")) {
                        dataWrite.showToast(getString(R.string.please_select_rating));
                    } else {
                        content = et_review.getText().toString();


                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("comment_post_id", product_id);
                            jsonObject.put("comment_author", et_gstName.getText().toString());
                            jsonObject.put("comment_author_email", et_gstEmail.getText().toString());
                            jsonObject.put("comment_content", content);
                            jsonObject.put("user_id", status);
                            jsonObject.put("rating", rating);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_SAVE_REVIEWS;
                        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
                        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
                        requestTask.delegate = this;
                        requestTask.execute(requestURL, "saveReview", jsonObject.toString());

                    }
                } else {

                    if (et_review.getText().toString().trim().equalsIgnoreCase("")) {
                        et_review.setError(getString(R.string.please_enter_review));

                    } else if (et_gstName.getText().toString().trim().equalsIgnoreCase("")) {
                        et_gstName.setError(getString(R.string.please_enter_name));

                    } else if (et_gstEmail.getText().toString().trim().equalsIgnoreCase("")) {
                        et_gstEmail.setError(getString(R.string.please_enter_email));

                    } else {
                        content = et_review.getText().toString();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("comment_post_id", product_id);
                            jsonObject.put("comment_author", et_gstName.getText().toString());
                            jsonObject.put("comment_author_email", et_gstEmail.getText().toString());
                            jsonObject.put("comment_content", content);
                            jsonObject.put("user_id", status);
                            jsonObject.put("rating", "");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_SAVE_REVIEWS;
                        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
                        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
                        requestTask.delegate = this;
                        requestTask.execute(requestURL, "saveReview", jsonObject.toString());

                    }
                }


            }
        } catch (Exception e) {
            Log.e("ProductDetailActivity", ":Exception++++++++++++++++++5");
            e.printStackTrace();
        }

    }

    private void buynow(String buynow) {
        try {
            product_id = beanDetailProduct.getData().getProduct_detail().getId();
            product_name = beanDetailProduct.getData().getProduct_detail().getTitle();
            image = beanDetailProduct.getData().getProduct_detail().getImage();
            product_type = beanDetailProduct.getData().getProduct_detail().getProduct_type().toString();
            tax_status = beanDetailProduct.getData().getProduct_detail().getTax_status().toString();

            //variation_price = beanDetailProduct.getData().getProduct_detail().getPrice().toString();
            qnty = etQnty_detail.getText().toString();
            //Log.d("shailesh", "max_stock===   "+max_stock);

            if (product_type.equals("simple")) {
                variations_id = null;
                variations = null;
//            subTotal = beanDetailProduct.getData().getProduct_detail().getCart_sale_price(); // chnage
                subTotal = "" + Float.parseFloat(beanDetailProduct.getData().getProduct_detail().getCart_sale_price()) * Integer.parseInt(qnty);
                variation_price = beanDetailProduct.getData().getProduct_detail().getCart_sale_price().toString();
                max_stock = beanDetailProduct.getData().getProduct_detail().getMax_qty();
                ori_price = beanDetailProduct.getData().getProduct_detail().getOrigional_price().toString();
                dataWrite.insert_cart(product_id, product_name, variations, variations_id, variation_price, qnty, image, subTotal, max_stock, ori_price, tax_status);

/*            Fragment fr = new ShoppingCartFragment();
            String title = getString(R.string.title_shoppingcart);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment current = fm.findFragmentByTag(title);
            if (current != null) {
                ft.remove(current);

            }
            ft.addToBackStack(title);
            ft.replace(R.id.container_body, fr);
            ft.commit();
            ((PayUmoneyActivity) activity).Title(title);*/
                startActivity(new Intent(activity, CartActivity.class));

            } else if (product_type.equals("variable")) {
                String variations = "";
                for (int i = 0; i < selectList.size(); i++) {

                    variations = variations.concat(" " + selectList.get(i));
                }

                if (variations_id == null || selectList.size() < beanDetailProduct.getData().getProduct_detail().getVariant().size()) {
                    // Toast toast= Toast.makeText(activity,"Please select variations",Toast.LENGTH_SHORT);
                    // toast.show();
                    dataWrite.showToast(getString(R.string.please_select_variations));

                } else {

                    dataWrite.insert_cart(product_id, product_name, variations, variations_id, variation_price, qnty, image, subTotal, max_stock, ori_price, tax_status);
             /*   Fragment fr = new ShoppingCartFragment();
                String title = getString(R.string.title_shoppingcart);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment current = fm.findFragmentByTag(title);
                if (current != null) {
                    ft.remove(current);
                }
                ft.addToBackStack(title);
                ft.replace(R.id.container_body, fr);
                ft.commit();
                ((PayUmoneyActivity) activity).Title(title);*/
                    startActivity(new Intent(activity, CartActivity.class));
                }
            }
        } catch (Exception e) {
            Log.e("ProductDetailActivity", ":Exception++++++++++++++++++6");
            e.printStackTrace();
        }

    }


    private void refreshPorductList(String products) {

        progressDialog.show();
        JSONObject jsonObject = new JSONObject();

        try {

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

    private void allReivews_call(String reviews) {
        progressDialog.show();
        ll_reviewList.removeAllViews();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", product_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_ALL_REVIEWS;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, "reviews", jsonObject.toString());

    }

    private void displayProductDetail(String detailProduct) {
        progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("theid", product_id);
            jsonObject.put("country", country);
            jsonObject.put("state", state);
            jsonObject.put("postcode", postcode);
            jsonObject.put("city", city);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_PRODUCT_DETAIL;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, "productDetail", jsonObject.toString());


    }


    private void fetch_reviews(String product_id) {
        try {
            mSwipeRefreshLayout.setRefreshing(false);
            String data = dataWrite.fetchProductReviews(product_id);
            reivewseslist = new ArrayList<BeanAllReivews>();

            AppDebugLog.println("data==-----" + data);
            if (data.length() != 0) {
                JSONObject object;
                try {
                    object = new JSONObject(data);

                    JSONObject jsonObject = object.optJSONObject("data");
                    JSONArray jsonArray;
                    reivewseslist.clear();
                    if (jsonObject.has("review")) {
                        jsonArray = jsonObject.optJSONArray("review");
                        if (jsonArray.length() != 0) {
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject object1 = jsonArray.optJSONObject(j);
                                BeanAllReivews bean = new BeanAllReivews();
                                bean.setReviewID(object1.getString("review_ID"));
                                bean.setReviewPostID(object1.getString("review_post_ID"));
                                bean.setRating(object1.getString("rating"));
                                bean.setReviewAuthor(object1.getString("review_author"));
                                bean.setReviewAuthorEmail(object1.getString("review_author_email"));
                                bean.setReviewDate(object1.getString("review_date"));
                                bean.setReviewContent(object1.getString("review_content"));
                                bean.setReviewUserId(object1.getString("review_user_id"));
                                reivewseslist.add(bean);

                            }

                            TextView tv_UserName, tv_date, tv_detail;
                            RatingBar ratingBar1 = null;


                            AppDebugLog.println("list siae==" + reivewseslist.size() + "\n count=" + count);
                            if (reivewseslist.size() == 0) {
                                tv_empty.setVisibility(View.VISIBLE);
                                tv_loadmore.setVisibility(View.GONE);

                            } else {
                                tv_reviews.setText(reivewseslist.size() + " Reviews");
                                tv_empty.setVisibility(View.GONE);
                                tv_loadmore.setVisibility(View.GONE);
                                tv_reviewcount.setText(reivewseslist.size() + " " + getString(R.string.review_for) + " " + product_name);


                                for (int i = 0; i < reivewseslist.size(); i++) {

                                    final View view = inflater_review.inflate(R.layout.inflater_reviews, null);
                                    tv_UserName = view.findViewById(R.id.tvUserNameReview);
                                    tv_date = view.findViewById(R.id.tvDateReview);
                                    tv_detail = view.findViewById(R.id.tvReivewDetail);
                                    ratingBar1 = view.findViewById(R.id.RatingbarReview);

                                    tv_UserName.setText(reivewseslist.get(i).getReviewAuthor());
                                    tv_date.setText(reivewseslist.get(i).getReviewDate());
                                    tv_detail.setText(reivewseslist.get(i).getReviewContent());

                                    String r = reivewseslist.get(i).getRating();
                                    if (r.equals("")) {
                                        ratingBar1.setRating(0);

                                    } else {

                                        float finalrate1 = Float.parseFloat(r);
                                        // Log.e("TEST","list finalrate=="+finalrate);
                                        ratingBar1.setRating(finalrate1);

                                        AppDebugLog.println("list finalrate1==" + ratingBar1.getRating());
                                    }

                                    ll_reviewList.addView(view);

                                }

                            }

                        } else {

                            dataWrite.showToast(getString(R.string.no_review_for_this_prodcut));

                        }
                    }


                    if (jsonObject.has("msg")) {
                        String message = jsonObject.getString("msg");
                            /*Toast.makeText(activity, message,
                                    Toast.LENGTH_LONG).show();*/
                        // dataWrite.showToast(message);
                    }

                    if (jsonObject.has("first_review")) {
                        String first_review = jsonObject.getString("first_review");
                        tv_empty.setVisibility(View.VISIBLE);
                        tv_empty.setText(first_review);
                    }

                    progressDialog.dismiss();

                } catch (JSONException e) {
                    // TODO: handle exception


                    dataWrite.showToast("error:" + e);
                    progressDialog.dismiss();
                }
            } else {
                //dataWrite.showToast("empty reivews");
            }
        } catch (Exception e) {
            Log.e("ProductDetailActivity", ":Exception++++++++++++++++++7");
            e.printStackTrace();
        }


    }

    private void fetch_productDetail(String product_id) {
        String data = dataWrite.fetchProductDetail(product_id);

        AppDebugLog.println("detailProduct is== " + data);

        try {
            beanDetailProduct = new BeanDetailProduct();
            Gson gson = new Gson();
            if (data != null) {
                Type type_one = new TypeToken<BeanDetailProduct>() {
                }.getType();
                beanDetailProduct = gson.fromJson(data, type_one);

                if (beanDetailProduct.getSuccess().equals("0")) {
                    //Toast.makeText(ApplicationContext.getAppContext(),"error to fetch data",Toast.LENGTH_SHORT).show();
                    dataWrite.showToast(getString(R.string.error_to_fetch_data));
                } else {

                    URL_LIST = beanDetailProduct.getData().getProduct_detail().getGallery_img();
                    if (URL_LIST.size() == 0) {
                        URL_LIST.add(0, beanDetailProduct.getData().getProduct_detail().getImage());
                        if (beanDetailProduct.getData().getProduct_detail().getYoutube_url().length() != 0) {
                            URL_LIST.add(1, beanDetailProduct.getData().getProduct_detail().getYoutube_url());
                        }
                        //initView();
                        myPagesAdapter = new MyPagesAdapter(URL_LIST);
                        vp.setAdapter(myPagesAdapter);
                        mIndicator.setViewPager(vp);
                        myPagesAdapter.notifyDataSetChanged();
                    } else {
                        // initView();
                        if (beanDetailProduct.getData().getProduct_detail().getYoutube_url().length() != 0) {
                            URL_LIST.add(URL_LIST.size(), beanDetailProduct.getData().getProduct_detail().getYoutube_url());
                        }
                        myPagesAdapter = new MyPagesAdapter(URL_LIST);

                        vp.setAdapter(myPagesAdapter);
                        mIndicator.setViewPager(vp);
                        myPagesAdapter.notifyDataSetChanged();
                    }
                    // Log.d("TEST", "detailProduct is== ");
                    //String curr = beanDetailProduct.getData().getProduct_detail().getCurrency_symbol();

                    String pdf = beanDetailProduct.getData().getProduct_detail().getPdf_file();
                    if (pdf.trim().length() == 0) {
                        cardPDF.setVisibility(View.GONE);
                    } else {
                        cardPDF.setVisibility(View.VISIBLE);
                    }


                    String display_review = beanDetailProduct.getData().getProduct_detail().getDisplay_review();
                    if (display_review.equals("closed")) {
                        cardAddreview.setVisibility(View.GONE);
                        cardReview.setVisibility(View.GONE);
                    } else {
                        cardAddreview.setVisibility(View.VISIBLE);
                        cardReview.setVisibility(View.VISIBLE);
                    }

                    product_id = beanDetailProduct.getData().getProduct_detail().getId();
                    product_name = beanDetailProduct.getData().getProduct_detail().getTitle();
                    image = beanDetailProduct.getData().getProduct_detail().getImage();
                    product_type = beanDetailProduct.getData().getProduct_detail().getProduct_type().toString();

                    tv_tax.setText(beanDetailProduct.getData().getProduct_detail().getSuffix());
                    tv_shortDescripn.setText(beanDetailProduct.getData().getProduct_detail().getTitle());
//                    tv_fullDescription.setText(Html.fromHtml(beanDetailProduct.getData().getProduct_detail().getDescription())); // change
                    tv_fullDescription.setText(beanDetailProduct.getData().getProduct_detail().getDescription());
                    tv_reviews.setText(beanDetailProduct.getData().getProduct_detail().getReview() + " Reviews");
                    String rate = beanDetailProduct.getData().getProduct_detail().getRating();


                    TextView tv_var;
                    LinearLayout ll_variable;

                    LayoutInflater in_variable = (LayoutInflater) activity.getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
                    in_variable = LayoutInflater.from(activity);
                    if (dataWrite.isWishExist(product_id)) {
                        ic_wish.setImageResource(R.drawable.ic_heart_red);
                    } else {
                        ic_wish.setImageResource(R.drawable.ic_heart);
                    }

                    if (beanDetailProduct.getData().getProduct_detail().getProduct_type().equals(PRODUCTTYPE_SIMPLE)) {
                        cardAtri.setVisibility(View.GONE);

                        if (beanDetailProduct.getData().getProduct_detail().getIs_in_stock().equals("false")) {

                            tv_stock.setVisibility(View.VISIBLE);
                            tv_stock.setTextColor(getResources().getColor(R.color.color_red));
                            btn_addToCart.setClickable(false);
                            btn_BuyNow.setClickable(false);

                            btn_addToCart.setBackgroundColor(getResources().getColor(R.color.color_perpal));
                            btn_BuyNow.setBackgroundColor(getResources().getColor(R.color.color_disable_white));

                            if (beanDetailProduct.getData().getProduct_detail().getCart_sale_price().equals("")) {
                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                tv_salePrice.setVisibility(View.INVISIBLE);

                            } else {
                                tv_regularPrice.setVisibility(View.VISIBLE);
                                tv_salePrice.setVisibility(View.VISIBLE);
                            }


                        } else {

                            tv_stock.setVisibility(View.VISIBLE);
                            tv_stock.setTextColor(getResources().getColor(R.color.color_green));

                            if (beanDetailProduct.getData().getProduct_detail().getCart_sale_price().equals("")) {
                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                tv_salePrice.setVisibility(View.INVISIBLE);
                                btn_addToCart.setClickable(false);
                                btn_BuyNow.setClickable(false);

                                btn_addToCart.setBackgroundColor(getResources().getColor(R.color.color_perpal));
                                btn_BuyNow.setBackgroundColor(getResources().getColor(R.color.color_disable_white));

                            } else {

                                tv_regularPrice.setVisibility(View.VISIBLE);
                                tv_salePrice.setVisibility(View.VISIBLE);
                                btn_addToCart.setClickable(true);
                                btn_BuyNow.setClickable(true);

                                btn_addToCart.setBackgroundColor(Color.parseColor(color_back));
                                btn_BuyNow.setBackgroundColor(getResources().getColor(R.color.color_white));

                            }
                        }


                        tv_stock.setText(beanDetailProduct.getData().getProduct_detail().getAvailability_html());

                        /* set price */
                        if (beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_regula().equals("")) {

                            if (beanDetailProduct.getData().getProduct_detail().getSale_price().equals("")) {
                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                tv_offer.setVisibility(View.INVISIBLE);
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getRegular_price());
                            } else {
                                tv_offer.setVisibility(View.VISIBLE);
                                tv_regularPrice.setVisibility(View.VISIBLE);


                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                tv_regularPrice.setText(beanDetailProduct.getData().getProduct_detail().getRegular_price());
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getSale_price());

                            }


                        } else {

                            if (beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_sale().equals("")) {
                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                tv_offer.setVisibility(View.INVISIBLE);
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_regula());
                            } else {
                                tv_offer.setVisibility(View.VISIBLE);
                                tv_regularPrice.setVisibility(View.VISIBLE);


                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                tv_regularPrice.setText(beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_regula());
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_sale());

                            }

                        }

                    } else if (beanDetailProduct.getData().getProduct_detail().getProduct_type().equals(PRODUCTTYPE_VARIABLE)) {

                        cardAtri.setVisibility(View.VISIBLE);


                        if (beanDetailProduct.getData().getProduct_detail().getRegular_tax_price().equals("")) {

                            if (beanDetailProduct.getData().getProduct_detail().getSale_price().equals("")) {
                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                tv_offer.setVisibility(View.INVISIBLE);
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getRegular_price());
                            } else {
                                tv_offer.setVisibility(View.VISIBLE);
                                tv_regularPrice.setVisibility(View.VISIBLE);

                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                tv_regularPrice.setText(beanDetailProduct.getData().getProduct_detail().getRegular_price());
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getSale_price());
                            }

                        } else {


                            if (beanDetailProduct.getData().getProduct_detail().getSale_tax_price().equals("")) {
                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                tv_offer.setVisibility(View.INVISIBLE);
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getRegular_tax_price());
                            } else {
                                tv_offer.setVisibility(View.VISIBLE);
                                tv_regularPrice.setVisibility(View.VISIBLE);

                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                tv_regularPrice.setText(beanDetailProduct.getData().getProduct_detail().getRegular_tax_price());
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getSale_tax_price());
                            }

                        }


                        if (beanDetailProduct.getData().getProduct_detail().getVariant().size() != 0) {

                            for (int i = 0; i < beanDetailProduct.getData().getProduct_detail().getVariant().size(); i++) {
                                final View view = inflater_attri.inflate(R.layout.inflater_attributes, null);

                                AppDebugLog.println("atrributes  is== " + i);
                                tv_var = view.findViewById(R.id.tvAttribute);
                                //ll_variable = view.findViewById(R.id.llVariable);
                                tv_var.setHint(beanDetailProduct.getData().getProduct_detail().getVariant().get(i).getKey());

                                textAttri.add(tv_var);


                                final TextView finalTv_var = tv_var;
                                final int finalI = i;
                                final TextView finalTv_var1 = tv_var;
                                tv_var.setOnClickListener(new View.OnClickListener() {
                                    @TargetApi(Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onClick(View v) {
                                        final ArrayList<Variant> variant = beanDetailProduct.getData().getProduct_detail().getVariant();
                                        final ArrayList<Variations> variationses = beanDetailProduct.getData().getProduct_detail().getVariations();

                                        selectList.clear();

                                        AppDebugLog.println("On click List size-----------" + selectList.size());
                                        LayoutInflater layoutInflater1
                                                = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
                                        final View popupView1 = layoutInflater1.inflate(R.layout.variation_listview, null);
                                        final PopupWindow popupWindow1 = new PopupWindow(
                                                popupView1,
                                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT, true);

                                        popupWindow1.setOutsideTouchable(true);
                                        popupWindow1.setAnimationStyle(R.style.PopupAnimation);
                                        ListView lv1 = (ListView) popupView1.findViewById(R.id.variationListView);
                                        RelativeLayout rl = (RelativeLayout) popupView1.findViewById(R.id.rlVariation);
                                        rl.setBackgroundColor(Color.parseColor(color_back));

                                        VariationsColorAdapter variationscolorAdapter = new VariationsColorAdapter(activity,
                                                beanDetailProduct.getData().getProduct_detail().getVariant().get(finalI).getValue());
                                        lv1.setAdapter(variationscolorAdapter);
                                        popupWindow1.showAtLocation(finalTv_var, Gravity.CENTER, 0, 0);


                                        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                URL_LIST.remove(0);
                                                Log.e("parent size", "" + URL_LIST.size());
                                                for (int i = 0; i < URL_LIST.size(); i++) {
                                                    Log.e("child size", "" + i);
                                                    Log.e("add image", "" + URL_LIST.get(i).toString());
                                                    URL_LIST2.add(URL_LIST.get(i).toString());
                                                }
                                                URL_LIST.clear();
                                                for (int i = 0; i < URL_LIST2.size(); i++) {
                                                    URL_LIST.add(URL_LIST2.get(i).toString());
                                                }
                                                URL_LIST2.clear();
                                                URL_LIST.add(0, variationses.get(position).getImage_src());
                                                Log.e("URL_LIST size", "" + URL_LIST.size());
                                                myPagesAdapter = new MyPagesAdapter(URL_LIST);
                                                vp.setAdapter(myPagesAdapter);
                                                mIndicator.setViewPager(vp);
                                                myPagesAdapter.notifyDataSetChanged();

                                                //Log.e(TAG, "On click selectList= "+variant.get(finalI).getValue().get(position));
                                                finalTv_var1.setText(variant.get(finalI).getValue().get(position));
                                                textAttri.get(finalI).setText(variant.get(finalI).getValue().get(position));


                                                for (int j = 0; j < textAttri.size(); j++) {

                                                    TextView s = textAttri.get(j);

                                                    selectList.add((String) s.getText());

                                                }


                                                // Log.e(TAG, "On click selectList= "+selectList.size());


                                                for (int k = 0; k < variationses.size(); k++) {


                                                    if ((equalLists(selectList, variationses.get(k).getAttributes())) && selectedAtri(textAttri)) {
                                                        btn_addToCart.setClickable(true);
                                                        btn_BuyNow.setClickable(true);

                                                        btn_addToCart.setBackgroundColor(Color.parseColor(color_back));
                                                        btn_BuyNow.setBackgroundColor(getResources().getColor(R.color.color_white));
                                                        tv_attriWarn.setVisibility(View.GONE);


                                                        if (variationses.get(k).getRegular_tax_price().equals("")) {

                                                            if (variationses.get(k).getDisplay_price().equals("")) {
                                                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                                                tv_offer.setVisibility(View.INVISIBLE);

                                                                tv_salePrice.setText(variationses.get(k).getDisplay_regular_price());
                                                            } else {
                                                                tv_offer.setVisibility(View.VISIBLE);
                                                                tv_regularPrice.setVisibility(View.VISIBLE);

                                                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                                                tv_regularPrice.setText(variationses.get(k).getDisplay_regular_price());
                                                                tv_salePrice.setText(variationses.get(k).getDisplay_price());

                                                            }


                                                        } else {

                                                            if (variationses.get(k).getDisplay_tax_price().equals("")) {
                                                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                                                tv_offer.setVisibility(View.INVISIBLE);

                                                                tv_salePrice.setText(variationses.get(k).getRegular_tax_price());
                                                            } else {
                                                                tv_offer.setVisibility(View.VISIBLE);
                                                                tv_regularPrice.setVisibility(View.VISIBLE);

                                                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                                                tv_regularPrice.setText(variationses.get(k).getRegular_tax_price());
                                                                tv_salePrice.setText(variationses.get(k).getDisplay_tax_price());

                                                            }
                                                        }

                                                        if (variationses.get(k).getIs_in_stock().equals("false")) {

                                                            tv_stock.setVisibility(View.VISIBLE);
                                                            tv_stock.setTextColor(getResources().getColor(R.color.color_red));
                                                            btn_addToCart.setClickable(false);
                                                            btn_BuyNow.setClickable(false);

                                                            btn_addToCart.setBackgroundColor(getResources().getColor(R.color.color_perpal));
                                                            btn_BuyNow.setBackgroundColor(getResources().getColor(R.color.color_disable_white));


                                                            if (variationses.get(k).getCart_sale_price().equals("")) {
                                                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                                                tv_salePrice.setVisibility(View.INVISIBLE);

                                                            } else {

                                                                tv_regularPrice.setVisibility(View.VISIBLE);
                                                                tv_salePrice.setVisibility(View.VISIBLE);
                                                            }

                                                        } else {
                                                            tv_stock.setVisibility(View.VISIBLE);
                                                            tv_stock.setTextColor(getResources().getColor(R.color.color_green));

                                                            if (variationses.get(k).getCart_sale_price().equals("")) {
                                                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                                                tv_salePrice.setVisibility(View.INVISIBLE);
                                                                btn_addToCart.setClickable(false);
                                                                btn_BuyNow.setClickable(false);

                                                                btn_addToCart.setBackgroundColor(getResources().getColor(R.color.color_perpal));
                                                                btn_BuyNow.setBackgroundColor(getResources().getColor(R.color.color_disable_white));

                                                            } else {

                                                                tv_regularPrice.setVisibility(View.VISIBLE);
                                                                tv_salePrice.setVisibility(View.VISIBLE);
                                                                btn_addToCart.setClickable(true);
                                                                btn_BuyNow.setClickable(true);

                                                                btn_addToCart.setBackgroundColor(Color.parseColor(color_back));
                                                                btn_BuyNow.setBackgroundColor(getResources().getColor(R.color.color_white));

                                                            }

                                                        }

                                                        if (variationses.get(k).getMax_qty() == null) {
                                                            max_stock = variationses.get(k).getMax_qty();
                                                        } else {
                                                            max_stock = variationses.get(k).getMax_qty();
                                                        }

                                                        tv_stock.setText(variationses.get(k).getAvailability_html());

                                                        variation_price = variationses.get(k).getCart_sale_price();
                                                        subTotal = variation_price;
                                                        ori_price = variationses.get(k).getOrigional_price();

                                                        variations_id = variationses.get(k).getVariation_id();
                                                        break;

                                                    } else if (variationses.get(k).getAttributes().size() == 0) {

                                                        btn_addToCart.setClickable(true);
                                                        btn_BuyNow.setClickable(true);

                                                        btn_addToCart.setBackgroundColor(Color.parseColor(color_back));
                                                        btn_BuyNow.setBackgroundColor(getResources().getColor(R.color.color_white));
                                                        tv_attriWarn.setVisibility(View.GONE);

                                                        if (variationses.get(k).getRegular_tax_price().equals("")) {

                                                            if (variationses.get(k).getDisplay_price().equals("")) {
                                                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                                                tv_offer.setVisibility(View.INVISIBLE);

                                                                tv_salePrice.setText(variationses.get(k).getDisplay_regular_price());
                                                            } else {
                                                                tv_offer.setVisibility(View.VISIBLE);
                                                                tv_regularPrice.setVisibility(View.VISIBLE);

                                                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                                                tv_regularPrice.setText(variationses.get(k).getDisplay_regular_price());
                                                                tv_salePrice.setText(variationses.get(k).getDisplay_price());

                                                            }


                                                        } else {

                                                            if (variationses.get(k).getDisplay_tax_price().equals("")) {
                                                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                                                tv_offer.setVisibility(View.INVISIBLE);

                                                                tv_salePrice.setText(variationses.get(k).getRegular_tax_price());
                                                            } else {
                                                                tv_offer.setVisibility(View.VISIBLE);
                                                                tv_regularPrice.setVisibility(View.VISIBLE);

                                                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                                                tv_regularPrice.setText(variationses.get(k).getRegular_tax_price());
                                                                tv_salePrice.setText(variationses.get(k).getDisplay_tax_price());

                                                            }
                                                        }

                                                        if (variationses.get(k).getIs_in_stock().equals("false")) {

                                                            tv_stock.setVisibility(View.VISIBLE);
                                                            tv_stock.setTextColor(getResources().getColor(R.color.color_red));
                                                            btn_addToCart.setClickable(false);
                                                            btn_BuyNow.setClickable(false);

                                                            btn_addToCart.setBackgroundColor(getResources().getColor(R.color.color_perpal));
                                                            btn_BuyNow.setBackgroundColor(getResources().getColor(R.color.color_disable_white));


                                                        } else {


                                                            tv_stock.setVisibility(View.VISIBLE);
                                                            tv_stock.setTextColor(getResources().getColor(R.color.color_green));
                                                            btn_addToCart.setClickable(true);
                                                            btn_BuyNow.setClickable(true);

                                                            btn_addToCart.setBackgroundColor(Color.parseColor(color_back));
                                                            btn_BuyNow.setBackgroundColor(getResources().getColor(R.color.color_white));

                                                        }

                                                        if (variationses.get(k).getMax_qty() == null) {
                                                            max_stock = variationses.get(k).getMax_qty();
                                                        } else {
                                                            max_stock = variationses.get(k).getMax_qty();
                                                        }

                                                        tv_stock.setText(variationses.get(k).getAvailability_html());


                                                        variation_price = variationses.get(k).getCart_sale_price();
                                                        subTotal = variation_price;
                                                        ori_price = variationses.get(k).getOrigional_price();

                                                        variations_id = variationses.get(k).getVariation_id();


                                                        /*implement various condition for tax on regular price*/


                                                    } else {

                                                        tv_attriWarn.setVisibility(View.VISIBLE);
                                                        btn_addToCart.setClickable(false);
                                                        btn_BuyNow.setClickable(false);

                                                        btn_addToCart.setBackgroundColor(getResources().getColor(R.color.color_perpal));
                                                        btn_BuyNow.setBackgroundColor(getResources().getColor(R.color.color_disable_white));

                                                    }

                                                }


                                                popupWindow1.dismiss();
                                            }
                                        });


                                    }
                                });


                                //TextView tvSize;
/*
                                            for (int j =0;j<beanDetailProduct.getData().getProduct_detail().getVariant().get(i).getValue().size();j++){
                                                final View view1 = in_variable.inflate(R.layout.inflater_variable,null);
                                                tvSize = view1.findViewById(R.id.tvAttri_product);

                                                tvSize.setText(beanDetailProduct.getData().getProduct_detail().getVariant().get(i).getValue().get(j));

                                                final int finalI = i;
                                                final int finalJ = j;
                                                final TextView finalTvSize = tvSize;

                                                tvSize.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        ArrayList<Variant> variant = beanDetailProduct.getData().getProduct_detail().getVariant();
                                                        ArrayList<Variations> variationses = beanDetailProduct.getData().getProduct_detail().getVariations();


                                                        //Log.e(TAG, "On click variable= "+variant.get(finalI).getKey());
                                                        finalTvSize.setBackground(getResources().getDrawable(R.drawable.spinner_press));

                                                    *//*    for (int l =0;l<variant.size();l++){
                                                            Log.e(TAG, "On click key = "+variant.get(l).getKey());
                                                            Log.e(TAG, "On click key------- "+variant.get(finalI).getKey());
                                                            if (variant.get(finalI).getKey().equals(variant.get(l).getKey())){
                                                                finalTvSize.setBackground(getResources().getDrawable(R.drawable.spinner_shape));
                                                            }else {

                                                            }
                                                        }*//*



                                                        selectList.add(variant.get(finalI).getValue().get(finalJ));

                                                        Log.e(TAG, "On click selectList= "+selectList.size());

                                                        for (int k = 0;k<variationses.size();k++){
                                                            if (equalLists(selectList,variationses.get(k).getAttributes())){

                                                                Log.e(TAG, "On click final variable= "+variationses.get(k).getDisplay_regular_price());
                                                                Log.e(TAG, "On click final variable= "+variationses.get(k).getAvailability_html());


                                                                        *//* set price *//*

                                                                if (variationses.get(k).getDisplay_price().equals("")){
                                                                    tv_regularPrice.setVisibility(View.INVISIBLE);
                                                                    tv_offer.setVisibility(View.INVISIBLE);
                                                                    tv_salePrice.setText(variationses.get(k).getDisplay_regular_price());
                                                                }else {
                                                                    tv_offer.setVisibility(View.VISIBLE);
                                                                    tv_regularPrice.setVisibility(View.VISIBLE);

                                                                    tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                                                    tv_regularPrice.setText(variationses.get(k).getDisplay_regular_price());
                                                                    tv_salePrice.setText(variationses.get(k).getDisplay_price());
                                                                }

                                                                if (variationses.get(k).getIs_in_stock().equals("true")){
                                                                    tv_stock.setTextColor(getResources().getColor(R.color.color_green));
                                                                }else {
                                                                    tv_stock.setTextColor(getResources().getColor(R.color.color_red));
                                                                }

                                                                tv_stock.setText(variationses.get(k).getAvailability_html());


                                                            }

                                                        }



                                                    }
                                                });

                                                ll_variable.addView(view1);
                                            }*/


                                ll_attri.addView(view);
                            }
                        }

                    } else if (beanDetailProduct.getData().getProduct_detail().getProduct_type().equals(PRODUCTTYPE_EXTERNAL)) {
                        cardAtri.setVisibility(View.GONE);
                        btn_BuyNow.setVisibility(View.GONE);
                        cardQnty.setVisibility(View.GONE);
                        final RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                        btn_addToCart.setLayoutParams(lparams);
                        btn_addToCart.setText(beanDetailProduct.getData().getProduct_detail().getButton_text());


                        /* set price */
                        if (beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_regula().equals("")) {

                            if (beanDetailProduct.getData().getProduct_detail().getSale_price().equals("")) {
                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                tv_offer.setVisibility(View.INVISIBLE);
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getRegular_price());
                            } else {
                                tv_offer.setVisibility(View.VISIBLE);
                                tv_regularPrice.setVisibility(View.VISIBLE);


                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                tv_regularPrice.setText(beanDetailProduct.getData().getProduct_detail().getRegular_price());
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getSale_price());

                            }


                        } else {

                            if (beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_sale().equals("")) {
                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                tv_offer.setVisibility(View.INVISIBLE);
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_regula());
                            } else {
                                tv_offer.setVisibility(View.VISIBLE);
                                tv_regularPrice.setVisibility(View.VISIBLE);


                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                tv_regularPrice.setText(beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_regula());
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_sale());

                            }

                        }

                    } else if (beanDetailProduct.getData().getProduct_detail().getProduct_type().equals(PRODUCTTYPE_GROUPED)) {
                        ll_grouplist.removeAllViews();
                        cardQnty.setVisibility(View.GONE);
                        btn_BuyNow.setVisibility(View.GONE);
                        ll_footer_detail.setVisibility(View.GONE);
                        final RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        btn_addToCart.setLayoutParams(lparams);
                        //Log.e("ERROR","Product size------   "+beanDetailProduct.getData().getProduct_detail().getGrouped_products().getProduct().size());
                        final Grouped_products grouped_products = beanDetailProduct.getData().getProduct_detail().getGrouped_products();

                        /* set price */
                        if (beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_regula().equals("")) {

                            if (beanDetailProduct.getData().getProduct_detail().getSale_price().equals("")) {
                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                tv_offer.setVisibility(View.INVISIBLE);
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getRegular_price());
                            } else {
                                tv_offer.setVisibility(View.VISIBLE);
                                tv_regularPrice.setVisibility(View.VISIBLE);


                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                tv_regularPrice.setText(beanDetailProduct.getData().getProduct_detail().getRegular_price());
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getSale_price());

                            }

                        } else {

                            if (beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_sale().equals("")) {
                                tv_regularPrice.setVisibility(View.INVISIBLE);
                                tv_offer.setVisibility(View.INVISIBLE);
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_regula());
                            } else {
                                tv_offer.setVisibility(View.VISIBLE);
                                tv_regularPrice.setVisibility(View.VISIBLE);


                                tv_offer.setText(beanDetailProduct.getData().getProduct_detail().getOn_sale());
                                tv_regularPrice.setText(beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_regula());
                                tv_salePrice.setText(beanDetailProduct.getData().getProduct_detail().getTax_product_with_price_sale());

                            }

                        }

                        View view;
                        TextView productname, saleprice, regularprice, tv_is_in_stock, readmore = null;
                        EditText etQuentity;
                        ImageView ivMinus, ivPlus;

                        for (int i = 0; i < grouped_products.getProduct().size(); i++) {
                            final int finalI = i;
                            if (grouped_products.getProduct().get(i).getProduct_type().equals(PRODUCTTYPE_SIMPLE)) {
                                isSimple = true;
                                ll_footer_detail.setVisibility(View.VISIBLE);
                                view = inflater_attri.inflate(R.layout.inflater_grouplist, null);
                                productname = view.findViewById(R.id.tvGroupProduct_name);
                                saleprice = view.findViewById(R.id.tvGroupProduct_salrprice);
                                regularprice = view.findViewById(R.id.tvGroupProduct_regularprice);
                                tv_is_in_stock = view.findViewById(R.id.tvGroupStock);
                                etQuentity = view.findViewById(R.id.etQnty_group);
                                ivMinus = view.findViewById(R.id.ivDelete_group);
                                ivPlus = view.findViewById(R.id.ivAdd_group);

                                Log.e(TAG, "simple Product name== " + grouped_products.getProduct().get(i).getTitle());
                                productname.setText(grouped_products.getProduct().get(i).getTitle());
                                tv_is_in_stock.setText(grouped_products.getProduct().get(i).getAvailability_html());
                                //tv_is_in_stock.setText("out of stock");
                                try {
                                    if (grouped_products.getProduct().get(i).getSale_price().equals("")) {
                                        regularprice.setVisibility(View.INVISIBLE);
                                        saleprice.setText(grouped_products.getProduct().get(i).getRegular_price());

                                    } else {

                                        regularprice.setVisibility(View.VISIBLE);
                                        regularprice.setPaintFlags(regularprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        regularprice.setText(grouped_products.getProduct().get(i).getRegular_price());
                                        saleprice.setText(grouped_products.getProduct().get(i).getSale_price());

                                    }


                                    if (grouped_products.getProduct().get(i).getIs_in_stock().equals("false")) {
                                        tv_is_in_stock.setVisibility(View.VISIBLE);
                                        tv_is_in_stock.setTextColor(getResources().getColor(R.color.color_red));
                                        ivMinus.setClickable(false);
                                        ivPlus.setClickable(false);

                                    } else {
                                        tv_is_in_stock.setVisibility(View.VISIBLE);
                                        tv_is_in_stock.setTextColor(getResources().getColor(R.color.color_green));
                                        ivMinus.setClickable(true);
                                        ivPlus.setClickable(true);
                                    }


                                } catch (NullPointerException e) {

                                    dataWrite.showToast("" + e);
                                }


                                final EditText finalEtQuentity = etQuentity;
                                ivPlus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (grouped_products.getProduct().get(finalI).getIs_in_stock().equals("false")) {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                                            builder1.setMessage(R.string.sorry_this_product_not_availabler);
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();

                                        } else {

                                            int t = Integer.parseInt(finalEtQuentity.getText().toString());
                                            int count = Integer.parseInt(String.valueOf(t + 1));
                                            if (grouped_products.getProduct().get(finalI).getMax_qty() == null) {
                                                finalEtQuentity.setText("" + count);

                                            } else {
                                                int stock = 0;
                                                if (grouped_products.getProduct().get(finalI).getMax_qty().equals("")) {
                                                    stock = 0;
                                                } else {
                                                    stock = Integer.parseInt(grouped_products.getProduct().get(finalI).getMax_qty());
                                                }

                                                if (count > stock) {

                                                } else {
                                                    finalEtQuentity.setText("" + count);
                                                }
                                            }

                                            Cartproduct bean = new Cartproduct();
                                            bean.setImage(grouped_products.getProduct().get(finalI).getImage());
                                            bean.setMax_stock(grouped_products.getProduct().get(finalI).getMax_qty());
                                            bean.setOri_price(grouped_products.getProduct().get(finalI).getOrigional_price());
                                            bean.setProduct_id(grouped_products.getProduct().get(finalI).getProduct_id());
                                            bean.setProduct_name(grouped_products.getProduct().get(finalI).getTitle());
                                            bean.setQnty("" + finalEtQuentity.getText());
                                            bean.setSubTotal("" + Float.parseFloat(grouped_products.getProduct().get(finalI).getCart_sale_price()) * Integer.parseInt("" + finalEtQuentity.getText()));
                                            bean.setTax_status(grouped_products.getProduct().get(finalI).getTax_status());
                                            bean.setVariation_price(grouped_products.getProduct().get(finalI).getCart_sale_price());
                                            bean.setVariations(null);
                                            bean.setVariations_id(null);

                                /*        Log.e(TAG, "setImage== "+grouped_products.getProduct().get(finalI).getImage());
                                        Log.e(TAG, "setMax_stock== "+grouped_products.getProduct().get(finalI).getMax_qty());
                                        Log.e(TAG, "setOri_price== "+grouped_products.getProduct().get(finalI).getOrigional_price());
                                        Log.e(TAG, "setProduct_id== "+grouped_products.getProduct().get(finalI).getProduct_id());
                                        Log.e(TAG, "setProduct_name== "+grouped_products.getProduct().get(finalI).getTitle());
                                        Log.e(TAG, "setSubTotal== "+""+Float.parseFloat(grouped_products.getProduct().get(finalI).getCart_sale_price())*Integer.parseInt(""+finalEtQuentity.getText()));
                                        Log.e(TAG, "setTax_status== "+grouped_products.getProduct().get(finalI).getTax_status());
                                        Log.e(TAG, "setVariation_price== "+grouped_products.getProduct().get(finalI).getCart_sale_price());
                                        Log.e(TAG, "setVariations== ");
                                        Log.e(TAG, "setQnty== "+""+finalEtQuentity.getText());*/


                                            cartproducts.add(bean);
                                        }


                                    }
                                });


                                ivMinus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        int count1;
                                        int t1 = 0;
                                        t1 = Integer.parseInt(finalEtQuentity.getText().toString());
                                        count1 = t1 - 1;

                                        if (count1 <= 0) {
                                            count1 = 0;
                                            finalEtQuentity.setText("" + count1);

                                        } else {
                                            finalEtQuentity.setText("" + count1);
                                        }

                                    }
                                });

                            } else {
                                view = inflater_attri.inflate(R.layout.inflater_grouplist2, null);
                                readmore = view.findViewById(R.id.tvgroup_readmore);
                                productname = view.findViewById(R.id.tvGroupProduct_name);
                                saleprice = view.findViewById(R.id.tvGroupProduct_salrprice);
                                regularprice = view.findViewById(R.id.tvGroupProduct_regularprice);

                                Log.e(TAG, "group Product name== " + grouped_products.getProduct().get(i).getTitle());
                                productname.setText(grouped_products.getProduct().get(i).getTitle());


                                try {
                                    if (grouped_products.getProduct().get(i).getSale_price().equals("")) {
                                        regularprice.setVisibility(View.INVISIBLE);
                                        saleprice.setText(grouped_products.getProduct().get(i).getRegular_price());

                                    } else {

                                        regularprice.setVisibility(View.VISIBLE);
                                        regularprice.setPaintFlags(regularprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        regularprice.setText(grouped_products.getProduct().get(i).getRegular_price());
                                        saleprice.setText(grouped_products.getProduct().get(i).getSale_price());

                                    }

                                    if (grouped_products.getProduct().get(i).getProduct_type().equals(PRODUCTTYPE_EXTERNAL)) {
                                        readmore.setText(grouped_products.getProduct().get(i).getButton_text());

                                    } else if (grouped_products.getProduct().get(i).getProduct_type().equals(PRODUCTTYPE_GROUPED)) {
                                        readmore.setText(R.string.readmore);

                                    } else if (grouped_products.getProduct().get(i).getProduct_type().equals(PRODUCTTYPE_VARIABLE)) {
                                        readmore.setText(R.string.readmore);

                                    }
                                    readmore.setBackgroundColor(Color.parseColor(color_back));
                                    readmore.setTextColor(Color.WHITE);


                                    readmore.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AppDebugLog.println("getProduct_type()====" + grouped_products.getProduct().get(finalI).getProduct_type());


                                            if (grouped_products.getProduct().get(finalI).getProduct_type().equals(PRODUCTTYPE_EXTERNAL)) {

                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(grouped_products.getProduct().get(finalI).getProduct_url()));
                                                startActivity(browserIntent);


                                            } else if (grouped_products.getProduct().get(finalI).getProduct_type().equals(PRODUCTTYPE_GROUPED)) {

                                                AppConstant.PRODUCT_ID = grouped_products.getProduct().get(finalI).getProduct_id();
                                                AppConstant.productname = grouped_products.getProduct().get(finalI).getTitle();
                                                Intent intent = new Intent(activity, ProductDetailActivity.class);
                                                startActivity(intent);

                                            } else if (grouped_products.getProduct().get(finalI).getProduct_type().equals(PRODUCTTYPE_VARIABLE)) {

                                                AppConstant.PRODUCT_ID = grouped_products.getProduct().get(finalI).getProduct_id();
                                                AppConstant.productname = grouped_products.getProduct().get(finalI).getTitle();
                                                Intent intent = new Intent(activity, ProductDetailActivity.class);
                                                intent.putExtra("position", finalI);
                                                startActivity(intent);

                                            }
                                        }
                                    });
                                } catch (NullPointerException e) {

                                    dataWrite.showToast("" + e);
                                }

                            }


                            productname.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AppConstant.PRODUCT_ID = grouped_products.getProduct().get(finalI).getProduct_id();
                                    AppConstant.productname = grouped_products.getProduct().get(finalI).getTitle();
                                    Intent intent = new Intent(activity, ProductDetailActivity.class);
                                    intent.putExtra("position", finalI);
                                    startActivity(intent);
                                }
                            });
                            ll_grouplist.addView(view);
                        }

                    }

                    /* set rating*/
                    if (rate.trim().length() == 0) {

                    } else {

                        float finalrate = Float.parseFloat(rate);
                        ratingBar.setRating(finalrate);
                    }

                }


            }

        } catch (Exception e) {

        }

    }


    public boolean selectedAtri(ArrayList<TextView> textAttri) {

        for (int j = 0; j < textAttri.size(); j++) {

            TextView s;
            if (textAttri.get(j).length() == 0) {
                s = textAttri.get(j);
                //Log.e(TAG, "zero selectList= "+s);
                return false;
            }
        }

        return true;
    }

    public boolean equalLists(List<String> one, List<String> two) {
        if (one == null && two == null) {
            return true;
        }

        if ((one == null && two != null)
                || one != null && two == null
                || one.size() < two.size()) {
            return false;

        }

        //to avoid messing the order of the lists we will use a copy
        //as noted in comments by A. R. S.
        one = new ArrayList<String>(one);
        two = new ArrayList<String>(two);

        AppDebugLog.println("compare = " + one.size() + ",,,," + two.size());

        Collections.sort(one);
        Collections.sort(two);
        return (one.equals(two)) || (one.containsAll(two));
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // activity.registerReceiver(receive, new IntentFilter(Intent_Class_Data.NOTIFICATION));

        ll_attri.removeAllViews();
        ll_reviewList.removeAllViews();
        tv_stock.setVisibility(View.GONE);
        tv_attriWarn.setVisibility(View.GONE);
        textAttri.clear();
        selectList.clear();
        fetch_reviews(product_id);
        fetch_productDetail(product_id);
        productQuantity(product_id);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //activity.unregisterReceiver(receive);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setCounter(AppConstant.counter);
    }

    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("product detail responseCode : " + responseCode);

        switch (responseCode) {
            case Success:
                fetch_productDetail(product_id);
                break;

            case product:
                //dataWrite.Insert_ProductList(AppConstant.shopview,appData.getResponseproduct().get(0));
                //groupProductDisplay();
                break;

            case Reviews:
                AppDebugLog.println("reviews added : ");
                fetch_reviews(product_id);
                break;

            case saveReivews:
                AppDebugLog.println("reviews saved : ");
                addRatingbar.setRating(0);
                et_review.setText("");
                et_gstEmail.setText("");
                et_gstName.setText("");
                count = N;
                ll_attri.removeAllViews();
                if (isNetworkAvailable()) {
                    displayProductDetail("detailProduct");
                    allReivews_call("reviews");
                    productQuantity(product_id);
                } else {
                    showNoInternetPromptDlg();
                }
                break;

            case ServerError:
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
            case R.id.rl_cart:
                startActivity(new Intent(activity, CartActivity.class));
                break;

            case R.id.iv_right_search:
                SearchDialogOpen();
                break;

        }
    }

    private class VariationsColorAdapter extends BaseAdapter {

        ArrayList<String> colorlist;
        LayoutInflater layoutInflater;

        public VariationsColorAdapter(Activity activity, ArrayList<String> variations_colors) {
            this.colorlist = variations_colors;
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return colorlist.size();
        }

        @Override
        public Object getItem(int i) {
            return colorlist.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            VariationsColorAdapter.ViewHolder vh;

            if (view == null) {
                vh = new VariationsColorAdapter.ViewHolder();
                view = layoutInflater.inflate(R.layout.inflater_variation, null);
                vh.tv_variationName = view.findViewById(R.id.tvVariationName);

                //vh.tv_variationName.setTextColor(Color.parseColor(color_font));

                view.setTag(vh);
            } else {

                vh = (VariationsColorAdapter.ViewHolder) view.getTag();
            }

            vh.tv_variationName.setText(colorlist.get(i));
            return view;
        }

        public class ViewHolder {
            TextView tv_variationName;
        }
    }

    class MyPagesAdapter extends PagerAdapter implements YouTubeThumbnailView.OnInitializedListener {

        List<String> list = new ArrayList<String>();
        ImageView thumb_image;
        DynamicHeightImageView ivImage;
        String video_id;

        public MyPagesAdapter(List<String> urlList) {
            this.list = urlList;

        }

        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            return list.size();
        }

        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            Log.e("TAG", "url====== " + list.get(position));
            if (isVideoUrl(list.get(position))) {
                View page = inflater.inflate(R.layout.product_detail_video, null);

                try {

                    String uriPath = list.get(position);

                    video_id = extractYoutubeId(uriPath);
                    // String uriPath = ; //update package name
                    //String uriPath = "https://www.youtube.com/watch?v=H1a9k7Mnpew";
                    //String uriPath = "https://vimeo.com/249675407";
                    // String url = getUrlVideoRTSP(uriPath);
                    //Log.e("TAG","url====== "+url);
                    final Uri uri = Uri.parse(uriPath);
                    final VideoView videoView;
                    final ProgressBar pgvideo;
                    final MediaController mediacontroller;
                    final YouTubeThumbnailView videoTumbnail;

                    pgvideo = (ProgressBar) page.findViewById(R.id.pgVideo);
                    videoView = (VideoView) page.findViewById(R.id.productVideo);
                    videoTumbnail = (YouTubeThumbnailView) page.findViewById(R.id.imageView_thumbnail);

                    videoTumbnail.initialize(Config.YOUTUBE_API_KEY, this);
                    final YouTubeThumbnailLoader loader;

                    videoTumbnail.setImageBitmap(null);


                    videoTumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(activity)) {
                                //Opens in the StandAlonePlayer but in "Light box" mode
                                startActivity(YouTubeStandalonePlayer.createVideoIntent(activity,
                                        Config.YOUTUBE_API_KEY, video_id, 0, true, true));
                            }
                        }
                    });


                } catch (Exception e) {

                }

                (container).addView(page, 0);
                return page;


            } else {

                View page = inflater.inflate(R.layout.product_detail_image, null);
                thumb_image = page.findViewById(R.id.imgDisplay);
                try {

                    Glide.with(activity).load(list.get(position))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(thumb_image);

                } catch (Exception e) {

                }

                thumb_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fullimageDisplay(position);
                    }
                });

                (container).addView(page, 0);
                return page;
            }
        }


        @Override
        public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {

            loader.setVideo(video_id);

        }

        @Override
        public void onInitializationFailure(YouTubeThumbnailView ThumbnailView, YouTubeInitializationResult InitializationResult) {

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            (container).removeView((View) object);
            object = null;
        }

    }

    public boolean isVideoUrl(String url) {
        String id = null;
        try {
            String query = new URL(url).getQuery();
            if (query != null) {
                String[] param = query.split("&");
                for (String row : param) {
                    String[] param1 = row.split("=");
                    if (param1[0].equals("v")) {//"https://www.youtube.com/watch?v=H1a9k7Mnpew"
                        //id = param1[1];
                        return true;
                    }
                }
            }
            //else
             /*   {
                    if (url.contains("embed"))
                    {
                        id = url.substring(url.lastIndexOf("/") + 1);
                    }
                }*/
        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }

        return false;
    }

    protected String extractYoutubeId(String url) throws MalformedURLException {
        String id = null;
        try {
            String query = new URL(url).getQuery();
            if (query != null) {
                String[] param = query.split("&");
                for (String row : param) {
                    String[] param1 = row.split("=");
                    if (param1[0].equals("v")) {//"https://www.youtube.com/watch?v=H1a9k7Mnpew"
                        id = param1[1];
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }
        return id;
    }

    class MyFullImagePagesAdapter extends PagerAdapter implements YouTubeThumbnailView.OnInitializedListener {
        private ScaleGestureDetector scaleGestureDetector;
        private Matrix matrix = new Matrix();
        List<String> list = new ArrayList<String>();
        String video_id;

        public MyFullImagePagesAdapter(List<String> urlList) {
            this.list = urlList;

        }

        @Override
        public int getCount() {
            //Return total pages, here one for each data item
            return list.size();
        }

        //Create the given page (indicated by position)
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Log.e("TAG", "url====== " + list.get(position));
            if (isVideoUrl(list.get(position))) {
                View page = inflater.inflate(R.layout.product_detail_video, null);

                try {

                    String uriPath = list.get(position);

                    video_id = extractYoutubeId(uriPath);
                    // String uriPath = ; //update package name
                    //String uriPath = "https://www.youtube.com/watch?v=H1a9k7Mnpew";
                    //String uriPath = "https://vimeo.com/249675407";
                    // String url = getUrlVideoRTSP(uriPath);
                    //Log.e("TAG","url====== "+url);
                    final Uri uri = Uri.parse(uriPath);
                    final VideoView videoView;
                    final ProgressBar pgvideo;
                    final MediaController mediacontroller;
                    final YouTubeThumbnailView videoTumbnail;

                    pgvideo = (ProgressBar) page.findViewById(R.id.pgVideo);
                    videoView = (VideoView) page.findViewById(R.id.productVideo);
                    videoTumbnail = (YouTubeThumbnailView) page.findViewById(R.id.imageView_thumbnail);

                    videoTumbnail.initialize(Config.YOUTUBE_API_KEY, this);
                    final YouTubeThumbnailLoader loader;

                    videoTumbnail.setImageBitmap(null);


                    videoTumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (YouTubeIntents.canResolvePlayVideoIntentWithOptions(activity)) {
                                //Opens in the StandAlonePlayer but in "Light box" mode
                                startActivity(YouTubeStandalonePlayer.createVideoIntent(activity,
                                        Config.YOUTUBE_API_KEY, video_id, 0, true, true));
                            }
                        }
                    });


                } catch (Exception e) {

                }

                (container).addView(page, 0);
                return page;
            } else {
                View page = inflater.inflate(R.layout.product_zoom_image, null);


                PhotoView imgDisplay =  page.findViewById(R.id.imgDisplay);
                         try {

                    Glide.with(activity).load(list.get(position))
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgDisplay);

                } catch (Exception e) {

                }


                (container).addView(page, 0);

                return page;
            }

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //See if object from instantiateItem is related to the given view
            //required by API
            return arg0 == (View) arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            (container).removeView((View) object);
            object = null;
        }

        @Override
        public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {

            loader.setVideo(video_id);

        }

        @Override
        public void onInitializationFailure(YouTubeThumbnailView ThumbnailView, YouTubeInitializationResult InitializationResult) {

        }
    }


    public static void setCounter(String counter) {
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

        LinearLayout ll_back = search_dialog.findViewById(R.id.ll_back);
        final EditText et_search = search_dialog.findViewById(R.id.et_search);
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

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
                        sale_price = view.findViewById(R.id.tvPrice_product);
                        regular_price = view.findViewById(R.id.tvRegularPrice);
                        description = view.findViewById(R.id.tvProductdescription);
                        offers = view.findViewById(R.id.tvOffers_product);
                        productImage = view.findViewById(R.id.ivProductImage);
                        progressBar = (ProgressBar) view.findViewById(R.id.pbarProduct);
                        ratingbar = view.findViewById(R.id.ratingbar);
                        rlProductmain = (RelativeLayout) view.findViewById(R.id.rlProduct);

                        icWish = view.findViewById(R.id.icWish_product);

                        break;
                    case LIST_TYPE_1:

                        sale_price = view.findViewById(R.id.tvPrice_product);
                        regular_price = view.findViewById(R.id.tvRegularPrice);
                        description = view.findViewById(R.id.tvProductdescription);
                        offers = view.findViewById(R.id.tvOffers_product);
                        productImage = view.findViewById(R.id.ivProductImage);
                        progressBar = (ProgressBar) view.findViewById(R.id.pbarProduct);
                        ratingbar = view.findViewById(R.id.ratingbar);
                        rlProductmain = (RelativeLayout) view.findViewById(R.id.rlProduct);

                        icWish = view.findViewById(R.id.icWish_product);

                        break;

                    case LIST_TYPE_2:

                        sale_price = view.findViewById(R.id.tvPrice_product);
                        regular_price = view.findViewById(R.id.tvRegularPrice);
                        description = view.findViewById(R.id.tvProductdescription);
                        offers = view.findViewById(R.id.tvOffers_product);
                        productImage = view.findViewById(R.id.ivProductImage);
                        progressBar = (ProgressBar) view.findViewById(R.id.pbarProduct);
                        ratingbar = view.findViewById(R.id.ratingbar);
                        rlProductmain = (RelativeLayout) view.findViewById(R.id.rlProduct);
                        icWish = view.findViewById(R.id.icWish_product);

                        //rlProductmain.getLayoutParams().height = rlProductmain.getHeight()-30;

                        break;
                    case LIST_TYPE_3:
                        sale_price = view.findViewById(R.id.tvPrice_product);
                        regular_price = view.findViewById(R.id.tvRegularPrice);
                        description = view.findViewById(R.id.tvProductdescription);
                        offers = view.findViewById(R.id.tvOffers_product);
                        productImage = view.findViewById(R.id.ivProductImage);
                        progressBar = (ProgressBar) view.findViewById(R.id.pbarProduct);
                        ratingbar = view.findViewById(R.id.ratingbar);
                        rlProductmain = (RelativeLayout) view.findViewById(R.id.rlProduct);

                        icWish = view.findViewById(R.id.icWish_product);

                        break;

                    case SMALLVIEW:

                        sale_price = view.findViewById(R.id.tvPrice_product);
                        regular_price = view.findViewById(R.id.tvRegularPrice);
                        description = view.findViewById(R.id.tvProductdescription);
                        offers = view.findViewById(R.id.tvOffers_product);
                        productImage = view.findViewById(R.id.ivProductImage);
                        progressBar = (ProgressBar) view.findViewById(R.id.pbarProduct);
                        ratingbar = view.findViewById(R.id.ratingbar);
                        rlProductmain = (RelativeLayout) view.findViewById(R.id.rlProduct);
                        icWish = view.findViewById(R.id.icWish_product);

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
                        if (parentClassName.equalsIgnoreCase("AdapterHomeProduct")) {
                            intent.putExtra("beanProductDetailList", beanHomeProducts1);
                        } else if (parentClassName.equalsIgnoreCase("AdapterSearch")) {
                        } else if (parentClassName.equalsIgnoreCase("BeautyFragment")) {
                            intent.putExtra("beanProductDetailList", beautyProductList1);
                        } else if (parentClassName.equalsIgnoreCase("OffersFragment")) {
                            intent.putExtra("beanProductDetailList", offerProductList1);
                        } else if (parentClassName.equalsIgnoreCase("ProductActivity")) {
                            intent.putExtra("beanProductDetailList", beanProductActivityList1);
                        } else if (parentClassName.equalsIgnoreCase("ProductFragment")) {
                            intent.putExtra("beanProductDetailList", beanProductFragmentList1);
                        } else if (parentClassName.equalsIgnoreCase("ProductDetailActivity")) {
                            intent.putExtra("beanProductDetailList", beanProductDetailList1);
                        }

                        intent.putExtra("parentClassName", "ProductDetailActivity");
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
                Log.e("ProductDetailActivity", ":Exception++++++++++++++++++8");
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return bean_list.size();
        }
    }

    private void setSimilarProduct() {
        try {
            if (parentClassName.equalsIgnoreCase("AdapterHomeProduct")) {
                beanHomeProducts.remove(position);
                albumsAdapter = new ProdutAdapter(beanHomeProducts);
                recyclerViewSimilarProduct.setAdapter(albumsAdapter);
                albumsAdapter.notifyDataSetChanged();
            } else if (parentClassName.equalsIgnoreCase("AdapterSearch")) {

            } else if (parentClassName.equalsIgnoreCase("BeautyFragment")) {
                beautyProductList.remove(position);
                albumsAdapter = new ProdutAdapter(beautyProductList);
                recyclerViewSimilarProduct.setAdapter(albumsAdapter);
                albumsAdapter.notifyDataSetChanged();
            } else if (parentClassName.equalsIgnoreCase("OffersFragment")) {
                offerProductList.remove(position);
                albumsAdapter = new ProdutAdapter(offerProductList);
                recyclerViewSimilarProduct.setAdapter(albumsAdapter);
                albumsAdapter.notifyDataSetChanged();
            } else if (parentClassName.equalsIgnoreCase("ProductActivity")) {
                beanProductActivityList.remove(position);
                albumsAdapter = new ProdutAdapter(beanProductActivityList);
                recyclerViewSimilarProduct.setAdapter(albumsAdapter);
                albumsAdapter.notifyDataSetChanged();
            } else if (parentClassName.equalsIgnoreCase("ProductFragment")) {
                beanProductFragmentList.remove(position);
                albumsAdapter = new ProdutAdapter(beanProductFragmentList);
                recyclerViewSimilarProduct.setAdapter(albumsAdapter);
                albumsAdapter.notifyDataSetChanged();
            } else if (parentClassName.equalsIgnoreCase("ProductDetailActivity")) {
                beanProductDetailList.remove(position);
                albumsAdapter = new ProdutAdapter(beanProductDetailList);
                recyclerViewSimilarProduct.setAdapter(albumsAdapter);
                albumsAdapter.notifyDataSetChanged();
            } else {

            }
        } catch (Exception e) {
            Log.e("ProductDetailActivity", ":Exception++++++++++++++++++9");
            e.printStackTrace();
        }


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
                        initialize2();
                    }
                }).show();
    }

    private void initialize2() {
        if (isNetworkAvailable()) {
            displayProductDetail("detailProduct");
            allReivews_call("reviews");
        } else {
            showNoInternetPromptDlg();
        }
    }

}
