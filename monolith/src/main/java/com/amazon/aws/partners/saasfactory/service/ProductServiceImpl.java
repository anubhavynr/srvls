package com.amazon.aws.partners.saasfactory.service;

import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.repository.ProductDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getProducts() throws Exception {
        StopWatch timer = new StopWatch();
        timer.start();
        List<Product> products = productDao.getProducts();
        timer.stop();
        logger.info("getProducts execution " + timer.getTotalTimeMillis());
        return products;
    }
}
