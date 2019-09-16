package com.amazon.aws.partners.saasfactory.service;

import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.domain.User;

import java.util.List;

public interface ProductService {

    public List<Product> getProductsByUser(User user) throws Exception;
}
