
package com.lujayn.latiwala.bean;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BeanHomeCategory implements Serializable
{

    @SerializedName("category_id")
    @Expose
    public String categoryId;
    @SerializedName("category_name")
    @Expose
    public String categoryName;
    @SerializedName("count")
    @Expose
    public String count;
    @SerializedName("child")
    @Expose
    public String child;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("slug")
    @Expose
    public String slug;
    @SerializedName("image")
    @Expose
    public String image;
    private final static long serialVersionUID = -2049271614075485758L;

}