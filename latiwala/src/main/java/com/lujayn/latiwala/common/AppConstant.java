package com.lujayn.latiwala.common;


import com.lujayn.latiwala.bean.Items;
import com.lujayn.latiwala.bean.Payment_gateways;

import java.util.ArrayList;

/**
 * Created by Shailesh on 06/05/16.
 */
public abstract class AppConstant {
    public static String hashKey = "hashkey";
    public static String checkSumKey = "checksum";
    public static final boolean PRODUCTION_DEBUG = false;
    public static final boolean FILE_DEBUG = true;
    public static final String APP_TAG = "VooTouch";
    public static final String FONT = "fonts/CourierPrime.ttf";
    public static final String FONT_ARIALNERROW = "fonts/ArialNarrow.ttf";
    public static final String USER_DATA = "user_data";
    public static final String ORDER_ID = "order_id";
    public static final String STATUS = "status";
    public static final String FInstall = "finstall";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "userId";
    public static final String USER_EMAIL = "user_email";
    public static final String user_pass = "password";
    public static final String BILL_AS_SHIP = "bill_as_ship";
    public static final String SETTING_OPTION = "setting_option";
    public static final String TRIAL_URL = "trial_url";
    //public static final String BASE_URL = "";
    public static final String NULL_STRING = "";
    public static final String LAST_FRAG = "last_frag";
    public static String counter = "0";
    public static String wish_counter = "0";

    public static String COUNTRY_CODE = "countrycode";
    public static String SEARCH_LIST = "searchlist";
    public static String CATEGORYSLUG = "categoryslug";
    public static String ApplyCoupon = "coupon";
    public static String key = "key";

    public static String catID = "catID";
    public static String catName = "catname";
    public static String catslug = "catslug";
    public static String _Activty = "activity";
    public static String sub_catID = "subcatid";
    public static String sub_catName = "sub_catname";
    public static String PRODUCT_ID = "productid";
    public static String productname = "productname";
    public static String page_view = "";
    public static String shopview = "shopview";


    public static final String kCurrentVersion = "dbversion";
    public static final String dBVersion = "dbVersion";
    public static final int defaultDBVersion = -1;
    public static String PREFRENCE_EXTRA_INFO = "extrainfo";

    // SplashSCreen TimeOut
    public static final long SPLASH_SCREEN_TIMEOUT = 100;

    public static ArrayList<Items> Itemslist;
    public static Payment_gateways payment_gateways;


    public enum HttpRequestType {

        DataUpdateRequest(1);

        private final int value;

        private HttpRequestType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public enum HTTPResponseCode {
        ServerError(0), Success(1), NetworkError(2), CategoryUpdate(3), Subcategory(4),Reviews(5),
        saveReivews(6),Country(7),State(8),product(9),SaveBill(10),SaveShip(11),Save(12),Method(13),
        saveOrder(14),Applycode(15), CancelCode(16),Success0(17),NotiSuccess(18),socialSuccess(19), social0(20),
        hash_success(21),NOproduct(22),checksum_success(23),paytmVerify(24),paytmResponce(25);

        private final int value;
        private HTTPResponseCode(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }




}
