package com.lujayn.latiwala.bean;

/**
 * Created by Shailesh on 12/10/16.
 */
public class Payment_details {
    private String paid;

    private String method_id;

    private String method_title;

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getMethod_id() {
        return method_id;
    }

    public void setMethod_id(String method_id) {
        this.method_id = method_id;
    }

    public String getMethod_title() {
        return method_title;
    }

    public void setMethod_title(String method_title) {
        this.method_title = method_title;
    }
}
