package com.lujayn.latiwala.bean;

/**
 * Created by Shailesh on 01/06/17.
 */

public class Base_location {
    private String woocommerce_default_customer_address;

    private String woocommerce_default_country;

    private String woocommerce_allowed_countries;

    private String woocommerce_ship_to_countries;


    public String getWoocommerce_default_customer_address() {
        return woocommerce_default_customer_address;
    }

    public void setWoocommerce_default_customer_address(String woocommerce_default_customer_address) {
        this.woocommerce_default_customer_address = woocommerce_default_customer_address;
    }

    public String getWoocommerce_default_country() {
        return woocommerce_default_country;
    }

    public void setWoocommerce_default_country(String woocommerce_default_country) {
        this.woocommerce_default_country = woocommerce_default_country;
    }

    public String getWoocommerce_allowed_countries() {
        return woocommerce_allowed_countries;
    }

    public void setWoocommerce_allowed_countries(String woocommerce_allowed_countries) {
        this.woocommerce_allowed_countries = woocommerce_allowed_countries;
    }

    public String getWoocommerce_ship_to_countries() {
        return woocommerce_ship_to_countries;
    }

    public void setWoocommerce_ship_to_countries(String woocommerce_ship_to_countries) {
        this.woocommerce_ship_to_countries = woocommerce_ship_to_countries;
    }
}
