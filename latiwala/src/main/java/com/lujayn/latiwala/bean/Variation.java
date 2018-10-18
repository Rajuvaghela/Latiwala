package com.lujayn.latiwala.bean;

/**
 * Created by Shailesh on 27/01/17.
 */

public class Variation {

    private Integer variations_id;

    private String color;

    private String size;

    private Integer variation_price;



    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public Integer getVariations_id() {
        return variations_id;
    }

    public void setVariations_id(Integer variations_id) {
        this.variations_id = variations_id;
    }

    public Integer getVariation_price() {
        return variation_price;
    }

    public void setVariation_price(Integer variation_price) {
        this.variation_price = variation_price;
    }
}
