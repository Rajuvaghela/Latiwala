package com.lujayn.latiwala.bean;

/**
 * Created by Shailesh on 12/10/16.
 */
public class Order_detail {
    private String order_number;

    private String total;

    private String shipping_tax;

    private String status;

    private String total_discount;

    private String shipping_methods;

    private String order_id;

    private String subtotal;

    private Payment_details payment_details;

    private Shipping_address shipping_address;

    private String total_shipping;

    private String cart_tax;

    private String total_tax;

    private Billing_address billing_address;

    public String getShipping_tax() {
        return shipping_tax;
    }

    public void setShipping_tax(String shipping_tax) {
        this.shipping_tax = shipping_tax;
    }

    public String getTotal_shipping() {
        return total_shipping;
    }

    public void setTotal_shipping(String total_shipping) {
        this.total_shipping = total_shipping;
    }

    public String getCart_tax() {
        return cart_tax;
    }

    public void setCart_tax(String cart_tax) {
        this.cart_tax = cart_tax;
    }

    public String getTotal_tax() {
        return total_tax;
    }

    public void setTotal_tax(String total_tax) {
        this.total_tax = total_tax;
    }

    public String getShipping_methods() {
        return shipping_methods;
    }

    public void setShipping_methods(String shipping_methods) {
        this.shipping_methods = shipping_methods;
    }

    public Shipping_address getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(Shipping_address shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(String total_discount) {
        this.total_discount = total_discount;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Payment_details getPayment_details() {
        return payment_details;
    }

    public void setPayment_details(Payment_details payment_details) {
        this.payment_details = payment_details;
    }

    public Billing_address getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(Billing_address billing_address) {
        this.billing_address = billing_address;
    }
}
