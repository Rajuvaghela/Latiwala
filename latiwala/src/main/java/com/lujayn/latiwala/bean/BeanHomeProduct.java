package com.lujayn.latiwala.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BeanHomeProduct implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.title);
        dest.writeString(this.qty);
        dest.writeString(this.currencySymbol);
        dest.writeString(this.description);
        dest.writeString(this.status);
        dest.writeString(this.rating);
        dest.writeString(this.parentCategoryId);
        dest.writeString(this.productType);
        dest.writeString(this.suffix);
        dest.writeString(this.onSale);
        dest.writeString(this.salePrice);
        dest.writeString(this.regularPrice);
        dest.writeString(this.taxProductWithPriceRegula);
        dest.writeString(this.taxProductWithPriceSale);
        dest.writeString(this.image);
    }

    public BeanHomeProduct() {
    }

    protected BeanHomeProduct(Parcel in) {
        this.productId = in.readString();
        this.title = in.readString();
        this.qty = in.readString();
        this.currencySymbol = in.readString();
        this.description = in.readString();
        this.status = in.readString();
        this.rating = in.readString();
        this.parentCategoryId = in.readString();
        this.productType = in.readString();
        this.suffix = in.readString();
        this.onSale = in.readString();
        this.salePrice = in.readString();
        this.regularPrice = in.readString();
        this.taxProductWithPriceRegula = in.readString();
        this.taxProductWithPriceSale = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<BeanHomeProduct> CREATOR = new Parcelable.Creator<BeanHomeProduct>() {
        @Override
        public BeanHomeProduct createFromParcel(Parcel source) {
            return new BeanHomeProduct(source);
        }

        @Override
        public BeanHomeProduct[] newArray(int size) {
            return new BeanHomeProduct[size];
        }
    };
}
