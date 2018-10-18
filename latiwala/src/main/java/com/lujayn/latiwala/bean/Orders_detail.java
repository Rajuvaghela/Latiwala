package com.lujayn.latiwala.bean;

import java.util.ArrayList;

/**
 * Created by Shailesh on 12/10/16.
 */
public class Orders_detail {

    private Order_detail order_detail;

    private ArrayList<Products> products;

    public Order_detail getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(Order_detail order_detail) {
        this.order_detail = order_detail;
    }

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }
}
