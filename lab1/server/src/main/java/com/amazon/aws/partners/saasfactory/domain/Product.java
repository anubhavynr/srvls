package com.amazon.aws.partners.saasfactory.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String sku;
    private String name;
    private BigDecimal price;
    private Category category;

    public Product() {
        this(null, null, null, null);
    }

    public Product(Integer id, String sku, String name, BigDecimal price) {
        this(id, sku, name, price, null);
    }

    public Product(Integer id, String sku, String name, BigDecimal price, Category category) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Product(Product copyMe) {
        if (copyMe != null) {
            this.id = copyMe.getId();
            this.sku = copyMe.getSku();
            this.name = copyMe.getName();
            this.price = copyMe.getPrice();
            this.category = copyMe.getCategory();
        }
    }

    @Override
    public String toString() {
        StringBuilder product = new StringBuilder();
        product.append(super.toString());
        product.append(" {\"id\":");
        product.append(getId());
        product.append(",\"sku\":\"");
        product.append(getSku());
        product.append("\",\"name\":\"");
        product.append(getName());
        product.append("\",\"price\":");
        product.append(getPrice());
        product.append(",\"category\":{\"id\":");
        product.append(getCategory() != null ? getCategory().getId() : "null");
        product.append(",\"name\":");
        if (getCategory() == null) {
            product.append("null");
        } else {
            product.append("\"");
            product.append(getCategory().getName());
            product.append("\"");
        }
        product.append("}");
        product.append("}");
        return product.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}