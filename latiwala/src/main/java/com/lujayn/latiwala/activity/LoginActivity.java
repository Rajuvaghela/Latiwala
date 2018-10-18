package com.lujayn.latiwala.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.lujayn.latiwala.R;
import com.lujayn.latiwala.Utility;
import com.lujayn.latiwala.bean.SettingOption;
import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.ApplicationContext;
import com.lujayn.latiwala.common.ApplicationData;

import com.lujayn.latiwala.common.SessionManager;
import com.lujayn.latiwala.common.SharedPrefManager;
import com.lujayn.latiwala.common.Webservice;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.database.DataWrite;
import com.lujayn.latiwala.network.RequestTask;
import com.lujayn.latiwala.network.RequestTaskDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dmax.dialog.SpotsDialog;

import static com.lujayn.latiwala.Utility.checkConnectivity;

/**
 * Created by Shailesh on 25/07/16.
 */
public class LoginActivity extends AppCompatActivity implements RequestTaskDelegate, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private Boolean exit = false;
    EditText et_email, et_password;
    TextView btn_login;
    TextView tv_forgotpass, tv_signup, tv_guest;
    SessionManager manager;
    DataWrite dataWrite;
    String userName, user_id;

    String color_back, color_font;

    private ProgressDialog progressDialog;
    private ApplicationData appData;
    SettingOption settingOption;
    String fname, lname, address, country, state, city, postcode, email, phone, company, password, username;


    /*facebook login*/
    CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    /*google login*/
    private GoogleApiClient mGoogleApiClient;
    private SignInButton btnSignIn;
    private static final int RC_SIGN_IN = 007;
    LinearLayout ll_back;
    Dialog forgotpassword;
    Activity activity;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        activity = this;
        pd = Utility.getDialog(activity);
        manager = new SessionManager();
        appData = ApplicationData.getSharedInstance();
        settingOption = new SettingOption();
        settingOption = manager.getPreferencesOption(LoginActivity.this, AppConstant.SETTING_OPTION);
        color_back = settingOption.getData().getOptions().getCate_shape_color();
        color_font = settingOption.getData().getOptions().getCate_name_color();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color_back));
        }

        dataWrite = new DataWrite(this);
        dataWrite.open();
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();


        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };
        loginButton.setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY);
        loginButton.setReadPermissions("public_profile", "user_friends", "email");
        loginButton.registerCallback(callbackManager, callback);

        initialization();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void nextActivity(Profile profile) {
        if (profile != null) {
            Log.e("FB", "first name= " + profile.getFirstName());
            Log.e("FB", "last name= " + profile.getLastName());
            Log.e("FB", "getId= " + profile.getId());
            Log.e("FB", "getMiddleName " + profile.getMiddleName());
            Log.e("FB", "name= " + profile.getName());
            Log.e("FB", "first name= " + profile.getLinkUri());
            Log.e("FB", "first name= " + profile.getCurrentProfile());

            fname = profile.getFirstName();
            lname = profile.getLastName();
            user_id = profile.getId();
            email = user_id + "@facebook.com";
            socialRegister(fname, lname, email, user_id);
        }
    }

    public boolean isInputLangEnable() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> ims = imm.getEnabledInputMethodList();
        for (InputMethodInfo method : ims) {
            List<InputMethodSubtype> submethods = imm.getEnabledInputMethodSubtypeList(method, true);
            for (InputMethodSubtype submethod : submethods) {
                if (submethod.getMode().equals("keyboard")) {
                    String currentLocale = submethod.getLocale();
                    Log.i("TAG", "Available input method locale: " + currentLocale);
                    if (currentLocale.equals("hi")) {

                        return true;
                    }

                }
            }
        }

        return false;
    }

    private void initialization() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        dataWrite.deletedData();
        et_email = (EditText) findViewById(R.id.etEmail_login);
        et_password = (EditText) findViewById(R.id.etPassword_login);
        btn_login = (TextView) findViewById(R.id.btnLogin);
        tv_forgotpass = (TextView) findViewById(R.id.tvForgotPassword);
        tv_signup = (TextView) findViewById(R.id.tvSignup);
        tv_guest = (TextView) findViewById(R.id.tvCheckoutAsGuest);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);

        btn_login.setBackgroundColor(Color.parseColor(color_back));
        btn_login.setTextColor(Color.parseColor(color_font));
        tv_signup.setBackgroundColor(Color.parseColor(color_back));
        tv_signup.setTextColor(Color.parseColor(color_font));

        RelativeLayout rl_header = (RelativeLayout) findViewById(R.id.rl_header);
        rl_header.setBackgroundColor(Color.parseColor(color_back));

        if (settingOption.getData().getOptions().getGuest_checkout().equals("yes")) {
            tv_guest.setVisibility(View.VISIBLE);
        } else {
            tv_guest.setVisibility(View.GONE);
        }
        Intent i = getIntent();
        if (i.hasExtra("SHOP")) {
            AppConstant.key = i.getStringExtra("SHOP");

        }

        if (i.hasExtra("LOGIN")) {
            AppConstant.key = i.getStringExtra("LOGIN");
            tv_guest.setVisibility(View.GONE);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btn_login.setOnClickListener(onClickMethod);
        tv_forgotpass.setOnClickListener(onClickMethod);
        tv_signup.setOnClickListener(onClickMethod);
        tv_guest.setOnClickListener(onClickMethod);
        btnSignIn.setOnClickListener(onClickMethod);

        ll_back.setOnClickListener(this);

    }

    private View.OnClickListener onClickMethod = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.btn_sign_in:

                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                    break;

                case R.id.btnLogin:

                    if (et_email.getText().length() == 0) {
                        et_email.setError(getString(R.string.please_enter_your_username));
                    } else if (et_password.getText().length() == 0) {

                        et_password.setError(getString(R.string.please_enter_password));
                    } else {
                        String status = manager.getPreferences(LoginActivity.this, "status");

                        AppDebugLog.println("status= " + status);

                        Login_webservice("login");

                    }
                    break;

                case R.id.tvForgotPassword:

                    if (AppConstant.key.equals("shopping")) {
                  /*      Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                        intent1.putExtra("SHOP", AppConstant.key);
                        startActivity(intent1);
                        finish();*/
                        forgotpassword();

                    } else {
                  /*      Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                        intent1.putExtra("LOGIN", AppConstant.key);
                        startActivity(intent1);
                        finish();*/
                        forgotpassword();
                    }
                    break;

                case R.id.tvSignup:

                    if (AppConstant.key.equals("shopping")) {
                        Intent intent1 = new Intent(LoginActivity.this, CreateAccountActivity.class);
                        intent1.putExtra("SHOP", AppConstant.key);
                        startActivity(intent1);
                        finish();

                    } else {
                        Intent intent1 = new Intent(LoginActivity.this, CreateAccountActivity.class);
                        intent1.putExtra("LOGIN", AppConstant.key);
                        startActivity(intent1);
                        finish();
                    }
                 /*   Intent intent1 = new Intent(LoginActivity.this,CreateAccountActivity.class);
                    intent1.putExtra("LOGIN",AppConstant.key);
                    startActivity(intent1);
                    finish();*/
                    break;

                case R.id.tvCheckoutAsGuest:

                    Intent i = new Intent(LoginActivity.this, CreateAccountActivity.class);
                    i.putExtra("GUEST", "guest");
                    startActivity(i);
                    finish();
                    break;
            }

        }
    };


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e("TAG", "display name: " + acct.getAccount());
            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String personEmail = acct.getEmail();

            Log.e("TAG", "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);
            //manager.setPreferences(ApplicationContext.getAppContext(), AppConstant.STATUS, "1");


            String[] name = personName.split(" ");
            fname = name[0];
            lname = name[1];
            address = "";
            country = "";
            state = "";
            city = "";
            postcode = "";
            email = personEmail;
            phone = "";
            company = "";
            password = "";
            username = "";
            user_id = "";
            //socialRegister(fname,lname,address,country,state,city,postcode,email,phone,company,password,username);
            socialRegister(fname, lname, email, user_id);


            // updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

    private void socialRegister(String fname, String lname, String email, String user_id) {
        int usersize = 5;
        char[] chars1 = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        Random random1 = new Random();
        for (int i = 0; i < usersize; i++) {
            char c = chars1[random1.nextInt(chars1.length)];
            sb1.append(c);
        }

        //password = sb.toString();
        username = fname + "" + lname + "" + sb1.toString();

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("google", "yes");
            jsonObject.putOpt("billing_first_name", fname);
            jsonObject.putOpt("billing_last_name", lname);
            jsonObject.putOpt("billing_address_1", address);
            jsonObject.putOpt("billing_company", "");
            jsonObject.putOpt("billing_city", "");
            jsonObject.putOpt("billing_state", "");
            jsonObject.putOpt("billing_country", "");
            jsonObject.putOpt("billing_email", email);
            jsonObject.putOpt("billing_postcode", "");
            jsonObject.putOpt("billing_phone", "");
            jsonObject.putOpt("password", "");
            jsonObject.put("user_name", username);

            jsonObject.putOpt("shipping_first_name", fname);
            jsonObject.putOpt("shipping_last_name", lname);
            jsonObject.putOpt("shipping_address_1", "");
            jsonObject.putOpt("shipping_company", "");
            jsonObject.putOpt("shipping_city", "");
            jsonObject.putOpt("shipping_state", "");
            jsonObject.putOpt("shipping_country", "");
            jsonObject.putOpt("shipping_postcode", "");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //   Log.d("shailesh", "data  is==="+ object.toString());
        if (AppConstant.key.equals("shopping")) {
            Log.d("shailesh", "data  is===" + AppConstant.key);
            String requestURL = Webservice.BASE_URL + "" + Webservice.URL_CREATEUSER_SOLICAL;
            AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
            RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
            requestTask.delegate = this;
            requestTask.execute(requestURL, "socialregister", jsonObject.toString());

        } else {

            String requestURL = Webservice.BASE_URL + "" + Webservice.URL_CREATEUSER_SOLICAL;
            AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
            RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
            requestTask.delegate = this;
            requestTask.execute(requestURL, "socialregister", jsonObject.toString());

        }

        LoginManager.getInstance().logOut();
        signOut();
    }

    private void Login_webservice(String login) {
        progressDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("username", et_email.getText().toString());
            object.put("google", "no");
            object.put("password", et_password.getText().toString());

        } catch (Exception e) {
            // TODO: handle exception
        }


        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_LOGIN;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, login, object.toString());

    }

    private void socialLogin_webservice(String sociallogin) {
        progressDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("username", email);
            object.put("google", "yes");
            object.put("password", "");

        } catch (Exception e) {
            // TODO: handle exception
        }

        //   Log.d("shailesh", "data  is==="+ object.toString());


        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_LOGIN_SOLICAL;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, sociallogin, object.toString());

    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // registerReceiver(receive, new IntentFilter(Intent_Class_Data.NOTIFICATION));

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // unregisterReceiver(receive);

    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("TAG", "Got cached sign-in");

            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            progressDialog.show();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    progressDialog.dismiss();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    public void backgroundActivityComp(String response, AppConstant.HttpRequestType completedRequestType) {
        AppConstant.HTTPResponseCode responseCode = appData.getResponseCode();
        AppDebugLog.println("Login responseCode : " + responseCode);

        switch (responseCode) {
            case Success:

                sendTokenToServer();

                break;

            case ServerError:
                progressDialog.dismiss();
                appData.showUserAlert(this, getString(R.string.checkusername),
                        getString(R.string.alert_body_server_error), null);
                break;

            case Success0:
                progressDialog.dismiss();
                break;

            case socialSuccess:
                socialLogin_webservice("login");
                break;

            case social0:
                socialLogin_webservice("login");
                break;

            case NotiSuccess:
                progressDialog.dismiss();
                if (AppConstant.key.equals("shopping")) {

                    Intent intentLogin = new Intent(ApplicationContext.getAppContext(), CheckOutActivity.class);
                    intentLogin.putExtra("USER", "user");
                    startActivity(intentLogin);
                    finish();

                } else {
                    Intent intentLogin = new Intent(ApplicationContext.getAppContext(), MainActivity.class);
                    intentLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentLogin.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intentLogin);
                    finish();
                }

                break;
        }
    }

    private void sendTokenToServer() {
        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        final String userid = manager.getPreferences(LoginActivity.this, AppConstant.USER_ID);
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("token", token);
            jsonObject.put("type", "1");
            jsonObject.put("userid", userid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String requestURL = Webservice.BASE_URL + "" + Webservice.URL_NOTIFICATION;
        AppDebugLog.println("requestURL in sendNewDataRequest : " + requestURL);
        RequestTask requestTask = new RequestTask(requestURL, AppConstant.HttpRequestType.DataUpdateRequest);
        requestTask.delegate = this;
        requestTask.execute(requestURL, "noti", jsonObject.toString());
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAG", "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    private void forgotpassword() {
        forgotpassword = new Dialog(activity);
        forgotpassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgotpassword.getWindow().setWindowAnimations(R.style.DialogAnimation1);
        forgotpassword.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        forgotpassword.setContentView(R.layout.activity_forgot_password);
        Window window = forgotpassword.getWindow();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);

        RelativeLayout rl_header = (RelativeLayout) forgotpassword.findViewById(R.id.rl_header);
        LinearLayout ll_back = (LinearLayout) forgotpassword.findViewById(R.id.ll_back);
        LinearLayout ll_cancel = (LinearLayout) forgotpassword.findViewById(R.id.ll_cancel);
        LinearLayout ll_forgot_pass = (LinearLayout) forgotpassword.findViewById(R.id.ll_forgot_pass);
        final EditText et_emailid = (EditText) forgotpassword.findViewById(R.id.et_emailid);
        TextView tv_cancel = (TextView) forgotpassword.findViewById(R.id.tv_cancel);
        rl_header.setBackgroundColor(Color.parseColor(color_back));
        tv_cancel.setTextColor(Color.parseColor(color_back));
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword.dismiss();
            }
        });
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword.dismiss();
            }
        });
        ll_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_emailid.getText().length() == 0) {
                    et_emailid.setError(getString(R.string.please_enter_registered_email_ID));
                } else {
                    ForgotPassword(et_emailid.getText().toString());
                }


            }
        });
        forgotpassword.show();
    }

    public void ForgotPassword(String email_id) {
        pd.show();
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("email", email_id);
        //postParam.put("p", "somepasswordhere");
        if (checkConnectivity()) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Webservice.BASE_URL + "" + Webservice.URL_FORGOT_PASSWORD, new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("res_forgot_pass", response.toString());
                            try {
                                JSONObject object = new JSONObject(response.toString());
                                if (object.has("success")) {
                                    String str_success = object.getString("success");
                                    if (str_success.equalsIgnoreCase("1")) {
                                        forgotpassword.dismiss();
                                        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
                                        dialog.setCancelable(false);
                                        dialog.setTitle("Success");
                                        dialog.setMessage("successfully user to sending email");
                                        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                        final android.support.v7.app.AlertDialog alert = dialog.create();
                                        alert.show();
                                    } else {
                                        Toast.makeText(activity, "Try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                pd.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                pd.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("res", "Error:forgot_pass " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            jsonObjReq.setTag(LoginActivity.this);
            ApplicationContext.getInstance().getRequestQueue().add(jsonObjReq);
            Utility.SetvollyTime30Sec(jsonObjReq);
        } else {
            Toast.makeText(activity, "There is no network connection at the moment.Try again later", Toast.LENGTH_SHORT).show();
        }
    }
}
