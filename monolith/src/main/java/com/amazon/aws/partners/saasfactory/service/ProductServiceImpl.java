package com.amazon.aws.partners.saasfactory.service;

import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.domain.User;
import com.amazon.aws.partners.saasfactory.repository.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getProductsByUser(User user) throws Exception {
        return productDao.getProducts();
    }
}
