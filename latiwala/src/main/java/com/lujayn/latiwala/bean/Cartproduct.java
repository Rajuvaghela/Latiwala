package com.lujayn.latiwala.bean;

/**
 * Created by Shailesh on 16/10/17.
 */

public class Cartproduct {

    private String product_id;

    private String product_name;

    private String variations;

    private String variation_price;

    private String variations_id;

    private String qnty;

    private String image;

    private String subTotal;

    private String max_stock;

    private String ori_price;

    private String tax_status;

    public String getVariations_id() {
        return variations_id;
    }

    public void setVariations_id(String variations_id) {
        this.variations_id = variations_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getVariations() {
        return variations;
    }

    public void setVariations(String variations) {
        this.variations = variations;
    }

    public String getVariation_price() {
        return variation_price;
    }

    public void setVariation_price(String variation_price) {
        this.variation_price = variation_price;
    }

    public String getQnty() {
        return qnty;
    }

    public void setQnty(String qnty) {
        this.qnty = qnty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getMax_stock() {
        return max_stock;
    }

    public void setMax_stock(String max_stock) {
        this.max_stock = max_stock;
    }

    public String getOri_price() {
        return ori_price;
    }

    public void setOri_price(String ori_price) {
        this.ori_price = ori_price;
    }

    public String getTax_status() {
        return tax_status;
    }

    public void setTax_status(String tax_status) {
        this.tax_status = tax_status;
    }
}
