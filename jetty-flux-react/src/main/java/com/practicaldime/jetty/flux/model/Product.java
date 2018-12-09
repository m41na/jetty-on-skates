package com.practicaldime.jetty.flux.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class Product {
    
    private Long id;
    private String name;
    private String descr;
    private String imgUrl;
    private BigDecimal price;

    public Product() {
        super();
    }

    public Product(@JsonProperty("id") Long id, 
            @JsonProperty("name") String name, 
            @JsonProperty("descr") String descr, 
            @JsonProperty("image") String imgUrl, 
            @JsonProperty("price") BigDecimal price) {
        this.id = id;
        this.name = name;
        this.descr = descr;
        this.imgUrl = imgUrl;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", descr=" + descr + ", imgUrl=" + imgUrl + ", price=" + price + '}';
    }
}
