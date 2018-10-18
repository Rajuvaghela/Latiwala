package com.lujayn.latiwala.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductDetailBean implements Serializable {

    @SerializedName("product_id")
    @Expose
    public String productId;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("qty")
    @Expose
    public String qty;
    @SerializedName("currency_symbol")
    @Expose
    public String currencySymbol;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("parent_category_id")
    @Expose
    public String parentCategoryId;
    @SerializedName("product_type")
    @Expose
    public String productType;
    @SerializedName("suffix")
    @Expose
    public String suffix;
    @SerializedName("on_sale")
    @Expose
    public String onSale;
    @SerializedName("sale_price")
    @Expose
    public String salePrice;
    @SerializedName("regular_price")
    @Expose
    public String regularPrice;
    @SerializedName("tax_product_with_price_regula")
    @Expose
    public String taxProductWithPriceRegula;
    @SerializedName("tax_product_with_price_sale")
    @Expose
    public String taxProductWithPriceSale;
    @SerializedName("image")
    @Expose
    public String image;
    private final static long serialVersionUID = 6559117620146009958L;

}
