package com.lujayn.latiwala.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lujayn.latiwala.R;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity implements RequestTaskDelegate, View.OnClickListener {
    ImageView iv_edit_billing, iv_edit_shipping;
    ImageView iv_editphoto, iv_editIcon;
    EditText et_fname, et_lname, et_email;
    TextView tv_billingaddress, tv_shippingaddress;
    TextView btn_save;
    DataWrite dataWrite;
    SessionManager manager;
    CharSequence chrImage[] = {"camera", "gallery"};
    String img_string;
    int PicID;
    File dir;
    ArrayList<BeanUserData> userDataList;
    private ProgressDialog progressDialog;
    String userName, user_id, user_fname, user_lname, user_email, bill_address, bill_city, bill_state, bill_country,
            bill_email, bill_fname, bill_lname, bill_phone, bill_postcode, ship_address, ship_city,
            ship_state, ship_country, ship_fname, ship_lname, ship_postcode, bill_company, ship_company,
            ship_country_code, bill_state_code, bill_country_code, ship_state_code;

    String color_back, color_font, color_statusbar;
    private ApplicationData appData;
    Activity activity;
    LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        activity = this;
        manager = new SessionManager();
        appData = ApplicationData.getSharedInstance();
        SettingOption settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(activity, AppConstant.SETTING_OPTION);
        color_back = settingOption.getData().getOptions().getCate_shape_color();
        color_font = settingOption.getData().getOptions().getCate_name_color();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        //getPermissionToGallery();
        dataWrite = new DataWrite(activity);
        dataWrite.open();

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        iv_editphoto = (ImageView) findViewById(R.id.ivEditProfilePic);
        iv_editIcon = (ImageView) findViewById(R.id.ivEditProfile_edit);
        iv_edit_billing = (ImageView) findViewById(R.id.ivEditBilling_editprofile);
        iv_edit_shipping = (ImageView) findViewById(R.id.ivEditShipping_editprofile);

        et_fname = (EditText) findViewById(R.id.etFirstname_editprofile);
        et_lname = (EditText) findViewById(R.id.etLastname_editprofile);
        et_email = (EditText) findViewById(R.id.etEmail_editprofile);

        tv_billingaddress = (TextView) findViewById(R.id.tvBilling_profile);
        tv_shippingaddress = (TextView) findViewById(R.id.tvshipping_profile);

        btn_save = (TextView) findViewById(R.id.btnSaveProfile);
        btn_save.setBackgroundColor(Color.parseColor(color_back));
        btn_save.setTextColor(Color.parseColor(color_font));
        RelativeLayout rl_header = (RelativeLayout) findViewById(R.id.rl_header);
        rl_header.setBackgroundColor(Color.parseColor(color_back));
        color_statusbar = settingOption.getData().getOptions().getStatus_bar_color();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color_statusbar));
        }

        userDataList = dataWrite.FetchUserData();

        btn_save.setOnClickListener(onClickMethod);
        iv_edit_billing.setOnClickListener(onClickMethod);
        iv_edit_shipping.setOnClickListener(onClickMethod);
        iv_editIcon.setOnClickListener(onClickMethod);
        ll_back.setOnClickListener(this);


        user_id = userDataList.get(0).getID();
        userName = userDataList.get(0).getDisplay_name();
        user_lname = userDataList.get(0).getUser_last_name();
        user_fname = userDataList.get(0).getUser_first_name();
        bill_address = userDataList.get(0).getBilling_address_1();
        bill_fname = userDataList.get(0).getBilling_first_name();
        bill_city = userDataList.get(0).getBilling_city();
        bill_company = userDataList.get(0).getBilling_company();
        bill_country = userDataList.get(0).getBilling_country();
        bill_lname = userDataList.get(0).getBilling_last_name();
        bill_email = userDataList.get(0).getBilling_email();
        bill_phone = userDataList.get(0).getBilling_phone();
        bill_state = userDataList.get(0).getBilling_state();
        bill_postcode = userDataList.get(0).getBilling_postcode();
        bill_country_code = userDataList.get(0).getBilling_country_code();
        bill_state_code = userDataList.get(0).getBilling_state_code();


        ship_fname = userDataList.get(0).getShipping_first_name();
        ship_lname = userDataList.get(0).getShipping_last_name();
        ship_address = userDataList.get(0).getShipping_address_1();
        ship_company = userDataList.get(0).getShipping_company();
        ship_city = userDataList.get(0).getShipping_city();
        ship_state = userDataList.get(0).getShipping_state();
        ship_country = userDataList.get(0).getShipping_country();
        ship_postcode = userDataList.get(0).getShipping_postcode();
        ship_country_code = userDataList.get(0).getShipping_country_code();
        ship_state_code = userDataList.get(0).getShipping_state_code();


        String billingaddress = bill_fname + " " + bill_lname + "\n"
                + bill_address + "\n" + bill_company + "\n" + bill_city +
                " " + bill_state +
                " " + bill_country +
                " " + bill_postcode +
                " " + bill_email +
                " " + bill_phone;

        String shippingaddress = ship_fname + " " + ship_lname + "\n"
                + ship_address + "\n" + ship_company + "\n" + ship_city +
                " " + ship_state +
                " " + ship_country +
                " " + ship_postcode;

        et_fname.setText(userDataList.get(0).getDisplay_name());
        et_lname.setText(user_lname);
        et_email.setText(userDataList.get(0).getUser_email());

        if (billingaddress.trim().length() == 0) {
            tv_billingaddress.setText(R.string.please_add_your_billing_address);

        } else {
            tv_billingaddress.setText(billingaddress);
        }

        if (shippingaddress.trim().length() == 0) {
            tv_shippingaddress.setText(R.string.please_add_your_shipping_address1);

        } else {
            tv_shippingaddress.setText(shippingaddress);

        }
    }

    private View.OnClickListener onClickMethod = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment fragment = null;
            String title = null;
            String tag = null;
            switch (view.getId()) {


                case R.id.btnSaveProfile:

                    saveprofile("save");
                    break;

                case R.id.ivEditBilling_editprofile:

                    Intent i = new Intent(activity, AddAddressActivity.class);
                    i.putExtra("tag", "billing");
                    startActivity(i);
                    finish();

                    break;

                case R.id.ivEditShipping_editprofile:
                    Intent j = new Intent(activity, AddAddressActivity.class);
                    j.putExtra("tag", "shipping");
                    startActivity(j);
                    finish();
                    break;

                case R.id.ivEditProfile_edit:

                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            activity);
                    builder.setSingleChoiceItems(chrImage, -1,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    String s = chrImage[which].toString();

                                    if (s.equalsIgnoreCase("camera")) {
                                        // genderBolean = 1;
                                        PicID = 1;
                                        //getPermissionToCamera();


                                    } else if (s.equalsIgnoreCase("gallery")) {
                                        // genderBolean = 0;
                                        PicID = 2;
                                        //gallerypicture();
                                    }
                                    dialog.cancel();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    break;
                default:
                    break;

            }

   /*         if (fragment!=null){
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("tag_address",tag);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();
                ((PayUmoneyActivity) activity).Title(title);

            }*/

        }
    };


