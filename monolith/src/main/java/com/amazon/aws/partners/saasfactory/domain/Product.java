package com.amazon.aws.partners.saasfactory.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String sku;
    private String name;
    private BigDecimal price;

    public Product() {
        this(null, null, null, null);
    }

    public Product(Long id, String sku, String name, BigDecimal price) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
    }

    public Product(Product copyMe) {
        if (copyMe != null) {
            this.id = copyMe.getId();
            this.sku = copyMe.getSku();
            this.name = copyMe.getName();
            this.price = copyMe.getPrice();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
