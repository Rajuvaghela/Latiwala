package com.lujayn.latiwala.bean;

import java.util.ArrayList;

/**
 * Created by Shailesh on 06/02/17.
 */

public class Variant {

    private ArrayList<String> value;

    private String key;


    public ArrayList<String> getValue() {
        return value;
    }

    public void setValue(ArrayList<String> value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
