package com.lujayn.latiwala.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.lujayn.latiwala.R;
import com.lujayn.latiwala.activity.MainActivity;
import com.lujayn.latiwala.activity.ProductDetailActivity;
import com.lujayn.latiwala.bean.BeanCart;
import com.lujayn.latiwala.bean.BeanCategory;
import com.lujayn.latiwala.bean.BeanCountry;
import com.lujayn.latiwala.bean.BeanState;
import com.lujayn.latiwala.bean.BeanSubCategory;
import com.lujayn.latiwala.bean.BeanUserData;
import com.lujayn.latiwala.bean.BeanWishlist;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationContext;
import com.lujayn.latiwala.custom.AppDebugLog;

import java.util.ArrayList;

/**
 * Created by Shailesh on 02/08/16.
 */
public class DataWrite {

    HelperData helperdata;
    SQLiteDatabase sqldatabase;
    ArrayList<BeanUserData> userDataArrayList;
    ArrayList<BeanUserData> userShippinglist;
    ArrayList<BeanUserData> userBillinglist;
    ArrayList<BeanCart> cartlist;
    ArrayList<BeanCategory> categorieslist;
    ArrayList<BeanSubCategory> subcategoriesList;
    ArrayList<BeanCountry> countrylist;
    ArrayList<BeanState> statelist;
    ArrayList<BeanWishlist> wishlist;

    ArrayList<Double> arrayist_total;
    String list_detail = "";
    String reviews = "";
    String productlist = "";
    String orderDetail = "";
    String ordersList = "";
    String method = "";
    Toast m_currentToast;

    public DataWrite(Context context) {
        helperdata = new HelperData(context);
    }

    public void open() {
        sqldatabase = helperdata.getWritableDatabase();
    }

