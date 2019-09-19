package com.amazon.aws.partners.saasfactory.service;

import com.amazon.aws.partners.saasfactory.domain.Product;

import java.util.List;

public interface ProductService {

    public List<Product> getProducts() throws Exception;

    public Product getProduct(Integer productId) throws Exception;

    public Product saveProduct(Product product) throws Exception;

    public void deleteProduct(Product product) throws Exception;
}
