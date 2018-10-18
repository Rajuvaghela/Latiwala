package com.lujayn.latiwala.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shailesh on 12/15/2015.
 */
public class HelperData extends SQLiteOpenHelper {
    public static final String Database_name = "database";
    public static final String Table_userDetail = "table_userDetail";
    public static final String Table_addtocart = "table_name_addtocart";
    public static final String Table_categorys = "table_categorys";
    public static final String Table_product_detail = "product_detail";
    public static final String Table_product_reviews = "product_reviews";
    public static final String Table_product_list = "product_list";
    public static final String Table_sub_category = "table_sub_category";
    public static final String Table_country = "table_country";
    public static final String Table_states = "table_states";
    public static final String Table_order_detail = "table_order_detail";
    public static final String Table_orders = "table_orders";
    public static final String Table_method = "table_method";
    public static final String Table_ship_method = "table_ship_method";
    public static final String Table_wishlist = "table_wishlist";



    /* Wishlist */
    public static final String KEY_WISHLIST_ID = "wishlist_id";
    public static final String KEY_VAR_WISH_ID = "var_id";
    public static final String KEY_ISWISHLIST= "isWishlist";

    /*cart items*/
    public static final String KEY_ID = "_ID";
    public static final String KEY_PRODUCT_NAME = "name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_QNTY = "qnty";
    public static final String KEY_IMAGE= "img";
    public static final String KEY_PRODUCT_ID="product_id";
    public static final String KEY_SUBTOTAL = "subtotal";
    public static final String KEY_VARIATION_ID= "variation_id";
    public static final String KEY_VARIATIONS= "variations";
    public static final String KEY_MAX_STOCK = "max_stock";
    public static final String KEY_ORI_PRICE = "ori_price";
    public static final String KEY_TAX_STATUS = "tax_status";
    //public static final String KEY_VARIATION_COLOR= "variation_color";


/*USER DETAIL*/
    public static final String KEY_USER_FNAME = "user_fname";
    public static final String KEY_USER_LNAME = "user_lname";
    public static final String KEY_USER_ID= "user_id";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_WISHLIST_COUNT = "wishlist_count";


    /*BILLING DETAIL*/
    public static final String KEY_BILL_ADDRESS = "bill_address";
    public static final String KEY_BILL_CITY = "bill_city";
    public static final String KEY_BILL_STATE = "bill_state";
    public static final String KEY_BILL_COMPANY = "bill_company";
    public static final String KEY_BILL_STATE_CODE="bill_state_code";
    public static final String KEY_BILL_COUNTRY = "bill_country";
    public static final String KEY_BILL_COUNTRY_CODE = "bill_country_code";
    public static final String KEY_BILL_EMAIL = "bill_email";
    public static final String KEY_BILL_FNAME = "bill_fname";
    public static final String KEY_BILL_LNAME = "bill_lname";
    public static final String KEY_BILL_PHONE = "bill_phone";
    public static final String KEY_BILL_POSTCOE = "bill_postcode";

    /*SHIPPING DETAIL*/
    public static final String KEY_SHIP_ADDRESS = "ship_address";
    public static final String KEY_SHIP_CITY = "ship_city";
    public static final String KEY_SHIP_STATE = "ship_state";
    public static final String KEY_SHIP_COMPANY = "ship_company";
    public static final String KEY_SHIP_STATE_CODE="ship_state_code";
    public static final String KEY_SHIP_COUNTRY = "ship_country";
    public static final String KEY_SHIP_COUNTRY_CODE="ship_country_code";
    public static final String KEY_SHIP_FNAME = "ship_fname";
    public static final String KEY_SHIP_LNAME = "ship_lname";
    public static final String KEY_SHIP_POSTCODE = "ship_postcode";


    /*Category*/
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_CATEGORY_NAME = "category_name";
    public static final String KEY_CATEGORY_COUNT = "count";
    public static final String KEY_CATEGORY_CHILD = "child";
    public static final String KEY_CATEGORY_DESCRIPTION = "description";
    public static final String KEY_CATEGORY_SLUG = "slug";
    public static final String KEY_CATEGORY_IMAGE = "image";


    /* Sub Category */
    public static final String KEY_SUBCATEGORY_ID = "subcategory_id";
    public static final String KEY_SUBCATEGORY_NAME = "subcategory_name";

    public static final String KEY_SUBCATEGORY_COUNT = "count";
    public static final String KEY_SUBCATEGORY_PARENT = "parent";
    public static final String KEY_SUBCATEGORY_DES = "description";
    public static final String KEY_SUBCATEGORY_SLUG = "slug";
    public static final String KEY_SUBCATEGORY_IMAGE = "image";
    public static final String KEY_SUBCATEGORY_CHILD = "child";

