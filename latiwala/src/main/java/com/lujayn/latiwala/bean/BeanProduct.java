package com.lujayn.latiwala.bean;

import java.io.Serializable;

/**
 * Created by Shailesh on 27/07/16.
 */
public class BeanProduct implements Serializable {
    private String product_id;
    private String title;
    private String qty;
    private String description;
    private String status;
    private String sale_price;
    private String regular_price;
    private String rating;
    private String parant_category_id;
    private String image;
    private String count;
    private String currency_symbol;
    private String on_sale;
    private String wish;
    //private String product_url;
    //private String button_text;
    private String tax_product_with_price_regula_min;
    private String tax_product_with_price_regula_max;
    private String regular_tax_price;
    private String tax_product_with_price_sale_min;
    private String tax_product_with_price_sale_max;
    private String sale_tax_price;
    private String tax_product_with_price_regula;
    private String tax_product_with_price_sale;


    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public String getTax_product_with_price_regula() {
        return tax_product_with_price_regula;
    }

    public void setTax_product_with_price_regula(String tax_product_with_price_regula) {
        this.tax_product_with_price_regula = tax_product_with_price_regula;
    }

    public String getTax_product_with_price_sale() {
        return tax_product_with_price_sale;
    }

    public void setTax_product_with_price_sale(String tax_product_with_price_sale) {
        this.tax_product_with_price_sale = tax_product_with_price_sale;
    }

    public String getOn_sale() {
        return on_sale;
    }

    public void setOn_sale(String on_sale) {
        this.on_sale = on_sale;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParant_category_id() {
        return parant_category_id;
    }

    public void setParant_category_id(String parant_category_id) {
        this.parant_category_id = parant_category_id;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public void setRegular_price(String regular_price) {
        this.regular_price = regular_price;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getTax_product_with_price_regula_min() {
        return tax_product_with_price_regula_min;
    }

    public void setTax_product_with_price_regula_min(String tax_product_with_price_regula_min) {
        this.tax_product_with_price_regula_min = tax_product_with_price_regula_min;
    }

    public String getTax_product_with_price_regula_max() {
        return tax_product_with_price_regula_max;
    }

    public void setTax_product_with_price_regula_max(String tax_product_with_price_regula_max) {
        this.tax_product_with_price_regula_max = tax_product_with_price_regula_max;
    }

    public String getRegular_tax_price() {
        return regular_tax_price;
    }

    public void setRegular_tax_price(String regular_tax_price) {
        this.regular_tax_price = regular_tax_price;
    }

    public String getTax_product_with_price_sale_min() {
        return tax_product_with_price_sale_min;
    }

    public void setTax_product_with_price_sale_min(String tax_product_with_price_sale_min) {
        this.tax_product_with_price_sale_min = tax_product_with_price_sale_min;
    }

    public String getTax_product_with_price_sale_max() {
        return tax_product_with_price_sale_max;
    }

    public void setTax_product_with_price_sale_max(String tax_product_with_price_sale_max) {
        this.tax_product_with_price_sale_max = tax_product_with_price_sale_max;
    }

    public String getSale_tax_price() {
        return sale_tax_price;
    }

    public void setSale_tax_price(String sale_tax_price) {
        this.sale_tax_price = sale_tax_price;
    }
}
