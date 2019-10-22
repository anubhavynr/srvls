package com.amazon.aws.partners.saasfactory.domain;

import java.io.Serializable;

public class Purchaser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String firstName;
    private String lastName;

    public Purchaser() {
        this(null, null, null);
    }

    public Purchaser(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
