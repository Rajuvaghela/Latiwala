package com.lujayn.latiwala.parser;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.bean.BeanCategory;
import com.lujayn.latiwala.bean.BeanDetailProduct;
import com.lujayn.latiwala.bean.BeanOrderDetail;
import com.lujayn.latiwala.bean.BeanPaymentInfo;
import com.lujayn.latiwala.bean.BeanSubCategory;
import com.lujayn.latiwala.bean.BeanUserData;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.AppConstant.HTTPResponseCode;
import com.lujayn.latiwala.common.ApplicationContext;
import com.lujayn.latiwala.common.ApplicationData;
import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.database.DataWrite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Shailesh on 21/04/17.
 */

public class ParseManager {


    public static void parseDataUpdateResponse(String response) {
        String category_id,category_name,count,child,description,slug,image;
       DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        Log.e("parseResponse:",""+response);
        AppDebugLog.println("Response in parseResponse : " + response);
        JSONObject object;
        String data=response;

        if (response.length() > 0) {
            ApplicationData appdata = ApplicationData.getSharedInstance();
            appdata.setResponseCode(HTTPResponseCode.CategoryUpdate);
            ArrayList<String> sluglist = appdata.getSluglist();
            ArrayList<BeanCategory> categories = appdata.getCategoryList();

            try {
                object = new JSONObject(data);

                if (object.getInt("success") == 1) {
                    JSONObject jsonObject = object.optJSONObject("data");
                    JSONArray jsonArray = jsonObject.optJSONArray("product_categories");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        BeanCategory beanCategory = new BeanCategory();
                        JSONObject object1 = jsonArray.optJSONObject(i);

                        category_id = object1.getString("category_id");
                        category_name = object1.getString("category_name");
                        description = object1.getString("description");
                        image = object1.getString("image");
                        count = object1.getString("count");
                        slug = object1.getString("slug");
                        child = object1.getString("child");

                        beanCategory.setCategory_id(category_id);
                        beanCategory.setCategory_name(category_name);
                        beanCategory.setSlug(slug);
                        beanCategory.setDescription(description);
                        beanCategory.setImage(image);
                        beanCategory.setCount(count);
                        beanCategory.setChild(child);

                        if (child.equals("0")){

                            AppDebugLog.println("slug = "+slug);
                            sluglist.add(slug);
                        }
                        categories.add(beanCategory);

                        dataWrite.Inster_Category(category_id, category_name, description, image, count, slug, child);

                    }


                } else {
                    ApplicationData.getSharedInstance().setResponseCode(HTTPResponseCode.ServerError);
                    dataWrite.showToast(ApplicationContext.getAppContext().getString(R.string.unknown_error));
                }





            } catch (Exception e) {
                // TODO: handle exception

                AppDebugLog.println("error =" + e);

            }
        }else {
            ApplicationData.getSharedInstance().setResponseCode(HTTPResponseCode.ServerError);
        }
    }

    public static void parseSubcategoryUpdateResponse(String response) {
        Log.e("Subcategory res:",""+response);
        AppDebugLog.println("Response in Subcategory: " + response);
        String subcategoryId, subcategoryName, subcategoryParent, subcategoryDes,subcategorySlug,subChild,
                subcategoryImage,subcategoryCount;
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        JSONObject object;

        if (response.length()>0){
            ApplicationData appdata = ApplicationData.getSharedInstance();
            appdata.setResponseCode(HTTPResponseCode.Subcategory);

           // ArrayList<BeanSubCategory> subCategories = appdata.getSubCategories();
            ArrayList<String> sluglist = appdata.getSluglist();
            //Log.e("TEST", "error =-------------" );
            try {
                object = new JSONObject(response);

                if (object.getInt("success")==1){
                    JSONObject jsonObject = object.optJSONObject("data");
                    JSONArray jsonArray = jsonObject.optJSONArray("sub_categories");

                    if (jsonArray.length()==0){
                        dataWrite.showToast(ApplicationContext.getAppContext().getString(R.string.subcategory_not_found));
                       // ApplicationData.getSharedInstance().setResponseCode(HTTPResponseCode.ServerError);
                        /*appdata.showUserAlert(ApplicationContext.getAppContext(), "SubCategory not found",
                                "Back to home", null);*/
                    }else {
                        for (int i =0;i<jsonArray.length();i++){
                            JSONObject object1 = jsonArray.optJSONObject(i);

                            BeanSubCategory bean = new BeanSubCategory();
                            bean.setSubcategory_id(object1.getString("subcategory_id"));
                            bean.setSubcategory_name(object1.getString("subcategory_name"));
                            bean.setDescription(object1.getString("description"));
                            bean.setImage(object1.getString("image"));
                            bean.setCount(object1.getString("count"));
                            bean.setSlug(object1.getString("slug"));
                            bean.setParent(object1.getString("parent"));
                            bean.setChild(object1.getString("child"));

                            subcategoryId = object1.getString("subcategory_id");
                            subcategoryName = object1.getString("subcategory_name");
                            subcategoryDes = object1.getString("description");
                            subcategoryImage = object1.getString("image");
                            subcategoryCount = object1.getString("count");
                            subcategorySlug = object1.getString("slug");
                            subcategoryParent = object1.getString("parent");
                            subChild = object1.getString("child");


                            AppDebugLog.println("bean.getSlug() =-------------" +bean.getSlug());

                            //subCategories.add(bean);
                            sluglist.add(object1.getString("slug"));

                            dataWrite.Insert_Subcategorys(subcategoryId,subcategoryName,subcategoryDes,
                                    subcategoryImage,subcategoryCount,subcategorySlug,subcategoryParent,subChild);

                        }
                    }


                }else{

                    //dataWrite.showToast("Unknown error subcategory");

                }


            } catch (Exception e) {
                // TODO: handle exception

                AppDebugLog.println("error =" + e);
            }


        }else {
            ApplicationData.getSharedInstance().setResponseCode(HTTPResponseCode.ServerError);
        }

    }

    public static void parseProductUpdateResponse(String response) {
        Log.e("Products res:",""+response);
        AppDebugLog.println("Response in Products...... : " + response);
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();

        if (response.length()>0){
            ApplicationData appdata = ApplicationData.getSharedInstance();
            appdata.setResponseCode(HTTPResponseCode.product);
           // Log.e("TEST", "AppConstant.CATEGORYSLUG  =" + AppConstant.CATEGORYSLUG );
            ArrayList<String> responseprodect = appdata.getResponseproduct();
            responseprodect.add(response.toString());

            //dataWrite.Insert_ProductList(AppConstant.CATEGORYSLUG,response.toString());
        }else {
            ApplicationData.getSharedInstance().setResponseCode(HTTPResponseCode.ServerError);
        }

    }

    public static void parseProductDetailResponse(String response) {
        Log.e("Products detail res:",""+response);
        AppDebugLog.println("Response in Products detail : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        appdata.setResponseCode(HTTPResponseCode.Success);
        BeanDetailProduct beanDetailProduct;
        String product_id;
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        dataWrite.Insert_PorductDetail(AppConstant.PRODUCT_ID,response.toString());

      /*  try {
            beanDetailProduct = new BeanDetailProduct();
            Gson gson = new Gson();
            if (response!=null){

                Type type_one = new TypeToken<BeanDetailProduct>() {
                }.getType();
                beanDetailProduct = gson.fromJson(response, type_one);

                if (beanDetailProduct.getSuccess().equals("0")){
                    //Toast.makeText(ApplicationContext.getAppContext(),"error to fetch data",Toast.LENGTH_SHORT).show();
                    dataWrite.showToast("error to fetch data");
                }else {
                    product_id  =beanDetailProduct.getData().getProduct_detail().getId();

                }
            }
        }catch (Exception e){

        }*/


    }

    public static void parseReviewsResponse(String response) {
        Log.e("Reviews res:",""+response);
        AppDebugLog.println("Response in Reviews : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        appdata.setResponseCode(HTTPResponseCode.Reviews);
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        dataWrite.Insert_Reviews(AppConstant.PRODUCT_ID,response.toString());
    }

    public static void parseSaveReviewsResponse(String response) {
        Log.e("Save Reviews res:",""+response);
        AppDebugLog.println("Response in Save reivews : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonObject1 = jsonObject.optJSONObject("data");

            if (jsonObject.getInt("success")==1){

                appdata.setResponseCode(HTTPResponseCode.saveReivews);
                if (jsonObject1.has("msg")){
                    String message = jsonObject1.getString("msg");
                              /*  Toast.makeText(getActivity(), message,
                                        Toast.LENGTH_LONG).show();*/
                    dataWrite.showToast(message);

                }
            }else {
                appdata.setResponseCode(HTTPResponseCode.ServerError);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parseChangePassordResponse(String response) {
        Log.e("Change pass res:",""+response);
        AppDebugLog.println("Response in Save reivews : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        JSONObject object;
        try {
            object = new JSONObject(response);
            JSONObject jsonObject1 =object.optJSONObject("data");

            if (object.getInt("success")==1){
                appdata.setResponseCode(HTTPResponseCode.Success);
                dataWrite.showToast(jsonObject1.getString("msg"));

            }else {
                appdata.setResponseCode(HTTPResponseCode.ServerError);
                dataWrite.showToast(jsonObject1.getString("msg"));

            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public static void parseCountryResponse(String response) {
        Log.e("country list " ,""+ response);
        AppDebugLog.println("country list Country : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        JSONObject object;
        try {
            object = new JSONObject(response);
            JSONObject jsonObject1 =object.optJSONObject("data");

            if (object.getInt("success")==1){
                appdata.setResponseCode(HTTPResponseCode.Country);
                JSONArray array = jsonObject1.optJSONArray("country_list");
                for (int i =0;i<array.length();i++){
                    JSONObject jsonObject = array.optJSONObject(i);

                    String country_code = jsonObject.getString("country_code");
                    String country_name = jsonObject.getString("country_name");
                   // Log.e("TEST", "country_code =" + country_code+" ,,,country_name "+country_name);

                    if (dataWrite.isCountryExist(country_code)){
                        //Log.e("TEST", "country_code =" + country_code+" ,,,country_name "+country_name);
                    }else {
                        dataWrite.Insert_Country(country_code,country_name);
                    }

                }

            }else{
                appdata.setResponseCode(HTTPResponseCode.ServerError);
                //dataWrite.showToast("Unknown error");

            }

        } catch (Exception e) {
            // TODO: handle exception

            AppDebugLog.println("error =" + e);

        }

    }

    public static void parseStatesResponse(String response) {
        Log.e("State res " ,""+ response);
        AppDebugLog.println("Response in State : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        JSONObject object;
        try {
            object = new JSONObject(response);
            JSONObject jsonObject = object.optJSONObject("data");

            if (object.getInt("success")==1){
                appdata.setResponseCode(HTTPResponseCode.State);
                JSONArray array = jsonObject.optJSONArray("state_list");
                for (int i =0;i<array.length();i++){
                    JSONObject jsonObject1 = array.optJSONObject(i);

                    String state_code = jsonObject1.getString("state_code");
                    String state_name = jsonObject1.getString("state_name");

                    if (dataWrite.isStateExist(state_code)){
                        //Log.e("TEST", "state_code =" + state_code+" ,,,state_name "+state_name);
                    }else {
                        dataWrite.Insert_States(AppConstant.COUNTRY_CODE,state_code,state_name);
                    }
                }

            }else{
                appdata.setResponseCode(HTTPResponseCode.ServerError);
                //dataWrite.showToast("Unknown error");
            }

        } catch (Exception e) {
            // TODO: handle exception
            ;
            AppDebugLog.println("error =" + e);
        }
    }

    public static void parseLoginResponse(String response) {
        Log.e("Response in login : ",""+response);
        AppDebugLog.println("Response in Login : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        appdata.setResponseCode(HTTPResponseCode.Success);
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        JSONObject object;
        String userName, user_id,user_fname,user_lname,user_email,bill_address,bill_city,bill_state,bill_country,bill_email,bill_fname,bill_lname,
                bill_phone,bill_postcode,ship_address,ship_city,ship_state,ship_country,ship_fname,ship_lname,ship_postcode,
                bill_company, ship_company,ship_country_code,bill_state_code,bill_country_code,ship_state_code;
        String userID,userEmail,user_name;
        ArrayList<BeanUserData> userDataList = new ArrayList<BeanUserData>();
        SessionManager manager = new SessionManager();
        try {

            object = new JSONObject(response);
            JSONObject object1 = object.optJSONObject("data");
            if (object.getInt("success")==0){

                appdata.setResponseCode(HTTPResponseCode.Success0);
                dataWrite.showToast(object1.getString("loginerror"));
            }else {
                appdata.setResponseCode(HTTPResponseCode.Success);
                JSONObject jsonObject = object1.optJSONObject("userinfo");

                BeanUserData beanUserdata = new BeanUserData();
                userName = jsonObject.getString("display_name");
                user_id = jsonObject.getString("ID");
                user_email=jsonObject.getString("user_email");
                user_fname = jsonObject.getString("user_first_name");
                user_lname = jsonObject.getString("user_last_name");


                beanUserdata.setDisplay_name(userName);
                beanUserdata.setID(user_id);
                beanUserdata.setUser_email(user_email);
                beanUserdata.setUser_first_name(user_fname);
                beanUserdata.setUser_last_name(user_lname);
                beanUserdata.setUser_login(jsonObject.getString("user_login"));
                beanUserdata.setUser_nicename(jsonObject.getString("user_nicename"));


                JSONObject jsonObject1 = object1.optJSONObject("billing_address");
                bill_address = jsonObject1.getString("billing_address_1");
                bill_city = jsonObject1.getString("billing_city");
                bill_country = jsonObject1.getString("billing_country");
                bill_company = jsonObject1.getString("billing_company");
                bill_email = jsonObject1.getString("billing_email");
                bill_fname = jsonObject1.getString("billing_first_name");
                bill_lname =jsonObject1.getString("billing_last_name");
                bill_phone = jsonObject1.getString("billing_phone");
                bill_postcode = jsonObject1.getString("billing_postcode");
                bill_state = jsonObject1.getString("billing_state");
                bill_country_code = jsonObject1.getString("billing_country_code");
                bill_state_code = jsonObject1.getString("billing_state_code");


                beanUserdata.setBilling_address_1(bill_address);
                beanUserdata.setBilling_city(bill_city);
                beanUserdata.setBilling_country(bill_country);
                beanUserdata.setBilling_company(bill_company);
                beanUserdata.setBilling_email(bill_email);
                beanUserdata.setBilling_first_name(bill_fname);
                beanUserdata.setBilling_last_name(bill_lname);
                beanUserdata.setBilling_phone(bill_phone);
                beanUserdata.setBilling_postcode(bill_postcode);
                beanUserdata.setBilling_state(bill_state);
                beanUserdata.setBilling_country_code(bill_country_code);
                beanUserdata.setBilling_state_code(bill_state_code);
               // Log.d("TEST", "state====== "+jsonObject1.getString("billing_state"));


                JSONObject jsonObject2 = object1.optJSONObject("shipping_address");

                ship_address =jsonObject2.getString("shipping_address_1");
                ship_city=jsonObject2.getString("shipping_city");
                ship_state=jsonObject2.getString("shipping_state");
                ship_country=jsonObject2.getString("shipping_country");
                ship_company = jsonObject2.getString("shipping_company");
                ship_fname=jsonObject2.getString("shipping_first_name");
                ship_lname=jsonObject2.getString("shipping_last_name");
                ship_postcode=jsonObject2.getString("shipping_postcode");
                ship_country_code = jsonObject2.getString("shipping_country_code");
                ship_state_code = jsonObject2.getString("shipping_state_code");

                beanUserdata.setShipping_address_1(ship_address);
                beanUserdata.setShipping_city(ship_city);
                beanUserdata.setShipping_company(ship_company);
                beanUserdata.setShipping_country(ship_country);
                beanUserdata.setShipping_state(ship_state);
                beanUserdata.setShipping_first_name(ship_fname);
                beanUserdata.setShipping_last_name(ship_lname);
                beanUserdata.setShipping_postcode(ship_postcode);
                beanUserdata.setShipping_country_code(ship_country_code);
                beanUserdata.setShipping_state_code(ship_state_code);

                userID = beanUserdata.getID();
                userEmail= beanUserdata.getUser_email();
                user_name = beanUserdata.getDisplay_name();

                userDataList.add(beanUserdata);

                manager.setPreferences(ApplicationContext.getAppContext(), AppConstant.STATUS, "1");
                manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.USER_ID,userID);
                manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.USER_EMAIL,userEmail);
                manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.USER_NAME,user_name);
                manager.setPreferencesArraylist(ApplicationContext.getAppContext(), AppConstant.USER_DATA, userDataList);
                userDataList=manager.getPreferencesArraylist(ApplicationContext.getAppContext(),AppConstant.USER_DATA);

                //Log.d("userDataList","share name==== "+ userDataList.get(0).getDisplay_name());

               // Log.d("status", "status is = " + manager.getPreferences(ApplicationContext.getAppContext(),AppConstant.STATUS));
                dataWrite.insertUser(user_fname, user_lname,user_id,user_email,bill_address,bill_city,bill_state,bill_state_code,
                        bill_country,bill_country_code,bill_email,bill_fname,bill_lname, bill_phone,bill_postcode,
                        ship_address,ship_city,ship_state,ship_state_code,ship_country,ship_country_code,
                        ship_fname,ship_lname,ship_postcode,bill_company,ship_company);




            }
        } catch (JSONException e) {
            e.printStackTrace();
            dataWrite.showToast(""+e);
            appdata.setResponseCode(HTTPResponseCode.ServerError);
        }

    }

    public static void parseSettingOptionResponse(String response) {
        Log.e("Response in Setting : ",""+response);
        AppDebugLog.println("Response in Setting : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        SettingOption settingOption;
        SessionManager manager = new SessionManager();
        try {
            settingOption = new SettingOption();
            Gson gson = new Gson();
            if (response!=null){
                Type type_one = new TypeToken<SettingOption>() {
                }.getType();
                settingOption = gson.fromJson(response,type_one);

                if (settingOption.getSuccess().equals("0")){
                    Toast.makeText(ApplicationContext.getAppContext(),"unkwon error",Toast.LENGTH_SHORT).show();
                    appdata.setResponseCode(HTTPResponseCode.ServerError);
                }else {

                    manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.SETTING_OPTION,settingOption);

                    appdata.setResponseCode(HTTPResponseCode.Success);
                }
            }

        } catch (Exception e) {
            // TODO: handle exception

            AppDebugLog.println("error :"+ e);
            appdata.setResponseCode(HTTPResponseCode.ServerError);

        }

    }

    public static void parseRegisterResponse(String response) {

        AppDebugLog.println("Response in Register : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        SettingOption settingOption;
        SessionManager manager = new SessionManager();
        JSONObject object;
        ArrayList<BeanUserData> userDataList = new ArrayList<BeanUserData>();
        String userName, user_id,user_fname,user_lname,user_email,bill_address,bill_city,bill_state,bill_country,bill_email,bill_fname,bill_lname,
                bill_phone,bill_postcode,bill_company,ship_address,ship_city,ship_company,ship_state,ship_country,
                ship_fname,ship_lname,ship_postcode,ship_country_code,bill_state_code,bill_country_code,ship_state_code;
        String userID,userEmail,user_name;
        try {
            object = new JSONObject(response);
            JSONObject jsonObject = object.optJSONObject("data");
            if (object.getInt("success")==0){
                appdata.setResponseCode(HTTPResponseCode.Success0);
                dataWrite.showToast(jsonObject.getString("registererror"));

            }else {
                appdata.setResponseCode(HTTPResponseCode.Success);
                JSONObject jsonObject1 = jsonObject.optJSONObject("userinfo");
                BeanUserData beanUserdata = new BeanUserData();
                userName = jsonObject1.getString("display_name");
                user_id = jsonObject1.getString("ID");
                user_email=jsonObject1.getString("user_email");
                user_lname = jsonObject1.getString("user_last_name");
                user_fname = jsonObject1.getString("user_first_name");

                beanUserdata.setDisplay_name(userName);
                beanUserdata.setID(user_id);
                beanUserdata.setUser_email(user_email);
                beanUserdata.setUser_first_name(user_fname);
                beanUserdata.setUser_last_name(user_lname);
                beanUserdata.setUser_login(jsonObject1.getString("user_login"));
                beanUserdata.setUser_nicename(jsonObject1.getString("user_nicename"));



                JSONObject jsonObject2 = jsonObject.optJSONObject("billing_address");
                bill_address = jsonObject2.getString("billing_address_1");
                bill_city = jsonObject2.getString("billing_city");
                bill_country = jsonObject2.getString("billing_country");
                bill_company = jsonObject2.getString("billing_company");
                bill_email = jsonObject2.getString("billing_email");
                bill_fname = jsonObject2.getString("billing_first_name");
                bill_lname =jsonObject2.getString("billing_last_name");
                bill_phone = jsonObject2.getString("billing_phone");
                bill_postcode = jsonObject2.getString("billing_postcode");
                bill_state = jsonObject2.getString("billing_state");
                bill_country_code = jsonObject2.getString("billing_country_code");
                bill_state_code = jsonObject2.getString("billing_state_code");


                beanUserdata.setBilling_address_1(bill_address);
                beanUserdata.setBilling_city(bill_city);
                beanUserdata.setBilling_country(bill_country);
                beanUserdata.setBilling_company(bill_company);
                beanUserdata.setBilling_email(bill_email);
                beanUserdata.setBilling_first_name(bill_fname);
                beanUserdata.setBilling_last_name(bill_lname);
                beanUserdata.setBilling_phone(bill_phone);
                beanUserdata.setBilling_postcode(bill_postcode);
                beanUserdata.setBilling_state(bill_state);
                beanUserdata.setBilling_country_code(bill_country_code);
                beanUserdata.setShipping_state_code(bill_state_code);
                //Log.d("TEST", "state====== "+jsonObject2.getString("billing_state"));


                JSONObject jsonObject3 = jsonObject.optJSONObject("shipping_address");

                ship_address =jsonObject3.getString("shipping_address_1");
                ship_city=jsonObject3.getString("shipping_city");
                ship_state=jsonObject3.getString("shipping_state");
                ship_company = jsonObject3.getString("shipping_company");
                ship_country=jsonObject3.getString("shipping_country");
                ship_fname=jsonObject3.getString("shipping_first_name");
                ship_lname=jsonObject3.getString("shipping_last_name");
                ship_postcode=jsonObject3.getString("shipping_postcode");
                ship_country_code = jsonObject3.getString("shipping_country_code");
                ship_state_code = jsonObject3.getString("shipping_state_code");

                beanUserdata.setShipping_address_1(ship_address);
                beanUserdata.setShipping_city(ship_city);
                beanUserdata.setShipping_company(ship_company);
                beanUserdata.setShipping_country(ship_country);
                beanUserdata.setShipping_state(ship_state);
                beanUserdata.setShipping_first_name(ship_fname);
                beanUserdata.setShipping_last_name(ship_lname);
                beanUserdata.setShipping_postcode(ship_postcode);
                beanUserdata.setShipping_country_code(ship_country_code);
                beanUserdata.setShipping_state_code(ship_state_code);


                userID = beanUserdata.getID();
                userEmail= beanUserdata.getUser_email();
                user_name = beanUserdata.getDisplay_name();

                userDataList.add(beanUserdata);

                manager.setPreferences(ApplicationContext.getAppContext(), "status", "1");
                manager.setPreferences(ApplicationContext.getAppContext(), AppConstant.USER_ID,userID);
                manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.USER_EMAIL,userEmail);
                manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.USER_NAME,user_name);
                manager.setPreferencesArraylist(ApplicationContext.getAppContext(), AppConstant.USER_DATA, userDataList);
                userDataList=manager.getPreferencesArraylist(ApplicationContext.getAppContext(),AppConstant.USER_DATA);

               // Log.d("userDataList","share name===="+ userDataList.get(0).getDisplay_name());

                dataWrite.deletedData();

                dataWrite.insertUser(user_fname, user_lname, user_id,user_email,bill_address,bill_city,bill_state,bill_state_code,
                        bill_country,bill_country_code,bill_email,bill_fname,bill_lname, bill_phone,bill_postcode,
                        ship_address,ship_city,ship_state,ship_state_code,ship_country,ship_country_code,
                        ship_fname,ship_lname,ship_postcode,bill_company,ship_company);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static void parseSocialResponse(String response) {

        AppDebugLog.println("Response in Social register : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        SettingOption settingOption;
        SessionManager manager = new SessionManager();
        JSONObject object;
        ArrayList<BeanUserData> userDataList = new ArrayList<BeanUserData>();
        String  user_id,user_fname,user_lname,user_email,bill_address,bill_city,bill_state,bill_country,bill_email,bill_fname,bill_lname,
                bill_phone,bill_postcode,bill_company,ship_address,ship_city,ship_company,ship_state,ship_country,
                ship_fname,ship_lname,ship_postcode,ship_country_code,bill_state_code,bill_country_code,ship_state_code;
        String userID,userEmail,user_name;
        try {
            object = new JSONObject(response);
            JSONObject jsonObject = object.optJSONObject("data");
            if (object.getInt("success")==0){
                appdata.setResponseCode(HTTPResponseCode.ServerError);
              //  dataWrite.showToast(jsonObject.getString("registererror"));

            }else  if (object.getInt("success")==3){
                appdata.setResponseCode(HTTPResponseCode.social0);
                //  dataWrite.showToast(jsonObject.getString("registererror"));

            }else {
                appdata.setResponseCode(HTTPResponseCode.socialSuccess);
                JSONObject jsonObject1 = jsonObject.optJSONObject("userinfo");
                BeanUserData beanUserdata = new BeanUserData();
                //userName = jsonObject1.getString("display_name");
                user_id = jsonObject1.getString("ID");
                user_email=jsonObject1.getString("user_email");
                user_lname = jsonObject1.getString("user_last_name");
                user_fname = jsonObject1.getString("user_first_name");

                //beanUserdata.setDisplay_name(userName);
                beanUserdata.setID(user_id);
                beanUserdata.setUser_email(user_email);
                beanUserdata.setUser_first_name(user_fname);
                beanUserdata.setUser_last_name(user_lname);
                beanUserdata.setUser_login(jsonObject1.getString("user_login"));
                beanUserdata.setUser_nicename(jsonObject1.getString("user_nicename"));


                //JSONObject jsonObject2 = jsonObject.optJSONObject("billing_address");
                bill_address = "";
                bill_city = "";
                bill_country = "";
                bill_company = "";
                bill_email = "";
                bill_fname = "";
                bill_lname ="";
                bill_phone = "";
                bill_postcode = "";
                bill_state = "";
                bill_country_code = "";
                bill_state_code = "";


                beanUserdata.setBilling_address_1(bill_address);
                beanUserdata.setBilling_city(bill_city);
                beanUserdata.setBilling_country(bill_country);
                beanUserdata.setBilling_company(bill_company);
                beanUserdata.setBilling_email(bill_email);
                beanUserdata.setBilling_first_name(bill_fname);
                beanUserdata.setBilling_last_name(bill_lname);
                beanUserdata.setBilling_phone(bill_phone);
                beanUserdata.setBilling_postcode(bill_postcode);
                beanUserdata.setBilling_state(bill_state);
                beanUserdata.setBilling_country_code(bill_country_code);
                beanUserdata.setShipping_state_code(bill_state_code);
                //Log.d("TEST", "state====== "+jsonObject2.getString("billing_state"));


                // JSONObject jsonObject3 = jsonObject.optJSONObject("shipping_address");

                ship_address ="";
                ship_city="";
                ship_state="";
                ship_company = "";
                ship_country="";
                ship_fname="";
                ship_lname="";
                ship_postcode="";
                ship_country_code = "";
                ship_state_code = "";

                beanUserdata.setShipping_address_1(ship_address);
                beanUserdata.setShipping_city(ship_city);
                beanUserdata.setShipping_company(ship_company);
                beanUserdata.setShipping_country(ship_country);
                beanUserdata.setShipping_state(ship_state);
                beanUserdata.setShipping_first_name(ship_fname);
                beanUserdata.setShipping_last_name(ship_lname);
                beanUserdata.setShipping_postcode(ship_postcode);
                beanUserdata.setShipping_country_code(ship_country_code);
                beanUserdata.setShipping_state_code(ship_state_code);


                userID = beanUserdata.getID();
                userEmail= beanUserdata.getUser_email();
                user_name = beanUserdata.getDisplay_name();

                userDataList.add(beanUserdata);

                manager.setPreferences(ApplicationContext.getAppContext(), "status", "1");
                manager.setPreferences(ApplicationContext.getAppContext(), AppConstant.USER_ID,userID);
                manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.USER_EMAIL,userEmail);
                manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.USER_NAME,user_name);
                manager.setPreferencesArraylist(ApplicationContext.getAppContext(), AppConstant.USER_DATA, userDataList);
                userDataList=manager.getPreferencesArraylist(ApplicationContext.getAppContext(),AppConstant.USER_DATA);

                // Log.d("userDataList","share name===="+ userDataList.get(0).getDisplay_name());

                dataWrite.deletedData();

                dataWrite.insertUser(user_fname, user_lname, user_id,user_email,bill_address,bill_city,bill_state,bill_state_code,
                        bill_country,bill_country_code,bill_email,bill_fname,bill_lname, bill_phone,bill_postcode,
                        ship_address,ship_city,ship_state,ship_state_code,ship_country,ship_country_code,
                        ship_fname,ship_lname,ship_postcode,bill_company,ship_company);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parseRegisterResponse1(String response) {

        AppDebugLog.println("Response in Register1 : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        SettingOption settingOption;
        SessionManager manager = new SessionManager();
        JSONObject object;
        ArrayList<BeanUserData> userDataList = new ArrayList<BeanUserData>();
        String userName, user_id,user_fname,user_lname,user_email,bill_address,bill_city,bill_state,bill_country,bill_email,bill_fname,bill_lname,
                bill_phone,bill_postcode,bill_company,ship_address,ship_city,ship_company,ship_state,ship_country,
                ship_fname,ship_lname,ship_postcode,ship_country_code,bill_state_code,bill_country_code,ship_state_code;
        String userID,userEmail,user_name;
        try {
            object = new JSONObject(response);
            JSONObject jsonObject = object.optJSONObject("data");
            if (object.getInt("success")==0){
                appdata.setResponseCode(HTTPResponseCode.Success0);
                dataWrite.showToast(jsonObject.getString("registererror"));

            }else {
                appdata.setResponseCode(HTTPResponseCode.Success);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            appdata.setResponseCode(HTTPResponseCode.ServerError);
            dataWrite.showToast(""+e);
        }

    }

    public static void parseForgotPasswordResponse(String response) {
        AppDebugLog.println("Response in Forgot : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();


        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getInt("success")==0){
                JSONObject jsonObject1 = jsonObject.optJSONObject("data");

                dataWrite.showToast(jsonObject1.getString("loginerror"));
                appdata.setResponseCode(HTTPResponseCode.ServerError);
            }else {
                appdata.setResponseCode(HTTPResponseCode.Success);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parseSaveShipResponse(String response) {

        AppDebugLog.println("Response in SaveShip : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        SessionManager manager = new SessionManager();
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        JSONObject object;
        String userName, user_id,user_fname,user_lname,user_email,bill_address,bill_city,bill_state,bill_country,bill_email,bill_fname,bill_lname,
                bill_phone,bill_postcode,bill_company,ship_address,ship_city,ship_company,ship_state,ship_country,
                ship_fname,ship_lname,ship_postcode,ship_country_code,bill_state_code,bill_country_code,ship_state_code;

        user_id = manager.getPreferences(ApplicationContext.getAppContext(),AppConstant.USER_ID);
        try {
            object = new JSONObject(response);
            JSONObject jsonObject = object.optJSONObject("data");

            if (object.getInt("success")==1){
                appdata.setResponseCode(HTTPResponseCode.SaveShip);


                JSONObject jsonObject3 = jsonObject.optJSONObject("shipping_address");

                ship_address =jsonObject3.getString("shipping_address_1");
                ship_city=jsonObject3.getString("shipping_city");
                ship_state=jsonObject3.getString("shipping_state");
                ship_company = jsonObject3.getString("shipping_company");
                ship_country=jsonObject3.getString("shipping_country");
                ship_fname=jsonObject3.getString("shipping_first_name");
                ship_lname=jsonObject3.getString("shipping_last_name");
                ship_postcode=jsonObject3.getString("shipping_postcode");
                ship_country_code = jsonObject3.getString("shipping_country_code");
                ship_state_code = jsonObject3.getString("shipping_state_code");



                dataWrite.insertShipping(user_id,ship_fname,ship_lname,ship_address,ship_country,ship_state,
                        ship_city,ship_postcode,ship_country_code,ship_state_code, ship_company);

            }else{
                appdata.setResponseCode(HTTPResponseCode.ServerError);
                //dataWrite.showToast( "Unknown error");
            }

        } catch (Exception e) {
            // TODO: handle exception

            AppDebugLog.println("error :"+e);
        }

    }

    public static void parseSaveBillResponse(String response) {
        AppDebugLog.println("Response in SaveBill : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        SessionManager manager = new SessionManager();

        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        String userName, user_id,user_fname,user_lname,user_email,bill_address,bill_city,bill_state,bill_country,bill_email,bill_fname,bill_lname,
                bill_phone,bill_postcode,bill_company,ship_address,ship_city,ship_company,ship_state,ship_country,
                ship_fname,ship_lname,ship_postcode,ship_country_code,bill_state_code,bill_country_code,ship_state_code;
        user_id = manager.getPreferences(ApplicationContext.getAppContext(),AppConstant.USER_ID);
        JSONObject object;
        try {
            object = new JSONObject(response);
            JSONObject jsonObject = object.optJSONObject("data");

            if (object.getInt("success")==1){
                appdata.setResponseCode(HTTPResponseCode.SaveBill);
                JSONObject jsonObject2 = jsonObject.optJSONObject("billing_address");
                bill_address = jsonObject2.getString("billing_address_1");
                bill_city = jsonObject2.getString("billing_city");
                bill_country = jsonObject2.getString("billing_country");
                bill_company = jsonObject2.getString("billing_company");
                bill_email = jsonObject2.getString("billing_email");
                bill_fname = jsonObject2.getString("billing_first_name");
                bill_lname =jsonObject2.getString("billing_last_name");
                bill_phone = jsonObject2.getString("billing_phone");
                bill_postcode = jsonObject2.getString("billing_postcode");
                bill_state = jsonObject2.getString("billing_state");
                bill_country_code = jsonObject2.getString("billing_country_code");
                bill_state_code = jsonObject2.getString("billing_state_code");


                dataWrite.insertBilling(user_id,bill_fname,bill_lname,bill_address,bill_country,bill_state,bill_city,bill_postcode,bill_email,bill_phone,bill_country_code,bill_state_code, bill_company);

                AppDebugLog.println("bill_country_code.. =" + bill_country_code+"\n"+"bill_state_code.. =" + bill_state_code);
            }else{

                //dataWrite.showToast( "Unknown error");
                appdata.setResponseCode(HTTPResponseCode.ServerError);
            }


        } catch (Exception e) {
            // TODO: handle exception

            AppDebugLog.println("error =" + e);
        }
    }

    public static void parseSaveResponse(String response) {
        AppDebugLog.println("Response in Save : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        SettingOption settingOption;
        SessionManager manager = new SessionManager();
        JSONObject object;
        ArrayList<BeanUserData> userDataList = new ArrayList<BeanUserData>();
        String userName, user_id,user_fname,user_lname,user_email,bill_address,bill_city,bill_state,bill_country,bill_email,bill_fname,bill_lname,
                bill_phone,bill_postcode,bill_company,ship_address,ship_city,ship_company,ship_state,ship_country,
                ship_fname,ship_lname,ship_postcode,ship_country_code,bill_state_code,bill_country_code,ship_state_code;

        try {
            object = new JSONObject(response);
            JSONObject jsonObject = object.optJSONObject("data");

            if (object.getInt("success")==0){
                appdata.setResponseCode(HTTPResponseCode.ServerError);
                dataWrite.showToast("Unknown error");
            }else {
                appdata.setResponseCode(HTTPResponseCode.Save);


                JSONObject jsonObject1 = jsonObject.optJSONObject("user_data");
                BeanUserData beanUserdata = new BeanUserData();
                userName = jsonObject1.getString("display_name");
                user_id = jsonObject1.getString("ID");
                user_email=jsonObject1.getString("user_email");
                user_lname = jsonObject1.getString("user_last_name");
                user_fname = jsonObject1.getString("user_first_name");

                beanUserdata.setDisplay_name(userName);
                beanUserdata.setID(user_id);
                beanUserdata.setUser_email(user_email);
                beanUserdata.setUser_first_name(user_fname);
                beanUserdata.setUser_last_name(user_lname);



                JSONObject jsonObject2 = jsonObject.optJSONObject("billing_address");
                bill_address = jsonObject2.getString("billing_address_1");
                bill_city = jsonObject2.getString("billing_city");
                bill_country = jsonObject2.getString("billing_country");
                bill_company = jsonObject2.getString("billing_company");
                bill_email = jsonObject2.getString("billing_email");
                bill_fname = jsonObject2.getString("billing_first_name");
                bill_lname =jsonObject2.getString("billing_last_name");
                bill_phone = jsonObject2.getString("billing_phone");
                bill_postcode = jsonObject2.getString("billing_postcode");
                bill_state = jsonObject2.getString("billing_state");
                bill_country_code = jsonObject2.getString("billing_country_code");
                bill_state_code = jsonObject2.getString("billing_state_code");


                JSONObject jsonObject3 = jsonObject.optJSONObject("shipping_address");

                ship_address =jsonObject3.getString("shipping_address_1");
                ship_city=jsonObject3.getString("shipping_city");
                ship_state=jsonObject3.getString("shipping_state");
                ship_company = jsonObject3.getString("shipping_company");
                ship_country=jsonObject3.getString("shipping_country");
                ship_fname=jsonObject3.getString("shipping_first_name");
                ship_lname=jsonObject3.getString("shipping_last_name");
                ship_postcode=jsonObject3.getString("shipping_postcode");
                ship_country_code = jsonObject3.getString("shipping_country_code");
                ship_state_code = jsonObject3.getString("shipping_state_code");



                dataWrite.insertUser(user_fname, user_lname, user_id,user_email,bill_address,bill_city,bill_state,bill_state_code,
                        bill_country,bill_country_code,bill_email,bill_fname,bill_lname, bill_phone,bill_postcode,
                        ship_address,ship_city,ship_state,ship_state_code,ship_country,ship_country_code,
                        ship_fname,ship_lname,ship_postcode,bill_company,ship_company);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parseOrderDetailResponse(String response) {
        AppDebugLog.println("Response in Order Detail : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        BeanOrderDetail  bean_data = new BeanOrderDetail();
        try {


            Gson gson = new Gson();
            if (response != null) {
                Type type_one = new TypeToken<BeanOrderDetail>() {
                }.getType();
                bean_data = gson.fromJson(response, type_one);

                //     Log.d("TEST", "orders count is " + bean_data.getCount());
                if (bean_data==null) {
                    // Toast.makeText(ApplicationContext.getAppContext(),"Order not found",Toast.LENGTH_SHORT).show();
                    dataWrite.showToast(ApplicationContext.getAppContext().getString(R.string.order_not_found));
                    appdata.setResponseCode(HTTPResponseCode.ServerError);


                } else {
                    String ordID = bean_data.getData().getOrders_detail().getOrder_detail().getOrder_id();
                   dataWrite.Insert_OrderDetail(ordID,response);
                    appdata.setResponseCode(HTTPResponseCode.Success);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            AppDebugLog.println("data........===" + e);

        }


    }

    public static void parseOrdersResponse(String response) {
        AppDebugLog.println("Response in Orders : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        appdata.setResponseCode(HTTPResponseCode.Success);
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        SessionManager manager = new SessionManager();
       String user_id = manager.getPreferences(ApplicationContext.getAppContext(),AppConstant.USER_ID);

        dataWrite.Insert_Orders(user_id,response);


    }

    public static void parseSearchResponse(String response) {
        AppDebugLog.println("Response in Search : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        AppConstant.SEARCH_LIST = response;
        appdata.setResponseCode(HTTPResponseCode.Success);


    }

    public static void parseMethodResponse(String response) {
        AppDebugLog.println("Response in Method : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        BeanPaymentInfo paymentInfo = new BeanPaymentInfo();
        String method_id,id,title,tax_status,cost;
        dataWrite.Insert_Method(response);


        appdata.setResponseCode(HTTPResponseCode.Method);


  /*      try {
            Gson gson = new Gson();
            if (response!=null){
                Type type_one = new TypeToken<BeanPaymentInfo>() {
                }.getType();
                paymentInfo = gson.fromJson(response,type_one);
                            *//*AppConstant.payment_gateways = paymentInfo.getData().getPayment_gateways();*//*
                if (paymentInfo.getSuccess().equals("0")){
                    //Toast.makeText(ApplicationContext.getAppContext(),"unkwon error",Toast.LENGTH_SHORT).show();
                    dataWrite.showToast("unkwon error");

                }else {
                    for (int i=0;i<paymentInfo.getData().getShipping_method().size();i++){
                        method_id = paymentInfo.getData().getShipping_method().get(i).getMethod_id();
                        id = paymentInfo.getData().getShipping_method().get(i).getId();
                        title = paymentInfo.getData().getShipping_method().get(i).getTitle();
                        tax_status = paymentInfo.getData().getShipping_method().get(i).getTax_status();
                        cost = paymentInfo.getData().getShipping_method().get(i).getCost();


                        dataWrite.Insert_ShipMethod(method_id,id,title,tax_status,cost);
                    }


                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.d("TEST", "error =" + e);

        }*/

    }

    public static void parseSaveOrderResponse(String response) {
        AppDebugLog.println("Response in SaveOrder : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();

        try {
            JSONObject object = new JSONObject(response);

            if (object.getInt("success")==0){
                appdata.setResponseCode(HTTPResponseCode.ServerError);

                dataWrite.showToast(ApplicationContext.getAppContext().getString(R.string.data_was_empty));

            }else {

                appdata.setResponseCode(HTTPResponseCode.saveOrder);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public static void parseApplyCouponResponse(String response) {
        AppDebugLog.println("Response in ApplyCoupon : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();

        if (response.length()>0){

            appdata.setResponseCode(HTTPResponseCode.Applycode);
            AppConstant.ApplyCoupon = response;
        }else {
            appdata.setResponseCode(HTTPResponseCode.ServerError);
        }



    }

    public static void parseRemoveCouponResponse(String response) {
        AppDebugLog.println("Response in RemoveCoupon : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();

        try {
            JSONObject  object = new JSONObject(response);
            if (object.getInt("success")==1) {
                dataWrite.showToast(object.getString("msg"));
                appdata.setResponseCode(HTTPResponseCode.CancelCode);


            }else {
                appdata.setResponseCode(HTTPResponseCode.ServerError);
                dataWrite.showToast(object.getString("msg"));

            }

        }catch (JSONException e){
            appdata.setResponseCode(HTTPResponseCode.ServerError);
        }
    }

    public static void parseNotification(String responseContent) {
        AppDebugLog.println("Response in Notification : " + responseContent);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        appdata.setResponseCode(HTTPResponseCode.NotiSuccess);
    }


    public static void parseHashKeyResponse(String responseContent) {

        AppDebugLog.println("Response in HashKey register : " + responseContent);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        SessionManager manager = new SessionManager();
        try {
            JSONObject object = new JSONObject(responseContent);


            manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.hashKey,responseContent);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        appdata.setResponseCode(HTTPResponseCode.hash_success);

    }


    public static void parsePaytmChecksumKeyResponse(String responseContent) {

        AppDebugLog.println("Response in Checksum Key register : " + responseContent);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        SessionManager manager = new SessionManager();
        try {
            JSONObject object = new JSONObject(responseContent);


            manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.checkSumKey,responseContent);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        appdata.setResponseCode(HTTPResponseCode.checksum_success);

    }

 /*   public static void parsePaytmVerifyResponse(String responseContent) {

        AppDebugLog.println("Response in paytm verify : " + responseContent);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        appdata.setResponseCode(HTTPResponseCode.Success);

    }*/

    public static void parsePaytmChecksumKeyVerifyResponse(String responseContent) {

        AppDebugLog.println("Response in Checksum Key Verify=  : " + responseContent);
        ApplicationData appdata = ApplicationData.getSharedInstance();

        SessionManager manager = new SessionManager();
        try {
            JSONObject object = new JSONObject(responseContent);


            manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.checkSumKey,responseContent);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        appdata.setResponseCode(HTTPResponseCode.paytmVerify);

    }

    public static void parsePaytmResponse(String response) {

        AppDebugLog.println("Response in PaytmResponse : " + response);
        ApplicationData appdata = ApplicationData.getSharedInstance();
        DataWrite dataWrite = new DataWrite(ApplicationContext.getAppContext());
        dataWrite.open();
        SessionManager manager = new SessionManager();

        try {
            JSONObject object = new JSONObject(response);

            if (object.getInt("success")==0){
                appdata.setResponseCode(HTTPResponseCode.ServerError);

                dataWrite.showToast(ApplicationContext.getAppContext().getString(R.string.data_was_empty));

            }else {

                //  String s= object.getString("data");

          /*      JSONObject jsonObject = object.optJSONObject("data");
                String s = "";
                if (jsonObject.has("order_id")){
                    s = jsonObject.getString("order_id");
                }

                manager.setPreferences(ApplicationContext.getAppContext(),AppConstant.ORDER_ID,s);
*/
                appdata.setResponseCode(HTTPResponseCode.paytmResponce);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

}