    /* Products */
    public static final String KEY_PRODUCT_LIST = "productList";
    public static final String KEY_CATEGORYSLUG = "category_slug";
    public static final String KEY_CURRENCY_SYMBOL = "currency_symbol";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_STATUS = "status";
    public static final String KEY_RATING = "rating";
    public static final String KEY_PARENT_CATEGORY_ID = "parent_category_id";
    public static final String KEY_SUFFIX = "suffix";
    public static final String KEY_ON_SALE = "on_sale";
    public static final String KEY_SALE_PRICE = "sale_price";
    public static final String KEY_REGULAR_PRICE = "regular_price";

    /* Country */
    public static final String KEY_COUNTRY_CODE = "country_code";
    public static final String KEY_COUNTRY_NAME="country_name";

    /* States*/
    public static final String KEY_STATE_CODE = "state_code";
    public static final String KEY_STATE_NAME="state_name";


    /*product detail*/
    public static final String KEY_PRODUCT_DETAIL = "product_detail";

    /*product reivews*/
    public static final String KEY_PRODUCT_REVIEWS = "product_reviews";

    /*order detail*/
    public static final String KEY_ORDER_DETAIL = "order_detail";
    public static final String KEY_ORDER_ID = "order_id";

    /* orders*/

    public static final String KEY_ORDERS = "orders";

    /* method*/
    public static final String KEY_METHOD = "_method";
    public static final String KEY_METHOD_ID = "method_id";
    public static final String KEY_METHODid = "id";
    public static final String KEY_METHOD_TITLE = "title";
    public static final String KEY_METHOD_TAX_STATUS = "tax_status";
    public static final String KEY_METHOD_COST = "cost";

    public static final int version =29;


