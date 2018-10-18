
package com.lujayn.latiwala.bean;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisscountMethodList implements Serializable
{

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("amount")
    @Expose
    public String amount;
    @SerializedName("type")
    @Expose
    public String type;
    private final static long serialVersionUID = 5648605565282719289L;

}
