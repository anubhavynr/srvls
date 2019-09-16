package com.amazon.aws.partners.saasfactory.repository;

import com.amazon.aws.partners.saasfactory.domain.Product;

import java.util.List;

public interface ProductDao {

    public Product getProduct(Long productId) throws Exception;

    public List<Product> getProducts() throws Exception;

    public Product saveProduct(Product product) throws Exception;

    public void deleteProduct(Product product) throws Exception;
}
