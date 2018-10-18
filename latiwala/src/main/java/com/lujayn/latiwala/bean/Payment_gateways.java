package com.lujayn.latiwala.bean;

import java.util.ArrayList;

/**
 * Created by Shailesh on 20/08/16.
 */
public class Payment_gateways {
    private String id;

    private String enabled;

    private String title;

    private String description;

    private ArrayList<Account_details> account_details;

    private ArrayList<String> enable_for_methods;

    private String instructions;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Account_details> getAccount_details() {
        return account_details;
    }

    public void setAccount_details(ArrayList<Account_details> account_details) {
        this.account_details = account_details;
    }

    public ArrayList<String> getEnable_for_methods() {
        return enable_for_methods;
    }

    public void setEnable_for_methods(ArrayList<String> enable_for_methods) {
        this.enable_for_methods = enable_for_methods;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