    public HelperData(Context context) {
        super(context, Database_name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Table_create = "Create table " + Table_userDetail + "(" + KEY_ID
                + " Integer primary key autoincrement,"+KEY_USER_FNAME+" TEXT," +KEY_USER_LNAME+
                " TEXT,"+ KEY_USER_ID +" TEXT," + KEY_USER_EMAIL + " TEXT," + KEY_BILL_ADDRESS +
                " TEXT," + KEY_BILL_CITY + " TEXT," + KEY_BILL_STATE + " TEXT,"+KEY_BILL_STATE_CODE+
                " TEXT," + KEY_BILL_COUNTRY + " TEXT," + KEY_BILL_COUNTRY_CODE +" TEXT," +
                KEY_BILL_EMAIL + " TEXT," + KEY_BILL_FNAME + " TEXT," + KEY_BILL_LNAME + " TEXT,"+
                KEY_BILL_COMPANY+" TEXT," + KEY_BILL_PHONE + " TEXT," + KEY_BILL_POSTCOE + " TEXT,"
                + KEY_SHIP_FNAME + " TEXT,"+ KEY_SHIP_LNAME + " TEXT," + KEY_SHIP_ADDRESS + " TEXT,"
                + KEY_SHIP_CITY + " TEXT," + KEY_SHIP_STATE + " TEXT,"+KEY_SHIP_STATE_CODE+" TEXT,"
                + KEY_SHIP_COUNTRY +" TEXT," + KEY_SHIP_COUNTRY_CODE + " TEXT," +KEY_SHIP_COMPANY+
                " TEXT," + KEY_SHIP_POSTCODE + " TEXT);";
        db.execSQL(Table_create);

        String Table_addToCart = "Create table " + Table_addtocart + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_PRODUCT_ID +" TEXT," + KEY_PRODUCT_NAME +
                " TEXT," + KEY_VARIATION_ID +" TEXT," + KEY_VARIATIONS + " TEXT," + KEY_PRICE +
                " TEXT," + KEY_ORI_PRICE + " TEXT," + KEY_QNTY + " TEXT,"+KEY_IMAGE + " TEXT," +
                KEY_SUBTOTAL +" TEXT," + KEY_MAX_STOCK + " TEXT," + KEY_TAX_STATUS + " TEXT);";
        db.execSQL(Table_addToCart);


        String Table_Category = "Create table " + Table_categorys + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_CATEGORY_ID +" TEXT," + KEY_CATEGORY_NAME
                + " TEXT," + KEY_CATEGORY_COUNT +" TEXT," + KEY_CATEGORY_CHILD + " TEXT," +
                KEY_CATEGORY_DESCRIPTION + " TEXT," + KEY_CATEGORY_SLUG + " TEXT," +
                KEY_CATEGORY_IMAGE + " TEXT,"+KEY_IMAGE + " TEXT);";
        db.execSQL(Table_Category);


        String Table_ProdcutDetail = "Create table " + Table_product_detail + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_PRODUCT_ID +" TEXT," + KEY_PRODUCT_DETAIL
                + " TEXT);";
        db.execSQL(Table_ProdcutDetail);

        String Table_ProdcutReviews = "Create table " + Table_product_reviews + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_PRODUCT_ID +" TEXT," + KEY_PRODUCT_REVIEWS
                + " TEXT);";
        db.execSQL(Table_ProdcutReviews);

        String Table_ProdcutLIST = "Create table " + Table_product_list + "(" + KEY_ID +
                " Integer primary key autoincrement," +KEY_CATEGORYSLUG + " TEXT," + KEY_PRODUCT_LIST
                + " TEXT);";
        db.execSQL(Table_ProdcutLIST);

        String Table_SubCategory = "Create table " + Table_sub_category + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_SUBCATEGORY_ID +" TEXT," + KEY_SUBCATEGORY_NAME
                + " TEXT," + KEY_SUBCATEGORY_COUNT +" TEXT," + KEY_SUBCATEGORY_PARENT + " TEXT," +
                KEY_SUBCATEGORY_DES + " TEXT," + KEY_SUBCATEGORY_SLUG + " TEXT," +
                KEY_SUBCATEGORY_IMAGE +" TEXT," + KEY_SUBCATEGORY_CHILD+ " TEXT);";
        db.execSQL(Table_SubCategory);

        String Table_Country = "Create table " + Table_country + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_COUNTRY_CODE +" TEXT," + KEY_COUNTRY_NAME
                + " TEXT);";
        db.execSQL(Table_Country);

        String Table_State = "Create table " + Table_states + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_COUNTRY_CODE +" TEXT," + KEY_STATE_CODE
                + " TEXT," + KEY_STATE_NAME + " TEXT);";
        db.execSQL(Table_State);

        String Table_OrderDetail = "Create table " + Table_order_detail + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_ORDER_ID +" TEXT," + KEY_ORDER_DETAIL + " TEXT);";
        db.execSQL(Table_OrderDetail);

        String Table_Orders = "Create table " + Table_orders + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_USER_ID +" TEXT," + KEY_ORDERS + " TEXT);";
        db.execSQL(Table_Orders);

        String Table_Methods = "Create table " + Table_method + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_METHOD + " TEXT);";
        db.execSQL(Table_Methods);


        String Table_ShipMethod = "Create table " + Table_ship_method + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_METHOD_ID +" TEXT," + KEY_METHODid
                + " TEXT," + KEY_METHOD_COST+" TEXT," + KEY_METHOD_TITLE + " TEXT," +
                KEY_METHOD_TAX_STATUS + " TEXT);";
        db.execSQL(Table_ShipMethod);

        String Table_wish= "Create table " + Table_wishlist + "(" + KEY_ID +
                " Integer primary key autoincrement,"+ KEY_PRODUCT_ID+" TEXT," + KEY_VARIATION_ID
                + " TEXT," +KEY_PRODUCT_NAME+" TEXT,"+KEY_PRICE+" TEXT,"+ KEY_DESCRIPTION+" TEXT,"+ KEY_IMAGE+" TEXT,"+KEY_ISWISHLIST+ " TEXT);";
        db.execSQL(Table_wish);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "Drop table if exists " + Table_userDetail;
        String drop_addtocart = "Drop table if exists " + Table_addtocart;
        String drop_detail ="Drop table if exists " + Table_product_detail;
        String drop_category ="Drop table if exists " + Table_categorys;
        String drop_reviews ="Drop table if exists " + Table_product_reviews;
        String drop_product_list="Drop table if exists " + Table_product_list;
        String drop_subCategory="Drop table if exists " + Table_sub_category;
        String drop_country="Drop table if exists " + Table_country;
        String drop_state="Drop table if exists " + Table_states;
        String drop_orderDetail="Drop table if exists " + Table_order_detail;
        String drop_orders="Drop table if exists " + Table_orders;
        String drop_method="Drop table if exists " + Table_method;
        String drop_shipMethod="Drop table if exists " + Table_ship_method;
        String drop_wish="Drop table if exists " + Table_wishlist;


        db.execSQL(drop);
        db.execSQL(drop_addtocart);
        db.execSQL(drop_detail);
        db.execSQL(drop_category);
        db.execSQL(drop_reviews);
        db.execSQL(drop_product_list);
        db.execSQL(drop_subCategory);
        db.execSQL(drop_country);
        db.execSQL(drop_state);
        db.execSQL(drop_orderDetail);
        db.execSQL(drop_orders);
        db.execSQL(drop_method);
        db.execSQL(drop_shipMethod);
        db.execSQL(drop_wish);

        onCreate(db);

    }
}