    public void showToast(String text) {
        if (m_currentToast != null) {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(ApplicationContext.getAppContext(), text, Toast.LENGTH_SHORT);
        m_currentToast.show();

    }

    public void Insert_Country(String country_code, String country_name) {
        String where = HelperData.KEY_COUNTRY_CODE + " = " + "'" + country_code + "'";
        String query = "SELECT * FROM " + HelperData.Table_country + " WHERE " + where;

        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();
        cv.put(HelperData.KEY_COUNTRY_CODE, country_code);
        cv.put(HelperData.KEY_COUNTRY_NAME, country_name);
        if (c.getCount() > 0) {
            c.moveToFirst();
            long data = sqldatabase.update(HelperData.Table_country, cv, where, null);
            //Toast.makeText(ApplicationContext.getAppContext(),"Updated Cart",Toast.LENGTH_SHORT).show();
            //showToast("Updated Country");
        } else {
            long data = sqldatabase.insert(HelperData.Table_country, null, cv);
            //Toast.makeText(ApplicationContext.getAppContext(),"Item Added",Toast.LENGTH_SHORT).show();
            // showToast("Country Added");
        }

    }

    public void Insert_States(String countryCode, String state_code, String state_name) {
        // Log.d("shailesh", "countryCode= "+ countryCode+" ,state_code== "+ state_code+" ,state_name== "+state_name);
        String where = HelperData.KEY_STATE_CODE + " = " + "'" + state_code + "'";
        String query = "SELECT * FROM " + HelperData.Table_states + " WHERE " + where;

        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();
        cv.put(HelperData.KEY_COUNTRY_CODE, countryCode);
        cv.put(HelperData.KEY_STATE_CODE, state_code);
        cv.put(HelperData.KEY_STATE_NAME, state_name);

        if (c.getCount() > 0) {
            c.moveToFirst();

            long data = sqldatabase.update(HelperData.Table_states, cv, where, null);
            //Toast.makeText(ApplicationContext.getAppContext(),"Updated Cart",Toast.LENGTH_SHORT).show();
            // showToast("Updated State");

        } else {
            long data = sqldatabase.insert(HelperData.Table_states, null, cv);
            //Toast.makeText(ApplicationContext.getAppContext(),"Item Added",Toast.LENGTH_SHORT).show();
            // showToast("State Added");
        }
    }

    public void Insert_OrderDetail(String ordID, String response) {

        AppDebugLog.println("ordID= " + ordID);
        String where = HelperData.KEY_ORDER_ID + " = " + ordID;
        String query = "SELECT * FROM " + HelperData.Table_order_detail + " WHERE " + where;

        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();
        cv.put(HelperData.KEY_ORDER_ID, ordID);
        cv.put(HelperData.KEY_ORDER_DETAIL, response);


        if (c.getCount() > 0) {
            c.moveToFirst();

            long data = sqldatabase.update(HelperData.Table_order_detail, cv, where, null);
            //Toast.makeText(ApplicationContext.getAppContext(),"Updated Cart",Toast.LENGTH_SHORT).show();
            // showToast("Updated order detail");

        } else {
            long data = sqldatabase.insert(HelperData.Table_order_detail, null, cv);
            //Toast.makeText(ApplicationContext.getAppContext(),"Item Added",Toast.LENGTH_SHORT).show();
            //  showToast("State order detail");
        }


    }


    public void Insert_Orders(String user_ID, String response) {

        String where = HelperData.KEY_USER_ID + " = " + user_ID;
        String query = "SELECT * FROM " + HelperData.Table_orders + " WHERE " + where;

        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();
        cv.put(HelperData.KEY_USER_ID, user_ID);
        cv.put(HelperData.KEY_ORDERS, response);

        if (c.getCount() > 0) {
            c.moveToFirst();

            long data = sqldatabase.update(HelperData.Table_orders, cv, where, null);
            //Toast.makeText(ApplicationContext.getAppContext(),"Updated Cart",Toast.LENGTH_SHORT).show();
            // showToast("Updated orders");

        } else {
            long data = sqldatabase.insert(HelperData.Table_orders, null, cv);
            //Toast.makeText(ApplicationContext.getAppContext(),"Item Added",Toast.LENGTH_SHORT).show();
            // showToast("orders detail");
        }


    }


    public void Insert_ShipMethod(String method_id, String id, String title, String tax_status, String cost) {

        String where = HelperData.KEY_METHOD_ID + " = " + "'" + method_id + "'";
        String query = "SELECT * FROM " + HelperData.Table_ship_method + " WHERE " + where;

        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();
        cv.put(HelperData.KEY_METHOD_ID, method_id);
        cv.put(HelperData.KEY_METHODid, id);
        cv.put(HelperData.KEY_METHOD_TITLE, title);
        cv.put(HelperData.KEY_METHOD_TAX_STATUS, tax_status);
        cv.put(HelperData.KEY_METHOD_COST, cost);

        if (c.getCount() > 0) {
            c.moveToFirst();

            long data = sqldatabase.update(HelperData.Table_ship_method, cv, where, null);
            //Toast.makeText(ApplicationContext.getAppContext(),"Updated Cart",Toast.LENGTH_SHORT).show();
            //showToast("Updated ship method");

        } else {
            long data = sqldatabase.insert(HelperData.Table_ship_method, null, cv);
            //Toast.makeText(ApplicationContext.getAppContext(),"Item Added",Toast.LENGTH_SHORT).show();
            //showToast("ship method detail");
        }
    }

    public void Insert_Method(String response) {

        String query = "SELECT * FROM " + HelperData.Table_orders;

        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();

        cv.put(HelperData.KEY_METHOD, response);

        long data = sqldatabase.insert(HelperData.Table_method, null, cv);
        //Toast.makeText(ApplicationContext.getAppContext(),"Item Added",Toast.LENGTH_SHORT).show();
        // showToast("method detail");

    }

    public void Insert_wish(String product_id, String product_name, String description, String price, String image) {
        String where = HelperData.KEY_PRODUCT_ID + " = " + product_id;
        String query = "SELECT * FROM " + HelperData.Table_wishlist + " WHERE " + where;

        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();

        cv.put(HelperData.KEY_PRODUCT_ID, product_id);
        cv.put(HelperData.KEY_PRODUCT_NAME, product_name);
        cv.put(HelperData.KEY_DESCRIPTION, description);
        cv.put(HelperData.KEY_PRICE, price);
        cv.put(HelperData.KEY_IMAGE, image);

        if (c.getCount() > 0) {
            c.moveToFirst();

            AppDebugLog.println("updated wishlit" + cv);

            long data = sqldatabase.update(HelperData.Table_wishlist, cv, where, null);
            showToast(ApplicationContext.getAppContext().getString(R.string.update_wishlist));
        } else {

            long data = sqldatabase.insert(HelperData.Table_wishlist, null, cv);
            showToast(ApplicationContext.getAppContext().getString(R.string.add_to_wishlist));
            int count = Integer.parseInt(AppConstant.wish_counter) + 1;
            AppConstant.wish_counter = "" + count;
            MainActivity.setWishCounter(AppConstant.wish_counter);
        }

    }

    public void deleteWish(String product_id) {
        ContentValues cv = new ContentValues();
        cv.put(HelperData.KEY_PRODUCT_ID, product_id);
        int i = sqldatabase.delete(HelperData.Table_wishlist, HelperData.KEY_PRODUCT_ID + " = " + product_id, null);

        int count = Integer.parseInt(AppConstant.wish_counter) - 1;
        AppConstant.wish_counter = "" + count;
        MainActivity.setWishCounter(AppConstant.wish_counter);
        showToast(ApplicationContext.getAppContext().getString(R.string.remove_from_wishlist));
    }


    public void insertUser(String user_fname, String user_lname, String user_id, String user_email, String bill_address,
                           String bill_city, String bill_state, String bill_state_code, String bill_country, String bill_country_code,
                           String bill_email, String bill_fname, String bill_lname, String bill_phone, String bill_postcode,
                           String ship_address, String ship_city, String ship_state, String ship_state_code, String ship_country,
                           String ship_country_code, String ship_fname, String ship_lname, String ship_postcode,
                           String bill_company, String ship_company) {

        String where = HelperData.KEY_USER_ID + " = " + user_id;
        String query = "SELECT * FROM " + HelperData.Table_userDetail + " WHERE " + where;

        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();

        cv.put(HelperData.KEY_USER_FNAME, user_fname);
        cv.put(HelperData.KEY_USER_ID, user_id);
        cv.put(HelperData.KEY_USER_EMAIL, user_email);
        cv.put(HelperData.KEY_USER_LNAME, user_lname);

        cv.put(HelperData.KEY_BILL_ADDRESS, bill_address);
        cv.put(HelperData.KEY_BILL_CITY, bill_city);
        cv.put(HelperData.KEY_BILL_STATE, bill_state);
        cv.put(HelperData.KEY_BILL_STATE_CODE, bill_state_code);
        cv.put(HelperData.KEY_BILL_COMPANY, bill_company);
        cv.put(HelperData.KEY_BILL_COUNTRY, bill_country);
        cv.put(HelperData.KEY_BILL_COUNTRY_CODE, bill_country_code);
        cv.put(HelperData.KEY_BILL_EMAIL, bill_email);
        cv.put(HelperData.KEY_BILL_FNAME, bill_fname);
        cv.put(HelperData.KEY_BILL_LNAME, bill_lname);
        cv.put(HelperData.KEY_BILL_PHONE, bill_phone);
        cv.put(HelperData.KEY_BILL_POSTCOE, bill_postcode);
        cv.put(HelperData.KEY_SHIP_ADDRESS, ship_address);
        cv.put(HelperData.KEY_SHIP_STATE, ship_state);
        cv.put(HelperData.KEY_SHIP_STATE_CODE, ship_state_code);
        cv.put(HelperData.KEY_SHIP_COUNTRY, ship_country);
        cv.put(HelperData.KEY_SHIP_COUNTRY_CODE, ship_country_code);
        cv.put(HelperData.KEY_SHIP_FNAME, ship_fname);
        cv.put(HelperData.KEY_SHIP_LNAME, ship_lname);
        cv.put(HelperData.KEY_SHIP_POSTCODE, ship_postcode);
        cv.put(HelperData.KEY_SHIP_COMPANY, ship_company);
        cv.put(HelperData.KEY_SHIP_CITY, ship_city);


        if (c.getCount() > 0) {
            c.moveToFirst();

            AppDebugLog.println("updated data is" + cv);

            long data = sqldatabase.update(HelperData.Table_userDetail, cv, where, null);

        } else {

            long data = sqldatabase.insert(HelperData.Table_userDetail, null, cv);


        }

    }

    public void insertUser1(String user_fname, String user_lname, String user_id, String user_email, String bill_address,
                            String bill_city, String bill_state, String bill_state_code, String bill_country, String bill_country_code,
                            String bill_email, String bill_fname, String bill_lname, String bill_phone, String bill_postcode,
                            String ship_address, String ship_city, String ship_state, String ship_state_code, String ship_country,
                            String ship_country_code, String ship_fname, String ship_lname, String ship_postcode,
                            String bill_company, String ship_company) {

        String where = HelperData.KEY_USER_ID + " = " + user_id;
        String query = "SELECT * FROM " + HelperData.Table_userDetail + " WHERE " + where;

        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();

        cv.put(HelperData.KEY_USER_FNAME, user_fname);
        cv.put(HelperData.KEY_USER_LNAME, user_lname);
        cv.put(HelperData.KEY_USER_ID, user_id);
        cv.put(HelperData.KEY_USER_EMAIL, user_email);

        cv.put(HelperData.KEY_BILL_ADDRESS, bill_address);
        cv.put(HelperData.KEY_BILL_CITY, bill_city);
        cv.put(HelperData.KEY_BILL_STATE, bill_state);
        cv.put(HelperData.KEY_BILL_STATE_CODE, bill_state_code);
        cv.put(HelperData.KEY_BILL_COMPANY, bill_company);
        cv.put(HelperData.KEY_BILL_COUNTRY, bill_country);
        cv.put(HelperData.KEY_BILL_COUNTRY_CODE, bill_country_code);
        cv.put(HelperData.KEY_BILL_EMAIL, bill_email);
        cv.put(HelperData.KEY_BILL_FNAME, bill_fname);
        cv.put(HelperData.KEY_BILL_LNAME, bill_lname);
        cv.put(HelperData.KEY_BILL_PHONE, bill_phone);
        cv.put(HelperData.KEY_BILL_POSTCOE, bill_postcode);
        cv.put(HelperData.KEY_SHIP_ADDRESS, ship_address);
        cv.put(HelperData.KEY_SHIP_STATE, ship_state);
        cv.put(HelperData.KEY_SHIP_STATE_CODE, ship_state_code);
        cv.put(HelperData.KEY_SHIP_COUNTRY, ship_country);
        cv.put(HelperData.KEY_SHIP_COUNTRY_CODE, ship_country_code);
        cv.put(HelperData.KEY_SHIP_FNAME, ship_fname);
        cv.put(HelperData.KEY_SHIP_LNAME, ship_lname);
        cv.put(HelperData.KEY_SHIP_POSTCODE, ship_postcode);
        cv.put(HelperData.KEY_SHIP_COMPANY, ship_company);
        cv.put(HelperData.KEY_SHIP_CITY, ship_city);


        if (c.getCount() > 0) {
            c.moveToFirst();
            AppDebugLog.println("updated data is" + cv);

            long data = sqldatabase.update(HelperData.Table_userDetail, cv, where, null);

        } else {

            long data = sqldatabase.insert(HelperData.Table_userDetail, null, cv);


        }

    }


    public ArrayList<BeanUserData> FetchBillAddress() {
        userBillinglist = new ArrayList<BeanUserData>();
        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_userDetail, null);
        int bill_address = c.getColumnIndex(HelperData.KEY_BILL_ADDRESS);
        int bill_city = c.getColumnIndex(HelperData.KEY_BILL_CITY);
        int bill_state = c.getColumnIndex(HelperData.KEY_BILL_STATE);
        int bill_company = c.getColumnIndex(HelperData.KEY_BILL_COMPANY);
        int bill_country = c.getColumnIndex(HelperData.KEY_BILL_COUNTRY);
        int bill_email = c.getColumnIndex(HelperData.KEY_BILL_EMAIL);
        int bill_fname = c.getColumnIndex(HelperData.KEY_BILL_FNAME);
        int bill_lname = c.getColumnIndex(HelperData.KEY_BILL_LNAME);
        int bill_phone = c.getColumnIndex(HelperData.KEY_BILL_PHONE);
        int bill_postcode = c.getColumnIndex(HelperData.KEY_BILL_POSTCOE);
        int bill_country_code = c.getColumnIndex(HelperData.KEY_BILL_COUNTRY_CODE);
        int bill_state_code = c.getColumnIndex(HelperData.KEY_BILL_STATE_CODE);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            BeanUserData bean = new BeanUserData();
            bean.setBilling_address_1(c.getString(bill_address));
            bean.setBilling_city(c.getString(bill_city));
            bean.setBilling_state(c.getString(bill_state));
            bean.setBilling_company(c.getString(bill_company));
            bean.setBilling_country(c.getString(bill_country));
            bean.setBilling_email(c.getString(bill_email));
            bean.setBilling_first_name(c.getString(bill_fname));
            bean.setBilling_last_name(c.getString(bill_lname));
            bean.setBilling_phone(c.getString(bill_phone));
            bean.setBilling_postcode(c.getString(bill_postcode));
            bean.setBilling_country_code(c.getString(bill_country_code));
            bean.setBilling_state_code(c.getString(bill_state_code));


            userBillinglist.add(bean);

        }
        return userBillinglist;

    }


    public ArrayList<BeanUserData> FetchShippingAddress() {

        userShippinglist = new ArrayList<BeanUserData>();
        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_userDetail, null);
        int ship_address = c.getColumnIndex(HelperData.KEY_SHIP_ADDRESS);
        int ship_city = c.getColumnIndex(HelperData.KEY_SHIP_CITY);
        int ship_state = c.getColumnIndex(HelperData.KEY_SHIP_STATE);
        int ship_country = c.getColumnIndex(HelperData.KEY_SHIP_COUNTRY);
        int ship_company = c.getColumnIndex(HelperData.KEY_SHIP_COMPANY);
        int ship_fname = c.getColumnIndex(HelperData.KEY_SHIP_FNAME);
        int ship_lname = c.getColumnIndex(HelperData.KEY_SHIP_LNAME);
        int ship_postcode = c.getColumnIndex(HelperData.KEY_SHIP_POSTCODE);
        int ship_country_code = c.getColumnIndex(HelperData.KEY_SHIP_COUNTRY_CODE);
        int ship_state_code = c.getColumnIndex(HelperData.KEY_SHIP_STATE_CODE);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            BeanUserData bean = new BeanUserData();
            bean.setShipping_address_1(c.getString(ship_address));
            bean.setShipping_city(c.getString(ship_city));
            bean.setShipping_state(c.getString(ship_state));
            bean.setShipping_country(c.getString(ship_country));
            bean.setShipping_company(c.getString(ship_company));
            bean.setShipping_first_name(c.getString(ship_fname));
            bean.setShipping_last_name(c.getString(ship_lname));
            bean.setShipping_postcode(c.getString(ship_postcode));
            bean.setShipping_country_code(c.getString(ship_country_code));
            bean.setShipping_state_code(c.getString(ship_state_code));

            AppDebugLog.println("shippin data= " + c.getString(ship_fname) + " " + c.getString(ship_address)
                    + " " + c.getString(ship_city) + " " + c.getString(ship_state) + " " + c.getString(ship_country) + " " + c.getString(ship_lname)
                    + " " + c.getString(ship_postcode));
            userShippinglist.add(bean);

        }

        return userShippinglist;
    }


    public void insertBilling(String user_id, String fname, String lname, String address, String country, String state, String city,
                              String postcode, String email, String phone, String country_code, String state_code, String company) {
        String query;

        String where = HelperData.KEY_USER_ID + " = " + user_id;
        if (user_id.equals("0.5")) {
            query = "SELECT * FROM " + HelperData.Table_userDetail;
        } else {
            query = "SELECT * FROM " + HelperData.Table_userDetail + " WHERE " + where;
        }

        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();

        cv.put(HelperData.KEY_BILL_ADDRESS, address);
        cv.put(HelperData.KEY_USER_ID, user_id);
        cv.put(HelperData.KEY_BILL_CITY, city);
        cv.put(HelperData.KEY_BILL_STATE, state);
        cv.put(HelperData.KEY_BILL_COUNTRY, country);
        cv.put(HelperData.KEY_BILL_COMPANY, company);
        cv.put(HelperData.KEY_BILL_EMAIL, email);
        cv.put(HelperData.KEY_BILL_FNAME, fname);
        cv.put(HelperData.KEY_BILL_LNAME, lname);
        cv.put(HelperData.KEY_BILL_PHONE, phone);
        cv.put(HelperData.KEY_BILL_POSTCOE, postcode);
        cv.put(HelperData.KEY_BILL_COUNTRY_CODE, country_code);
        cv.put(HelperData.KEY_BILL_STATE_CODE, state_code);


        if (c.getCount() > 0) {
            c.moveToFirst();

            AppDebugLog.println("updated data is" + cv);

            long data = sqldatabase.update(HelperData.Table_userDetail, cv, where, null);

        } else {

            long data = sqldatabase.insert(HelperData.Table_userDetail, null, cv);


        }

    }

    public void insertShipping(String user_id, String fname, String lname, String address, String country, String state, String city,
                               String postcode, String country_code, String state_code, String company) {


        String where = HelperData.KEY_USER_ID + " = " + user_id;
        String query = "SELECT * FROM " + HelperData.Table_userDetail + " WHERE " + where;

        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();

        cv.put(HelperData.KEY_SHIP_ADDRESS, address);
        cv.put(HelperData.KEY_SHIP_STATE, state);
        cv.put(HelperData.KEY_SHIP_COUNTRY, country);
        cv.put(HelperData.KEY_SHIP_COMPANY, company);
        cv.put(HelperData.KEY_SHIP_FNAME, fname);
        cv.put(HelperData.KEY_SHIP_LNAME, lname);
        cv.put(HelperData.KEY_SHIP_POSTCODE, postcode);
        cv.put(HelperData.KEY_SHIP_CITY, city);
        cv.put(HelperData.KEY_SHIP_COUNTRY_CODE, country_code);
        cv.put(HelperData.KEY_SHIP_STATE_CODE, state_code);

        if (c.getCount() > 0) {
            c.moveToFirst();
            AppDebugLog.println("updated data is" + cv);


            long data = sqldatabase.update(HelperData.Table_userDetail, cv, where, null);

        } else {

            long data = sqldatabase.insert(HelperData.Table_userDetail, null, cv);


        }
    }


    public void deletedData() {

        int i = sqldatabase.delete(HelperData.Table_userDetail, null, null);

    }

    public void clearDatabase() {

//        int cartdata = sqldatabase.delete(HelperData.Table_addtocart, null, null);
        int categorydata = sqldatabase.delete(HelperData.Table_categorys, null, null);
        int productdetail = sqldatabase.delete(HelperData.Table_product_detail, null, null);
        int productreviews = sqldatabase.delete(HelperData.Table_product_reviews, null, null);
        int productlist = sqldatabase.delete(HelperData.Table_product_list, null, null);
        int subcat = sqldatabase.delete(HelperData.Table_sub_category, null, null);
        int coutry = sqldatabase.delete(HelperData.Table_country, null, null);
        int state = sqldatabase.delete(HelperData.Table_states, null, null);
        int orderdetail = sqldatabase.delete(HelperData.Table_order_detail, null, null);
        int orders = sqldatabase.delete(HelperData.Table_orders, null, null);
        int method = sqldatabase.delete(HelperData.Table_method, null, null);
        int shipmethod = sqldatabase.delete(HelperData.Table_ship_method, null, null);

    }

    public void deletedMethod() {

        ContentValues cv = new ContentValues();

        int i = sqldatabase.delete(HelperData.Table_method, null, null);


    }

    public void deletedProductList() {

        ContentValues cv = new ContentValues();

        int i = sqldatabase.delete(HelperData.Table_product_list, null, null);


    }

    public void deletedCategory() {

        ContentValues cv = new ContentValues();

        int i = sqldatabase.delete(HelperData.Table_categorys, null, null);


    }

    public void deletedSubCategory() {

        ContentValues cv = new ContentValues();

        int i = sqldatabase.delete(HelperData.Table_sub_category, null, null);


    }

    public void deletedCartData() {

        ContentValues cv = new ContentValues();

        int i = sqldatabase.delete(HelperData.Table_addtocart, null, null);


    }


    public void deletedProdutDetail() {

        ContentValues cv = new ContentValues();

        int i = sqldatabase.delete(HelperData.Table_product_detail, null, null);


    }

    public ArrayList<BeanUserData> FetchUserData() {

        userDataArrayList = new ArrayList<BeanUserData>();
        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_userDetail, null);
        int user_id = c.getColumnIndex(HelperData.KEY_USER_ID);
        int user_name = c.getColumnIndex(HelperData.KEY_USER_FNAME);
        int user_email = c.getColumnIndex(HelperData.KEY_USER_EMAIL);
        int user_lname = c.getColumnIndex(HelperData.KEY_USER_LNAME);

        int bill_address = c.getColumnIndex(HelperData.KEY_BILL_ADDRESS);
        int bill_city = c.getColumnIndex(HelperData.KEY_BILL_CITY);
        int bill_state = c.getColumnIndex(HelperData.KEY_BILL_STATE);
        int bill_country = c.getColumnIndex(HelperData.KEY_BILL_COUNTRY);
        int bill_email = c.getColumnIndex(HelperData.KEY_BILL_EMAIL);
        int bill_fname = c.getColumnIndex(HelperData.KEY_BILL_FNAME);
        int bill_lname = c.getColumnIndex(HelperData.KEY_BILL_LNAME);
        int bill_phone = c.getColumnIndex(HelperData.KEY_BILL_PHONE);
        int bill_postcode = c.getColumnIndex(HelperData.KEY_BILL_POSTCOE);
        int bill_company = c.getColumnIndex(HelperData.KEY_BILL_COMPANY);
        int bill_country_code = c.getColumnIndex(HelperData.KEY_BILL_COUNTRY_CODE);
        int bill_state_code = c.getColumnIndex(HelperData.KEY_BILL_STATE_CODE);


        int ship_address = c.getColumnIndex(HelperData.KEY_SHIP_ADDRESS);
        int ship_city = c.getColumnIndex(HelperData.KEY_SHIP_CITY);
        int ship_state = c.getColumnIndex(HelperData.KEY_SHIP_STATE);
        int ship_country = c.getColumnIndex(HelperData.KEY_SHIP_COUNTRY);
        int ship_fname = c.getColumnIndex(HelperData.KEY_SHIP_FNAME);
        int ship_lname = c.getColumnIndex(HelperData.KEY_SHIP_LNAME);
        int ship_postcode = c.getColumnIndex(HelperData.KEY_SHIP_POSTCODE);
        int ship_company = c.getColumnIndex(HelperData.KEY_SHIP_COMPANY);
        int ship_country_code = c.getColumnIndex(HelperData.KEY_SHIP_COUNTRY_CODE);
        int ship_state_code = c.getColumnIndex(HelperData.KEY_SHIP_STATE_CODE);


        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            BeanUserData bean = new BeanUserData();
            bean.setDisplay_name(c.getString(user_name));
            bean.setID(c.getString(user_id));
            bean.setUser_email(c.getString(user_email));
            bean.setUser_last_name(c.getString(user_lname));

            bean.setBilling_address_1(c.getString(bill_address));
            bean.setBilling_city(c.getString(bill_city));
            bean.setBilling_state(c.getString(bill_state));
            bean.setBilling_country(c.getString(bill_country));
            bean.setBilling_email(c.getString(bill_email));
            bean.setBilling_first_name(c.getString(bill_fname));
            bean.setBilling_last_name(c.getString(bill_lname));
            bean.setBilling_phone(c.getString(bill_phone));
            bean.setBilling_postcode(c.getString(bill_postcode));
            bean.setBilling_company(c.getString(bill_company));
            bean.setBilling_country_code(c.getString(bill_country_code));
            bean.setBilling_state_code(c.getString(bill_state_code));


            bean.setShipping_address_1(c.getString(ship_address));
            bean.setShipping_city(c.getString(ship_city));
            bean.setShipping_state(c.getString(ship_state));
            bean.setShipping_country(c.getString(ship_country));
            bean.setShipping_first_name(c.getString(ship_fname));
            bean.setShipping_last_name(c.getString(ship_lname));
            bean.setShipping_postcode(c.getString(ship_postcode));
            bean.setShipping_company(c.getString(ship_company));
            bean.setShipping_country_code(c.getString(ship_country_code));
            bean.setShipping_state_code(c.getString(ship_state_code));


            userDataArrayList.add(bean);

        }

        return userDataArrayList;
    }

    public void insertTOcart(String product_id, String product_name, String variations,
                             String variations_id, String variation_price, String qnty, String image, String subTotal, String ori_price, String tax_status) {
        String where;
        String query;
        if (variations_id != null) {

            where = HelperData.KEY_VARIATIONS + " = " + "'" + variations + "'";
            query = "SELECT * FROM " + HelperData.Table_addtocart + " WHERE " + where;
        } else {
            where = HelperData.KEY_PRODUCT_ID + " = " + product_id;
            query = "SELECT * FROM " + HelperData.Table_addtocart + " WHERE " + HelperData.KEY_PRODUCT_ID + " = " + product_id;
        }


        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();
        cv.put(HelperData.KEY_PRODUCT_ID, product_id);
        cv.put(HelperData.KEY_PRODUCT_NAME, product_name);
        cv.put(HelperData.KEY_VARIATION_ID, variations_id);
        cv.put(HelperData.KEY_VARIATIONS, variations);
        cv.put(HelperData.KEY_PRICE, variation_price);
        cv.put(HelperData.KEY_QNTY, qnty);
        cv.put(HelperData.KEY_IMAGE, image);
        cv.put(HelperData.KEY_SUBTOTAL, subTotal);
        cv.put(HelperData.KEY_ORI_PRICE, ori_price);
        cv.put(HelperData.KEY_TAX_STATUS, tax_status);

        if (qnty.equalsIgnoreCase("1")) {

        } else if (c.getCount() > 0) {
            c.moveToFirst();
            long data = sqldatabase.update(HelperData.Table_addtocart, cv, where, null);

            showToast(ApplicationContext.getAppContext().getString(R.string.updated_cart));
        } else {
            long data = sqldatabase.insert(HelperData.Table_addtocart, null, cv);


            showToast(ApplicationContext.getAppContext().getString(R.string.item_added));
            int count = Integer.parseInt(AppConstant.counter) + 1;
            AppConstant.counter = "" + count;
            MainActivity.setCounter(AppConstant.counter);
        }

    }

    public void deletedFromCart(String product_id, String variations_id, String variations) {
        ContentValues cv = new ContentValues();
        if (variations_id != null) {
            cv.put(HelperData.KEY_VARIATION_ID, variations_id);
            int i = sqldatabase.delete(HelperData.Table_addtocart, HelperData.KEY_VARIATIONS + " = " + "'" + variations + "'", null);
            int count = Integer.parseInt(AppConstant.counter) - 1;
            AppConstant.counter = "" + count;
            MainActivity.setCounter(AppConstant.counter);

        } else {


            cv.put(HelperData.KEY_PRODUCT_ID, product_id);
            int i = sqldatabase.delete(HelperData.Table_addtocart, HelperData.KEY_PRODUCT_ID + " = " + product_id, null);
            int count = Integer.parseInt(AppConstant.counter) - 1;
            AppConstant.counter = "" + count;
            MainActivity.setCounter(AppConstant.counter);

        }
    }

    public ArrayList<BeanCart> fatchCartData() {

        cartlist = new ArrayList<BeanCart>();
        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_addtocart, null);
        int product_id = c.getColumnIndex(HelperData.KEY_PRODUCT_ID);
        int product_name = c.getColumnIndex(HelperData.KEY_PRODUCT_NAME);
        int variations = c.getColumnIndex(HelperData.KEY_VARIATIONS);
        int variations_id = c.getColumnIndex(HelperData.KEY_VARIATION_ID);
        int variation_price = c.getColumnIndex(HelperData.KEY_PRICE);
        int qnty = c.getColumnIndex(HelperData.KEY_QNTY);
        int image = c.getColumnIndex(HelperData.KEY_IMAGE);
        int subTotal = c.getColumnIndex(HelperData.KEY_SUBTOTAL);
        int max_stock = c.getColumnIndex(HelperData.KEY_MAX_STOCK);
        int ori_price = c.getColumnIndex(HelperData.KEY_ORI_PRICE);
        int tax_status = c.getColumnIndex(HelperData.KEY_TAX_STATUS);


        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            BeanCart beanCart = new BeanCart();
            beanCart.setProduct_id(c.getString(product_id));
            beanCart.setProduct_name(c.getString(product_name));
            beanCart.setVariations(c.getString(variations));
            beanCart.setVariation_id(c.getString(variations_id));
            beanCart.setPrice(c.getString(variation_price));
            beanCart.setQnty(c.getString(qnty));
            beanCart.setImage(c.getString(image));
            beanCart.setSubTotal(c.getString(subTotal));
            beanCart.setMax_stock(c.getString(max_stock));
            beanCart.setOri_price(c.getString(ori_price));
            beanCart.setTax_status(c.getString(tax_status));
            cartlist.add(beanCart);

        }

        return cartlist;
    }


    public int getCartCount() {
        String sql = "SELECT * FROM " + HelperData.Table_addtocart;
        Cursor cursor = sqldatabase.rawQuery(sql, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public int getWishCount() {
        String countQuery = "SELECT  * FROM " + HelperData.Table_wishlist;

        Cursor cursor = sqldatabase.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public ArrayList<Double> fechtotal() {
        arrayist_total = new ArrayList<Double>();
        String sql = " SELECT " + HelperData.KEY_SUBTOTAL + " FROM " + HelperData.Table_addtocart;
        Cursor c = sqldatabase.rawQuery(sql, null);
        int subTotal = c.getColumnIndex(HelperData.KEY_SUBTOTAL);
        //int qnty = c.getColumnIndex(HelperData.KEY_QNTY);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            String p = "" + c.getString(subTotal);
            //String q = ""+c.getString(qnty);

            double j = Double.parseDouble(p);


            arrayist_total.add(j);


        }
        return arrayist_total;
    }


    public void insert_cart(String product_id, String product_name, String variations,
                            String variations_id, String variation_price, String qnty, String image,
                            String subTotal, String stock, String ori_price, String tax_status) {
        String where;
        String query;
        if (variations_id != null) {

            where = HelperData.KEY_VARIATIONS + " = " + "'" + variations + "'";
            query = "SELECT * FROM " + HelperData.Table_addtocart + " WHERE " + where;
        } else {
            where = HelperData.KEY_PRODUCT_ID + " = " + product_id;
            query = "SELECT * FROM " + HelperData.Table_addtocart + " WHERE " +
                    HelperData.KEY_PRODUCT_ID + " = " + product_id;
        }


        Cursor c = sqldatabase.rawQuery(query, null);
        ContentValues cv = new ContentValues();
        cv.put(HelperData.KEY_PRODUCT_ID, product_id);
        cv.put(HelperData.KEY_PRODUCT_NAME, product_name);
        cv.put(HelperData.KEY_VARIATION_ID, variations_id);
        cv.put(HelperData.KEY_VARIATIONS, variations);
        cv.put(HelperData.KEY_PRICE, variation_price);
        cv.put(HelperData.KEY_QNTY, qnty);
        cv.put(HelperData.KEY_IMAGE, image);
        cv.put(HelperData.KEY_SUBTOTAL, subTotal);
        cv.put(HelperData.KEY_MAX_STOCK, stock);
        cv.put(HelperData.KEY_ORI_PRICE, ori_price);
        cv.put(HelperData.KEY_TAX_STATUS, tax_status);

        if (c.getCount() > 0) {
            c.moveToFirst();
            long data = sqldatabase.update(HelperData.Table_addtocart, cv, where, null);

            showToast(ApplicationContext.getAppContext().getString(R.string.updated_cart));
        } else {
            long data = sqldatabase.insert(HelperData.Table_addtocart, null, cv);
            showToast(ApplicationContext.getAppContext().getString(R.string.item_added));
            int count = Integer.parseInt(AppConstant.counter) + 1;
            AppConstant.counter = "" + count;
            MainActivity.setCounter(AppConstant.counter);
            ProductDetailActivity.setCounter(AppConstant.counter);
        }
    }

    public void Insert_PorductDetail(String product_id, String product_detail) {
        String where;
        String query;
        if (product_id != null) {

            where = HelperData.KEY_PRODUCT_ID + " = " + product_id;
            query = "SELECT * FROM " + HelperData.Table_product_detail + " WHERE " + where;

            Cursor c = sqldatabase.rawQuery(query, null);
            ContentValues cv = new ContentValues();
            cv.put(HelperData.KEY_PRODUCT_ID, product_id);
            cv.put(HelperData.KEY_PRODUCT_DETAIL, product_detail);

            if (c.getCount() > 0) {
                c.moveToFirst();
                long data = sqldatabase.update(HelperData.Table_product_detail, cv, where, null);

                // showToast("Updated detail");
            } else {
                long data = sqldatabase.insert(HelperData.Table_product_detail, null, cv);

                //  showToast("detail Added");
            }

        }

    }


    public void Insert_Reviews(String product_id, String product_reviews) {
        String where;
        String query;
        if (product_id != null) {

            where = HelperData.KEY_PRODUCT_ID + " = " + product_id;
            query = "SELECT * FROM " + HelperData.Table_product_reviews + " WHERE " + where;

            Cursor c = sqldatabase.rawQuery(query, null);
            ContentValues cv = new ContentValues();
            cv.put(HelperData.KEY_PRODUCT_ID, product_id);
            cv.put(HelperData.KEY_PRODUCT_REVIEWS, product_reviews);

            if (c.getCount() > 0) {
                c.moveToFirst();
                long data = sqldatabase.update(HelperData.Table_product_reviews, cv, where, null);

                //  showToast("Updated review");
            } else {
                long data = sqldatabase.insert(HelperData.Table_product_reviews, null, cv);

                // showToast("review Added");
            }

        }

    }

    public void Insert_ProductList(String CATEGORYSLUG, String product_list) {
        String where;
        String query;
        if (product_list != null) {

            where = HelperData.KEY_CATEGORYSLUG + " = " + "'" + CATEGORYSLUG + "'";
            query = "SELECT * FROM " + HelperData.Table_product_list + " WHERE " + where;

            Cursor c = sqldatabase.rawQuery(query, null);
            ContentValues cv = new ContentValues();

            cv.put(HelperData.KEY_PRODUCT_LIST, product_list);
            cv.put(HelperData.KEY_CATEGORYSLUG, CATEGORYSLUG);

            if (c.getCount() > 0) {
                c.moveToFirst();
                long data = sqldatabase.update(HelperData.Table_product_list, cv, where, null);

                // showToast("Updated product");
            } else {
                long data = sqldatabase.insert(HelperData.Table_product_list, null, cv);

                // showToast("product list Added");
            }

        }

    }

    public void Insert_ProductList1(String product_list) {
        String where;
        String query;
        if (product_list != null) {

            // where = HelperData.KEY_CATEGORYSLUG + " = " + "'"+CATEGORYSLUG+"'";
            query = "SELECT * FROM " + HelperData.Table_product_list;

            Cursor c = sqldatabase.rawQuery(query, null);
            ContentValues cv = new ContentValues();

            cv.put(HelperData.KEY_PRODUCT_LIST, product_list);

            long data = sqldatabase.insert(HelperData.Table_product_list, null, cv);

           /* if (c.getCount() > 0) {
                c.moveToFirst();
                long data =  sqldatabase.update(HelperData.Table_product_list, cv, where,null);

                //showToast("Updated product");
            }
            else {


                //showToast("product list Added");
            }*/

        }

    }


    public void Inster_Category(String category_id, String category_name, String description,
                                String image, String count, String slug, String child) {

        String where;
        String query;
        if (category_id != null) {

            where = HelperData.KEY_CATEGORY_ID + " = " + category_id;
            query = "SELECT * FROM " + HelperData.Table_categorys + " WHERE " + where;

            Cursor c = sqldatabase.rawQuery(query, null);
            ContentValues cv = new ContentValues();
            cv.put(HelperData.KEY_CATEGORY_ID, category_id);
            cv.put(HelperData.KEY_CATEGORY_NAME, category_name);
            cv.put(HelperData.KEY_CATEGORY_DESCRIPTION, description);
            cv.put(HelperData.KEY_CATEGORY_IMAGE, image);
            cv.put(HelperData.KEY_CATEGORY_COUNT, count);
            cv.put(HelperData.KEY_CATEGORY_SLUG, slug);
            cv.put(HelperData.KEY_CATEGORY_CHILD, child);


            if (c.getCount() > 0) {
                c.moveToFirst();
                long data = sqldatabase.update(HelperData.Table_categorys, cv, where, null);

                //  showToast("Updated Category");
            } else {
                long data = sqldatabase.insert(HelperData.Table_categorys, null, cv);

                // showToast("Category Added");
            }

        }

    }

    public void Insert_Subcategorys(String subcategoryId, String subcategoryName, String subcategoryDes,
                                    String subcategoryImage, String subcategoryCount,
                                    String subcategorySlug, String subcategoryParent, String subChild) {

        String where;
        String query;
        if (subcategoryId != null) {

            where = HelperData.KEY_SUBCATEGORY_ID + " = " + subcategoryId;
            query = "SELECT * FROM " + HelperData.Table_sub_category + " WHERE " + where;

            Cursor c = sqldatabase.rawQuery(query, null);
            ContentValues cv = new ContentValues();
            cv.put(HelperData.KEY_SUBCATEGORY_ID, subcategoryId);
            cv.put(HelperData.KEY_SUBCATEGORY_NAME, subcategoryName);
            cv.put(HelperData.KEY_SUBCATEGORY_DES, subcategoryDes);
            cv.put(HelperData.KEY_SUBCATEGORY_IMAGE, subcategoryImage);
            cv.put(HelperData.KEY_SUBCATEGORY_COUNT, subcategoryCount);
            cv.put(HelperData.KEY_SUBCATEGORY_SLUG, subcategorySlug);
            cv.put(HelperData.KEY_SUBCATEGORY_PARENT, subcategoryParent);
            cv.put(HelperData.KEY_SUBCATEGORY_CHILD, subChild);


            if (c.getCount() > 0) {
                c.moveToFirst();
                long data = sqldatabase.update(HelperData.Table_sub_category, cv, where, null);

                //  showToast("Updated subCategory");
            } else {
                long data = sqldatabase.insert(HelperData.Table_sub_category, null, cv);

                //  showToast("subCategory Added");
            }

        }

    }

    public ArrayList<BeanSubCategory> fetchSubCategorys(String catid) {
        subcategoriesList = new ArrayList<BeanSubCategory>();
        String where;
        where = HelperData.KEY_SUBCATEGORY_PARENT + " = " + catid;
        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_sub_category + " WHERE " + where, null);

        int subcategory_id = c.getColumnIndex(HelperData.KEY_SUBCATEGORY_ID);
        int subcategory_name = c.getColumnIndex(HelperData.KEY_SUBCATEGORY_NAME);
        int subcategory_des = c.getColumnIndex(HelperData.KEY_SUBCATEGORY_DES);
        int subcategory_image = c.getColumnIndex(HelperData.KEY_SUBCATEGORY_IMAGE);
        int subcategory_count = c.getColumnIndex(HelperData.KEY_SUBCATEGORY_COUNT);
        int subcategory_slug = c.getColumnIndex(HelperData.KEY_SUBCATEGORY_SLUG);
        int subcategory_parent = c.getColumnIndex(HelperData.KEY_SUBCATEGORY_PARENT);
        int sub_child = c.getColumnIndex(HelperData.KEY_SUBCATEGORY_CHILD);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            BeanSubCategory beanCart = new BeanSubCategory();
            beanCart.setSubcategory_id(c.getString(subcategory_id));
            beanCart.setSubcategory_name(c.getString(subcategory_name));
            beanCart.setParent(c.getString(subcategory_parent));
            beanCart.setCount(c.getString(subcategory_count));
            beanCart.setDescription(c.getString(subcategory_des));
            beanCart.setImage(c.getString(subcategory_image));
            beanCart.setSlug(c.getString(subcategory_slug));
            beanCart.setChild(c.getString(sub_child));

            AppDebugLog.println("catslug=" + c.getString(subcategory_slug) + ", catID= " + c.getString(subcategory_id));

            subcategoriesList.add(beanCart);
        }

        return subcategoriesList;
    }

    public ArrayList<BeanCategory> fetchCategorys() {
        categorieslist = new ArrayList<BeanCategory>();
        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_categorys, null);

        int category_id = c.getColumnIndex(HelperData.KEY_CATEGORY_ID);
        int category_name = c.getColumnIndex(HelperData.KEY_CATEGORY_NAME);
        int category_child = c.getColumnIndex(HelperData.KEY_CATEGORY_CHILD);
        int category_count = c.getColumnIndex(HelperData.KEY_CATEGORY_COUNT);
        int category_des = c.getColumnIndex(HelperData.KEY_CATEGORY_DESCRIPTION);
        int category_slug = c.getColumnIndex(HelperData.KEY_CATEGORY_SLUG);
        int category_image = c.getColumnIndex(HelperData.KEY_CATEGORY_IMAGE);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            BeanCategory beanCart = new BeanCategory();
            beanCart.setCategory_id(c.getString(category_id));
            beanCart.setCategory_name(c.getString(category_name));
            beanCart.setChild(c.getString(category_child));
            beanCart.setCount(c.getString(category_count));
            beanCart.setDescription(c.getString(category_des));
            beanCart.setImage(c.getString(category_image));
            beanCart.setSlug(c.getString(category_slug));


            AppDebugLog.println("catslug=" + c.getString(category_slug) + ", catID= " + c.getString(category_id));

            categorieslist.add(beanCart);
        }

        return categorieslist;
    }

    public ArrayList<BeanWishlist> fetchWishlist() {
        wishlist = new ArrayList<BeanWishlist>();
        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_wishlist, null);
        int product_id = c.getColumnIndex(HelperData.KEY_PRODUCT_ID);
        int product_name = c.getColumnIndex(HelperData.KEY_PRODUCT_NAME);
        int description = c.getColumnIndex(HelperData.KEY_DESCRIPTION);
        int price = c.getColumnIndex(HelperData.KEY_PRICE);
        int image = c.getColumnIndex(HelperData.KEY_IMAGE);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            BeanWishlist beanWishlist = new BeanWishlist();
            beanWishlist.setProduct_id(c.getString(product_id));
            beanWishlist.setTitle(c.getString(product_name));
            beanWishlist.setDescription(c.getString(description));
            beanWishlist.setSale_price(c.getString(price));
            beanWishlist.setImage(c.getString(image));
            wishlist.add(beanWishlist);
        }

        return wishlist;
    }


    public String fetchProductList(String catslug) {

        //Cursor c = sqldatabase.rawQuery("SELECT * FROM HelperData.Table_product_detail WHERE TRIM(name) = '"+name.trim()+"'", null);
        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_product_list +
                " WHERE " + HelperData.KEY_CATEGORYSLUG + " = " + "'" + catslug + "'", null);
        int list = c.getColumnIndex(HelperData.KEY_PRODUCT_LIST);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            productlist = c.getString(list);

        }

        return productlist;
    }

    public String fetchProductList1() {

        //Cursor c = sqldatabase.rawQuery("SELECT * FROM HelperData.Table_product_detail WHERE TRIM(name) = '"+name.trim()+"'", null);
        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_product_list, null);
        int list = c.getColumnIndex(HelperData.KEY_PRODUCT_LIST);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            productlist = c.getString(list);

        }

        return productlist;
    }


    public String fetchProductDetail(String product_id) {

        if (product_id != null) {

            Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_product_detail +
                    " WHERE " + HelperData.KEY_PRODUCT_ID + " = " + product_id, null);
            int detail = c.getColumnIndex(HelperData.KEY_PRODUCT_DETAIL);

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                list_detail = c.getString(detail);
            }
        }

        return list_detail;
    }

    public int fetchproductQuantity(String product_id) {
        int qnty = 1;
        if (productlist != null) {
            Cursor c = sqldatabase.rawQuery("SELECT " + HelperData.KEY_QNTY + " FROM " + HelperData.Table_addtocart +
                    " WHERE " + HelperData.KEY_PRODUCT_ID + " = " + product_id, null);
            int qty = c.getColumnIndex(HelperData.KEY_QNTY);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                qnty = Integer.parseInt(c.getString(qty));
                // showToast("qnty====== " + qnty);
            }
            return qnty;

        } else {
            return 1;
        }

    }


    public String fetchProductReviews(String product_id) {

        if (product_id != null) {


            Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_product_reviews + " WHERE " + HelperData.KEY_PRODUCT_ID + " = " + product_id, null);
            int review = c.getColumnIndex(HelperData.KEY_PRODUCT_REVIEWS);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                reviews = c.getString(review);

            }

        }

        return reviews;
    }

    public boolean isProductExist(String product_id) {

        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_product_detail + " WHERE " + HelperData.KEY_PRODUCT_ID + " = " + product_id, null);
        if (c.moveToFirst()) {
            //showToast("detail Exist");
            return true;
        } else {
            return false;
        }

    }

    public boolean isStateExist(String state_code) {

        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_states + " WHERE " + HelperData.KEY_STATE_CODE + " = " + "'" + state_code + "'", null);
        if (c.moveToFirst()) {
            //showToast("state Exist");
            return true;
        } else {
            return false;
        }

    }


    public boolean isCountryExist(String country_code) {

        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_country + " WHERE " + HelperData.KEY_COUNTRY_CODE + " = " + "'" + country_code + "'", null);
        if (c.moveToFirst()) {
            // showToast("country Exist");
            return true;
        } else {
            return false;
        }

    }

    public boolean isWishExist(String id) {

        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_wishlist + " WHERE " + HelperData.KEY_PRODUCT_ID + " = " + "'" + id + "'", null);
        if (c.moveToFirst()) {
            //showToast("Already added");
            return true;
        } else {
            return false;
        }

    }

    public int isCountrylist() {
        String countQuery = "SELECT  * FROM " + HelperData.Table_country;

        Cursor cursor = sqldatabase.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    public ArrayList<BeanCountry> fetchCountry() {
        countrylist = new ArrayList<BeanCountry>();

        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_country, null);
        int country_code = c.getColumnIndex(HelperData.KEY_COUNTRY_CODE);
        int country_name = c.getColumnIndex(HelperData.KEY_COUNTRY_NAME);


        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            BeanCountry country = new BeanCountry();
            country.setCountry_code(c.getString(country_code));
            country.setCountry_name(c.getString(country_name));

            countrylist.add(country);

            //AppDebugLog.println("country is==  "+c.getString(country_code));
        }

        return countrylist;
    }

    public ArrayList<BeanState> fetchStates(String countryCode) {
        statelist = new ArrayList<BeanState>();

        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_states + " WHERE " + HelperData.KEY_COUNTRY_CODE + " = " + "'" + countryCode + "'", null);
        int state_code = c.getColumnIndex(HelperData.KEY_STATE_CODE);
        int state_name = c.getColumnIndex(HelperData.KEY_STATE_NAME);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            BeanState state = new BeanState();
            state.setState_code(c.getString(state_code));
            state.setState_name(c.getString(state_name));

            statelist.add(state);

        }

        return statelist;
    }

    public String fetchOrderDetail(String ordID) {

        if (ordID != null) {

            //Cursor c = sqldatabase.rawQuery("SELECT * FROM HelperData.Table_product_detail WHERE TRIM(name) = '"+name.trim()+"'", null);
            Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_order_detail + " WHERE " + HelperData.KEY_ORDER_ID + " = " + ordID, null);
            int detail = c.getColumnIndex(HelperData.KEY_ORDER_DETAIL);

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                orderDetail = c.getString(detail);

            }

        }

        return orderDetail;
    }

    public String fetchOrders(String user_ID) {

        if (user_ID != null) {


            Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_orders + " WHERE " + HelperData.KEY_USER_ID + " = " + user_ID, null);
            int detail = c.getColumnIndex(HelperData.KEY_ORDERS);

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                ordersList = c.getString(detail);

            }

        }

        return ordersList;
    }

    public String fetchMethod() {


        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_method, null);
        int detail = c.getColumnIndex(HelperData.KEY_METHOD);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            method = c.getString(detail);

        }

        return method;
    }

    public boolean isShipMethodExist(String method_id) {

        Cursor c = sqldatabase.rawQuery("SELECT * FROM " + HelperData.Table_ship_method + " WHERE " + HelperData.KEY_METHOD_ID + " = " + "'" + method_id + "'", null);
        if (c.moveToFirst()) {
            //showToast("methods Exist");
            return true;
        } else {
            return false;
        }

    }


}
