package com.lujayn.latiwala.bean;

/**
 * Created by Shailesh on 16/09/16.
 */
public class BeanCoupon {
    private String usage_count;

    private String[] customer_emails;

    private String maximum_amount;

    private String usage_limit_per_user;

    private String exclude_sale_items;

    private String code;

    private String individual_use;

    private String type;

    private String[] product_ids;

    private String id;

    private String amount;

    private String limit_usage_to_x_items;

    private String[] product_category_ids;

    private String description;

    private String minimum_amount;

    private String expiry_date;

    private String enable_free_shipping;

    private String[] exclude_product_ids;

    private String[] exclude_product_category_ids;

    private Setting usage_limit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEnable_free_shipping() {
        return enable_free_shipping;
    }

    public void setEnable_free_shipping(String enable_free_shipping) {
        this.enable_free_shipping = enable_free_shipping;
    }

    public String getExclude_sale_items() {
        return exclude_sale_items;
    }

    public void setExclude_sale_items(String exclude_sale_items) {
        this.exclude_sale_items = exclude_sale_items;
    }

    public String getUsage_count() {
        return usage_count;
    }

    public void setUsage_count(String usage_count) {
        this.usage_count = usage_count;
    }

    public String[] getCustomer_emails() {
        return customer_emails;
    }

    public void setCustomer_emails(String[] customer_emails) {
        this.customer_emails = customer_emails;
    }

    public String getMaximum_amount() {
        return maximum_amount;
    }

    public void setMaximum_amount(String maximum_amount) {
        this.maximum_amount = maximum_amount;
    }

    public String getUsage_limit_per_user() {
        return usage_limit_per_user;
    }

    public void setUsage_limit_per_user(String usage_limit_per_user) {
        this.usage_limit_per_user = usage_limit_per_user;
    }

    public String getIndividual_use() {
        return individual_use;
    }

    public void setIndividual_use(String individual_use) {
        this.individual_use = individual_use;
    }

    public String[] getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(String[] product_ids) {
        this.product_ids = product_ids;
    }

    public String getLimit_usage_to_x_items() {
        return limit_usage_to_x_items;
    }

    public void setLimit_usage_to_x_items(String limit_usage_to_x_items) {
        this.limit_usage_to_x_items = limit_usage_to_x_items;
    }

    public String[] getProduct_category_ids() {
        return product_category_ids;
    }

    public void setProduct_category_ids(String[] product_category_ids) {
        this.product_category_ids = product_category_ids;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMinimum_amount() {
        return minimum_amount;
    }

    public void setMinimum_amount(String minimum_amount) {
        this.minimum_amount = minimum_amount;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String[] getExclude_product_ids() {
        return exclude_product_ids;
    }

    public void setExclude_product_ids(String[] exclude_product_ids) {
        this.exclude_product_ids = exclude_product_ids;
    }

    public String[] getExclude_product_category_ids() {
        return exclude_product_category_ids;
    }

    public void setExclude_product_category_ids(String[] exclude_product_category_ids) {
        this.exclude_product_category_ids = exclude_product_category_ids;
    }

    public Setting getUsage_limit() {
        return usage_limit;
    }

    public void setUsage_limit(Setting usage_limit) {
        this.usage_limit = usage_limit;
    }
}
