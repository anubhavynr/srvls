package com.amazon.aws.partners.saasfactory;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public String toString() {
        StringBuilder category = new StringBuilder();
        category.append(super.toString());
        category.append(" {\"id\":");
        category.append(getId());
        category.append("\",\"name\":");
        if (getName() == null) {
            category.append("null");
        } else {
            category.append("\"");
            category.append(getName());
            category.append("\"");
        }
        category.append("}");
        return category.toString();
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
