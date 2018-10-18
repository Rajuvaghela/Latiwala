package com.lujayn.latiwala.bean;

import java.util.ArrayList;

/**
 * Created by Shailesh on 05/08/16.
 */
public class Product_detail {
    private String sale_price;

    private String status;

    private ArrayList<String> gallery_img;

    private Grouped_products grouped_products;

    private String image;

    private String youtube_url;

    private String pdf_file;

    private String qty;

    private String product_type;

    private String wishlist_in;

    private ArrayList<Variations> variations;

    private ArrayList<Variant> variant;

    private String id;

    private String title;

    private String price;

    private String short_description;

    private String description;

    private String regular_price;

    private String parent_category_id;

    private String rating;

    private String review;

    private String currency_symbol;

    private  String text_include_tax;

    private String cart_sale_price;

    private String suffix;
    private String price_format;

    private String decimals;

    private String on_sale;

    private String thousand_separator;

    private String decimal_separator;

    private String availability_html;

    private String max_qty;

    private String is_in_stock;

    private String origional_price;

    private String tax_status;

    private String weight;

    private String dimensions;
    private String product_url;
    private String button_text;

    private String tax_product_with_price_regula_min;

    private String tax_product_with_price_regula_max;
    private String regular_tax_price;
    private String tax_product_with_price_sale_min;
    private String tax_product_with_price_sale_max;
    private String sale_tax_price;
    private String tax_product_with_price_regula;
    private String tax_product_with_price_sale;

    private String display_review;

    public String getYoutube_url() {
        return youtube_url;
    }

    public void setYoutube_url(String youtube_url) {
        this.youtube_url = youtube_url;
    }

    public String getPdf_file() {
        return pdf_file;
    }

    public void setPdf_file(String pdf_file) {
        this.pdf_file = pdf_file;
    }

    public Grouped_products getGrouped_products() {
        return grouped_products;
    }

    public void setGrouped_products(Grouped_products grouped_products) {
        this.grouped_products = grouped_products;
    }

    public String getProduct_url() {
        return product_url;
    }

    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }

    public String getButton_text() {
        return button_text;
    }

    public void setButton_text(String button_text) {
        this.button_text = button_text;
    }

    public String getDisplay_review() {
        return display_review;
    }

    public void setDisplay_review(String display_review) {
        this.display_review = display_review;
    }

    public String getTax_product_with_price_regula() {
        return tax_product_with_price_regula;
    }

    public void setTax_product_with_price_regula(String tax_product_with_price_regula) {
        this.tax_product_with_price_regula = tax_product_with_price_regula;
    }

    public String getTax_product_with_price_sale() {
        return tax_product_with_price_sale;
    }

    public void setTax_product_with_price_sale(String tax_product_with_price_sale) {
        this.tax_product_with_price_sale = tax_product_with_price_sale;
    }

    public String getTax_product_with_price_regula_min() {
        return tax_product_with_price_regula_min;
    }

    public void setTax_product_with_price_regula_min(String tax_product_with_price_regula_min) {
        this.tax_product_with_price_regula_min = tax_product_with_price_regula_min;
    }

    public String getTax_product_with_price_regula_max() {
        return tax_product_with_price_regula_max;
    }

    public void setTax_product_with_price_regula_max(String tax_product_with_price_regula_max) {
        this.tax_product_with_price_regula_max = tax_product_with_price_regula_max;
    }

    public String getRegular_tax_price() {
        return regular_tax_price;
    }

    public void setRegular_tax_price(String regular_tax_price) {
        this.regular_tax_price = regular_tax_price;
    }

    public String getTax_product_with_price_sale_min() {
        return tax_product_with_price_sale_min;
    }

    public void setTax_product_with_price_sale_min(String tax_product_with_price_sale_min) {
        this.tax_product_with_price_sale_min = tax_product_with_price_sale_min;
    }

    public String getTax_product_with_price_sale_max() {
        return tax_product_with_price_sale_max;
    }

    public void setTax_product_with_price_sale_max(String tax_product_with_price_sale_max) {
        this.tax_product_with_price_sale_max = tax_product_with_price_sale_max;
    }

    public String getSale_tax_price() {
        return sale_tax_price;
    }

    public void setSale_tax_price(String sale_tax_price) {
        this.sale_tax_price = sale_tax_price;
    }

    public String getTax_status() {
        return tax_status;
    }

    public void setTax_status(String tax_status) {
        this.tax_status = tax_status;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getOrigional_price() {
        return origional_price;
    }

    public void setOrigional_price(String origional_price) {
        this.origional_price = origional_price;
    }

    public String getIs_in_stock() {
        return is_in_stock;
    }

    public void setIs_in_stock(String is_in_stock) {
        this.is_in_stock = is_in_stock;
    }

    public String getAvailability_html() {
        return availability_html;
    }

    public void setAvailability_html(String availability_html) {
        this.availability_html = availability_html;
    }

    public String getMax_qty() {
        return max_qty;
    }

    public void setMax_qty(String max_qty) {
        this.max_qty = max_qty;
    }

    public ArrayList<Variations> getVariations() {
        return variations;
    }

    public void setVariations(ArrayList<Variations> variations) {
        this.variations = variations;
    }

    public ArrayList<Variant> getVariant() {
        return variant;
    }

    public void setVariant(ArrayList<Variant> variant) {
        this.variant = variant;
    }

    public String getDecimal_separator() {
        return decimal_separator;
    }

    public void setDecimal_separator(String decimal_separator) {
        this.decimal_separator = decimal_separator;
    }

    public String getPrice_format() {
        return price_format;
    }

    public void setPrice_format(String price_format) {
        this.price_format = price_format;
    }

    public String getDecimals() {
        return decimals;
    }

    public void setDecimals(String decimals) {
        this.decimals = decimals;
    }

    public String getOn_sale() {
        return on_sale;
    }

    public void setOn_sale(String on_sale) {
        this.on_sale = on_sale;
    }

    public String getThousand_separator() {
        return thousand_separator;
    }

    public void setThousand_separator(String thousand_separator) {
        this.thousand_separator = thousand_separator;
    }

    public String getCart_sale_price() {
        return cart_sale_price;
    }

    public void setCart_sale_price(String cart_sale_price) {
        this.cart_sale_price = cart_sale_price;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }



    public String getText_include_tax() {
        return text_include_tax;
    }

    public void setText_include_tax(String text_include_tax) {
        this.text_include_tax = text_include_tax;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }


    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getGallery_img() {
        return gallery_img;
    }

    public void setGallery_img(ArrayList<String> gallery_img) {
        this.gallery_img = gallery_img;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getWishlist_in() {
        return wishlist_in;
    }

    public void setWishlist_in(String wishlist_in) {
        this.wishlist_in = wishlist_in;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public void setRegular_price(String regular_price) {
        this.regular_price = regular_price;
    }

    public String getParent_category_id() {
        return parent_category_id;
    }

    public void setParent_category_id(String parent_category_id) {
        this.parent_category_id = parent_category_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
