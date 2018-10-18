package com.lujayn.latiwala.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lujayn-15 on 26/4/18.
 */

public class BeanSearchProduct implements Parcelable {

    @SerializedName("product_id")
    public String Product_id;

    @SerializedName("title")
    public String Title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Product_id);
        dest.writeString(this.Title);
    }

    public BeanSearchProduct() {
    }

    protected BeanSearchProduct(Parcel in) {
        this.Product_id = in.readString();
        this.Title = in.readString();
    }

    public static final Parcelable.Creator<BeanSearchProduct> CREATOR = new Parcelable.Creator<BeanSearchProduct>() {
        @Override
        public BeanSearchProduct createFromParcel(Parcel source) {
            return new BeanSearchProduct(source);
        }

        @Override
        public BeanSearchProduct[] newArray(int size) {
            return new BeanSearchProduct[size];
        }
    };
}
