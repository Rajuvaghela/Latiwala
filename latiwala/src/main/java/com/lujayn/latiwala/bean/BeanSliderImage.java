package com.lujayn.latiwala.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BeanSliderImage implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("category_link_id")
    @Expose
    public String categoryLinkId;
    @SerializedName("category_name")
    @Expose
    public String categoryName;
    @SerializedName("child")
    @Expose
    public String child;
    @SerializedName("slug")
    @Expose
    public String slug;
    private final static long serialVersionUID = 5797630145728549703L;

}
