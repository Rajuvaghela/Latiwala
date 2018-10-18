package com.lujayn.latiwala.bean;

import java.util.ArrayList;

/**
 * Created by Shailesh on 05/08/16.
 */
public class Data {
    private Product_detail product_detail;

    private ArrayList<Shipping_method> shipping_method;

    private ArrayList<Payment_gateways> payment_gateways;

//    private Taxes taxes; //change
  private ArrayList<Taxes> taxes; //change


    private ArrayList<Orders> orders;

    private Orders_detail orders_detail;

    private Options options;

  /*  public Taxes getTaxes() {
        return taxes;
    }

    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }*/

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public Orders_detail getOrders_detail() {
        return orders_detail;
    }

    public void setOrders_detail(Orders_detail orders_detail) {
        this.orders_detail = orders_detail;
    }

    public Product_detail getProduct_detail() {
        return product_detail;
    }

    public void setProduct_detail(Product_detail product_detail) {
        this.product_detail = product_detail;
    }

    public ArrayList<Shipping_method> getShipping_method() {
        return shipping_method;
    }

    public void setShipping_method(ArrayList<Shipping_method> shipping_method) {
        this.shipping_method = shipping_method;
    }

    public ArrayList<Payment_gateways> getPayment_gateways() {
        return payment_gateways;
    }

    public void setPayment_gateways(ArrayList<Payment_gateways> payment_gateways) {
        this.payment_gateways = payment_gateways;
    }

    public ArrayList<Orders> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Orders> orders) {
        this.orders = orders;
    }

    public ArrayList<Taxes> getTaxes() {
        return taxes;
    }

    public void setTaxes(ArrayList<Taxes> taxes) {
        this.taxes = taxes;
    }
}
