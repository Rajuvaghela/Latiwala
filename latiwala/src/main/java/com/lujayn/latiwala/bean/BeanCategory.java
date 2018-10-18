package com.lujayn.latiwala.bean;

import java.util.ArrayList;

/**
 * Created by Shailesh on 26/07/16.
 */
public class BeanCategory {

    private String category_id;
    private String category_name;
    private String slug;
    private String description;
    private String image;
    private String count;
    private String child;

    private ArrayList<BeanSubCategory> beanSubCategories;


    public BeanCategory() {
        category_id = "";
        category_name = "";
        slug = "";
        description = "";
        image = "";
        count = "";
        child = "";

    }

    public BeanCategory(ArrayList<BeanSubCategory> beanSubCategories) {
        this.beanSubCategories = beanSubCategories;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public ArrayList<BeanSubCategory> getBeanSubCategories() {
        return beanSubCategories;
    }

    public void setBeanSubCategories(ArrayList<BeanSubCategory> beanSubCategories) {
        this.beanSubCategories = beanSubCategories;
    }


}