/*    protected void gallerypicture() {

        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image*//*");

        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, GALLERY_OPEN_CODE);

    }*/

 /*   protected void takePicture() {
        // TODO Auto-generated method stub
        dir = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"wooPicture");
        if (!dir.exists())
        {
            if (!dir.mkdirs()) {
                Log.d("PayUmoneyActivity", "failed to create Firebase Storage directory");
            }
        }

        File file_dir = new File(Environment.getExternalStorageDirectory().toString());
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		*//* create instance of File with name img.jpg *//*
        File file = new File(file_dir, String.format("profile.jpg"));
		*//* put uri as extra in intent object *//*
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		*//* start activity for result pass intent as argument and request code *//*

        startActivityForResult(i, CAMERA_OPEN_CODE);
    }*/



/*    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
*//*        Log.d("Gallery","gallery resultCode ==== "+requestCode);
        Log.d("Gallery","gallery resultCode ==== "+resultCode);
        Log.d("Gallery","gallery data ==== "+data);*//*
        if (PicID == 1) {
            if (requestCode == CAMERA_OPEN_CODE&&resultCode==-1) {
                // create instance of File with same name we created before to
                // get image from storage
                File file = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : file.listFiles()) {
                    if (temp.getName().equals("profile.jpg")) {
                        file = temp;
                        break;
                    }
                }

                // Crop the captured image using an other intent
                try {
					*//* the user's device may not support cropping *//*
                    cropCapturedImage(Uri.fromFile(file));
                } catch (ActivityNotFoundException aNFE) {
                    // display an error message if user device doesn't support
                    String errorMessage = "Sorry - your device doesn't support the crop action!";

                    dataWrite.showToast(errorMessage);
                }
            }

            if (requestCode == 2 && resultCode == -1) {
                // Create an instance of bundle and get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap from extras
                Bitmap thePic = extras.getParcelable("data");
                // set image bitmap to image view
                //iv_editphoto.setImageBitmap(thePic);
                File file = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : file.listFiles()) {
                    if (temp.getName().equals("profile.jpg")) {
                        file = temp;
                        break;
                    }
                }
                file.delete();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thePic.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                File file1 = new File(dir, String.format(String.valueOf(System.currentTimeMillis()) + ".jpg"));

                FileOutputStream fo;
                try {
                    file1.createNewFile();
                    fo = new FileOutputStream(file1);
                    fo.write(stream.toByteArray());
                    byte[] image = stream.toByteArray();
                    img_string = Base64.encodeToString(image, Base64.DEFAULT);
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("Gallery","gallery bitmap ==== "+img_string);
            }
        } else {
            if (requestCode == GALLERY_OPEN_CODE && resultCode == activity.RESULT_OK && null != data) {

                Uri selectedImageUri = data.getData();
                File f = new File(selectedImageUri.getPath());
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImageUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
               // Bitmap bmp = BitmapFactory.decodeFile(f.getPath());
                //iv_editphoto.setImageBitmap(bitmap);
                String selectedPath = getPath(selectedImageUri);
               // iv_editphoto.setImageURI(selectedImageUri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                options.inPurgeable = true;


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image = stream.toByteArray();

                img_string = Base64.encodeToString(image, Base64.DEFAULT);

                Log.d("Gallery","gallery bitmap ==== "+img_string);
				*//*
     * Uri selectedImage = data.getData();
     *
     * String[] filePathColumn = { MediaStore.Images.Media.DATA };
     *
     * Cursor cursor = getContentResolver().query(selectedImage,
     * filePathColumn, null, null, null); cursor.moveToFirst();
     *
     * int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
     * String picturePath = cursor.getString(columnIndex);
     * cursor.close();
     *
     * BitmapFactory.Options options = new BitmapFactory.Options();
     *
     * options.inSampleSize = 2; options.inPurgeable=true;
     * bmp=BitmapFactory.decodeFile(picturePath,options );
     *
     * imageview.setImageBitmap(BitmapFactory.decodeFile(picturePath)
     * );
     *//*
            }
        }

    }*/


    private void saveprofile(String save) {
        progressDialog.show();
        user_lname = et_lname.getText().toString();
        user_email = et_email.getText().toString();
        user_fname = et_fname.getText().toString();
        manager.setPreferences(activity, AppConstant.USER_EMAIL, user_email);
        manager.setPreferences(activity, AppConstant.USER_NAME, userName);

        AppDebugLog.println("country_code===== " + ship_country_code);
        AppDebugLog.println("state_code===== " + ship_state_code);
        AppDebugLog.println("country_code===== " + bill_country_code);
        AppDebugLog.println("state_code===== " + bill_state_code);

        dataWrite.insertUser(user_fname, user_lname, user_id, user_email, bill_address, bill_city, bill_state, bill_state_code, bill_country, bill_country_code, bill_email, bill_fname, bill_lname,
                bill_phone, bill_postcode, ship_address, ship_city, ship_state, ship_state_code, ship_country, ship_country_code,
                ship_fname, ship_lname, ship_postcode, bill_company, ship_company);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.putOpt("user_id", userDataList.get(0).getID().toString());
            jsonObject.putOpt("display_name", et_fname.getText().toString());
            jsonObject.putOpt("first_name", et_fname.getText().toString());
            jsonObject.putOpt("last_name", et_lname.getText().toString());
            jsonObject.putOpt("email", et_email.getText().toString());
            //jsonObject.putOpt("image",img_string);


            //  JSONObject jsonObject1 = new JSONObject();
            jsonObject.putOpt("billing_first_name", bill_fname);
            jsonObject.putOpt("billing_last_name", bill_lname);
            jsonObject.putOpt("billing_address_1", bill_address);
            jsonObject.putOpt("billing_company", bill_company);
            jsonObject.putOpt("billing_city", bill_city);
            jsonObject.putOpt("billing_state", bill_state);
            jsonObject.putOpt("billing_country", bill_country);
            jsonObject.putOpt("billing_email", bill_email);
            jsonObject.putOpt("billing_postcode", bill_postcode);
            jsonObject.putOpt("billing_phone", bill_phone);
            jsonObject.putOpt("billing_country_code", bill_country_code);
            jsonObject.putOpt("billing_state_code", bill_state_code);

            //  jsonObject.put("billing_address",jsonObject1);

            //   JSONObject jsonObject2 = new JSONObject();
            jsonObject.putOpt("shipping_first_name", ship_fname);
            jsonObject.putOpt("shipping_last_name", ship_lname);
            jsonObject.putOpt("shipping_address_1", ship_address);
            jsonObject.putOpt("shipping_company", ship_company);
            jsonObject.putOpt("shipping_city", ship_city);
            jsonObject.putOpt("shipping_state", ship_state);
            jsonObject.putOpt("shipping_country", ship_country);
            jsonObject.putOpt("shipping_postcode", ship_postcode);
            jsonObject.putOpt("shipping_country_code", ship_country_code);
            jsonObject.putOpt("shipping_state_code", ship_state_code);

            //   jsonObject.put("shipping_address",jsonObject2);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_EDIT_PROFILE;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, save, jsonObject.toString());

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // activity.registerReceiver(receive, new IntentFilter(Intent_Class_Data.NOTIFICATION));

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // activity.unregisterReceiver(receive);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("Login responseCode : " + responseCode);

        switch (responseCode) {
            case Save:
                progressDialog.dismiss();
                startActivity(new Intent(activity, MainActivity.class));
                finish();
                break;
            case ServerError:
                appData.showUserAlert(activity, getString(R.string.alert_title_message),
                        getString(R.string.alert_body_server_error), null);
                progressDialog.dismiss();
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
        }
    }
}
