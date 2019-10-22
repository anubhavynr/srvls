package com.amazon.aws.partners.saasfactory.repository;

import com.amazon.aws.partners.saasfactory.domain.Order;

import java.util.List;

public interface OrderDao {
    
    public Order getOrder(Integer orderId) throws Exception;

    public List<Order> getOrders() throws Exception;

    public Order saveOrder(Order order) throws Exception;

    public void deleteOrder(Order order) throws Exception;
}
