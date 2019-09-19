package com.amazon.aws.partners.saasfactory.domain;

import java.io.Serializable;

public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;

    public Category() {
        this(null, null);
    }

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(Category copyMe) {
        if (copyMe != null) {
            this.id = copyMe.getId();
            this.name = copyMe.getName();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
