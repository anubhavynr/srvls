package com.amazon.aws.partners.saasfactory.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class OrderLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer orderId;
    private Product product;
    private Integer quantity;
    private BigDecimal unitPurchasePrice;

    public OrderLineItem() {
        this(null, null, null, null, null);
    }

    public OrderLineItem(Integer id, Integer orderId, Product product, Integer quantity, BigDecimal unitPurchasePrice) {
        this.id = id;
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.unitPurchasePrice = unitPurchasePrice;
    }

    public BigDecimal getExtendedPurchasePrice() {
        BigDecimal extendedPurchasePrice = unitPurchasePrice.multiply(new BigDecimal(quantity.intValue(), new MathContext(2, RoundingMode.HALF_EVEN)));
        return extendedPurchasePrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPurchasePrice() {
        return unitPurchasePrice;
    }

    public void setUnitPurchasePrice(BigDecimal unitPurchasePrice) {
        this.unitPurchasePrice = unitPurchasePrice;
    }

}
