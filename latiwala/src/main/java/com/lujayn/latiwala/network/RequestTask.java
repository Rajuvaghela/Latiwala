package com.lujayn.latiwala.network;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import com.lujayn.latiwala.common.AppConstant;
import com.lujayn.latiwala.common.AppConstant.HttpRequestType;
import com.lujayn.latiwala.custom.AppDebugLog;
import com.lujayn.latiwala.parser.ParseManager;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class RequestTask extends AsyncTask<String, RequestTaskDelegate, HttpURLConnection> {
    public RequestTaskDelegate delegate;

    private String requestURL;
    private String responseContent;
    private String reqType;
    private HttpRequestType currentRequest;
    private Object extraInfo;
    private final String USER_AGENT = "Mozilla/5.0";
    int responseCode;
    public RequestTask(String requestURL, HttpRequestType userRequest) {
        this.requestURL = requestURL;
        this.currentRequest = userRequest;
        this.responseContent = AppConstant.NULL_STRING;
    }

    public RequestTask(String requestURL, AppConstant.HttpRequestType userRequest, Object extraInfo) {
        this.requestURL = requestURL;
        this.currentRequest = userRequest;
        this.extraInfo = extraInfo;
        this.responseContent = AppConstant.NULL_STRING;
    }


    private HttpURLConnection getSimpleRequest(String... params) {
        try {
            for (String param : params) {
                AppDebugLog.println("Params : " + param);
            }

            String urlStr = params[0];
            reqType = params[1];
            URL url = new URL(urlStr);
            AppDebugLog.println("url in  getSimpleRequest : " + url);
            //AppDebugLog.println("params == :" + params.length);
            //AppDebugLog.println("params == :" + params[0]+", "+params[1]);

            HttpURLConnection con = null;

            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection cons = (HttpsURLConnection) url.openConnection();
                //HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                cons.setHostnameVerifier(DO_NOT_VERIFY);
                con = cons;
            } else {
                con = (HttpURLConnection) url.openConnection();
            }

/*importan note: if website will in multilanguage than remove comment and use below code*/

        /*  HttpURLConnection cons = (HttpURLConnection) url.openConnection();
            cons.setInstanceFollowRedirects(false);
            // if print out (debug or logging), you will see secondURL has / at the end
            URL secondURL = new URL(cons.getHeaderField("Location"));
            con = (HttpURLConnection) secondURL.openConnection();
        */

            AppDebugLog.println("url = "+ url);

            if (params.length == 3) {// Post

                // add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("content-type", "application/json");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String postParam = params[2];
                AppDebugLog.println("Sending 'POST' request to URL : " + url);
                AppDebugLog.println("Post parameters : " + postParam);

                // Send post request
                con.setDoOutput(true);
                //DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                OutputStream wr = con.getOutputStream();
                wr.write(postParam.getBytes("UTF-8"));
                wr.flush();
                wr.close();

                responseCode = con.getResponseCode();

                AppDebugLog.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine + "\n");
                }
                in.close();

                responseContent = response.toString();
                AppDebugLog.println("responseContent in getSimpleRequest : " + responseContent);

            } else {// GET
                // optional default is GET
                con.setRequestMethod("GET");

                // add request header
                con.setRequestProperty("content-type", "application/json");


                responseCode = con.getResponseCode();
                AppDebugLog.println("\nSending 'GET' request to URL in getSimpleRequest: " + url);
                AppDebugLog.println("Response Code in getSimpleRequest : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                responseContent = response.toString();
                AppDebugLog.println("responseContent in getSimpleRequest : " + responseContent);
            }

            return con;
        } catch (UnsupportedEncodingException e) {
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (Exception e) {
            AppDebugLog.println("Exception in getSimpleRequest : " + e.getLocalizedMessage());
        }

        return null;
    }

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getContinuesRequest(String... params) {
        int count;
        try {
            LocalFileManager fileManager = LocalFileManager.sharedFileManager(null);

            URL url = new URL(params[0]);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();
            URLConnection conection = url.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            String fileURL = fileManager.getAbsolutePathForURL(requestURL, true);
            OutputStream output = new FileOutputStream(new File(fileURL));

            byte data[] = new byte[1024];

            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                if (this.isCancelled()) {
                    throw new Exception("Task Cancelled");
                }
                if (delegate != null) {
                    delegate.percentageDownloadCompleted((int) ((total * 100) / lenghtOfFile), extraInfo);
                }
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            LocalFileManager.sharedFileManager(null).deleteFileForURL(requestURL);
        }
    }

    protected HttpURLConnection doInBackground(String... params) {
        // String line = null;

        HttpURLConnection httpURLConnection = this.getSimpleRequest(params);

        LocalFileManager fileManager = LocalFileManager.sharedFileManager(null);

        if (responseCode==200){
            if (responseContent.length() > 0) {
                fileManager.saveContentForURL(this.requestURL, responseContent);
            } else {
                String oldContent = fileManager.getContentForURL(requestURL);
                if (oldContent.length() > 0) {
                    responseContent = oldContent;
                }

                return httpURLConnection;
                // this.getResponseContent(entity);
            }
            return null;
        }
        return null;
    }

    protected void onProgressUpdate(String sResponse) {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(HttpURLConnection httpURLConnection) {
        super.onPostExecute(httpURLConnection);
//
//        if (responseContent.length() == 0 && entity != null) {
//            this.getResponseContent(entity);
//        }
        switch (currentRequest) {
            case DataUpdateRequest:
                if (reqType.equals("option")){
                    ParseManager.parseSettingOptionResponse(responseContent);
                }else if (reqType.equals("category")){
                    ParseManager.parseDataUpdateResponse(responseContent);
                }else if (reqType.equals("subcategory")){
                    ParseManager.parseSubcategoryUpdateResponse(responseContent);
                }else if (reqType.equals("products")){

                    //Log.e("TEST", "AppConstant.CATEGORYSLUG  =" + AppConstant.CATEGORYSLUG );
                    ParseManager.parseProductUpdateResponse(responseContent);
                }else if (reqType.equals("productDetail")){
                    ParseManager.parseProductDetailResponse(responseContent);
                }else if (reqType.equals("reviews")){
                    ParseManager.parseReviewsResponse(responseContent);
                }else if (reqType.equals("saveReview")){
                    ParseManager.parseSaveReviewsResponse(responseContent);
                }else if (reqType.equals("Changepassword")){
                    ParseManager.parseChangePassordResponse(responseContent);
                }else if (reqType.equals("country")){
                    ParseManager.parseCountryResponse(responseContent);
                }else if (reqType.equals("state")){
                    ParseManager.parseStatesResponse(responseContent);
                }else if (reqType.equals("login")){
                    ParseManager.parseLoginResponse(responseContent);
                }else if (reqType.equals("register")){
                    ParseManager.parseRegisterResponse(responseContent);
                }else if (reqType.equals("forgotpassword")){
                    ParseManager.parseForgotPasswordResponse(responseContent);
                }else if (reqType.equals("saveBill")){
                    ParseManager.parseSaveBillResponse(responseContent);

                }else if (reqType.equals("saveShip")){
                    ParseManager.parseSaveShipResponse(responseContent);
                }else if (reqType.equals("save")){
                    ParseManager.parseSaveResponse(responseContent);
                }else if (reqType.equals("orderdetail")){
                    ParseManager.parseOrderDetailResponse(responseContent);
                } else if (reqType.equals("pastorder")){
                    ParseManager.parseOrdersResponse(responseContent);
                }else if ((reqType.equals("search"))){

                    ParseManager.parseSearchResponse(responseContent);
                }else if (reqType.equals("method")){
                    ParseManager.parseMethodResponse(responseContent);
                }else if (reqType.equals("order")){
                    ParseManager.parseSaveOrderResponse(responseContent);
                }else if (reqType.equals("code")){
                    ParseManager.parseApplyCouponResponse(responseContent);
                }else if (reqType.equals("removecode")){
                    ParseManager.parseRemoveCouponResponse(responseContent);
                }else if (reqType.equals("register1")){
                    ParseManager.parseRegisterResponse1(responseContent);
                }else if (reqType.equals("noti")){
                    ParseManager.parseNotification(responseContent);
                }else if (reqType.equals("socialregister")){
                    ParseManager.parseSocialResponse(responseContent);
                }else if (reqType.equals("hashkey")){
                    ParseManager.parseHashKeyResponse(responseContent);
                }else if (reqType.equals("paytmhash")){
                    ParseManager.parsePaytmChecksumKeyResponse(responseContent);
                }else if (reqType.equals("paytmhashverify")){
                    ParseManager.parsePaytmChecksumKeyVerifyResponse(responseContent);
                }else if (reqType.equals("paytmorder")){
                    ParseManager.parsePaytmResponse(responseContent);
                }
                break;

            default:
                break;
        }

        delegate.backgroundActivityComp(responseContent, currentRequest);
    }

//    private void getResponseContent(HttpEntity entity) {
//        // LocalFileManager fileManager =
//        // LocalFileManager.sharedFileManager(null);
//        // if (currentRequest != HttpRequestType.FileDownloadRequest) {
//        try {
//            responseContent = EntityUtils.toString(entity, HTTP.UTF_8);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
