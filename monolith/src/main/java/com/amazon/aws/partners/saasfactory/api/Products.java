package com.amazon.aws.partners.saasfactory.api;

import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path="/api")
public class Products {

    private static final Logger logger = LoggerFactory.getLogger(Products.class);

    @Autowired
    private ProductService productService;

    @CrossOrigin
    @GetMapping(path = "/products/{id}")
    public Product getProduct(@PathVariable Integer id) throws Exception {
        return productService.getProduct(id);
    }

    @CrossOrigin
    @GetMapping(path = "/products")
    public List<Product> getProducts() throws Exception {
        return productService.getProducts();
    }

    @CrossOrigin
    @PutMapping(path = "/products/{id}")
    public Product saveProduct(@PathVariable Integer id, @RequestBody Product product) throws Exception {
        logger.info("Products::saveProduct [id = " + id + ", product = " + product + ", product.id = " + (product != null ? product.getId() : "null") + "]");
        if (id == null || product == null || !id.equals(product.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productService.saveProduct(product);
    }

    @CrossOrigin
    @PostMapping(path = "/products")
    public Product saveProduct(@RequestBody Product product) throws Exception {
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productService.saveProduct(product);
    }

    @CrossOrigin
    @DeleteMapping(path = "/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable Integer id, @RequestBody Product product) throws Exception {
        if (id == null || product == null || !id.equals(product.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        productService.deleteProduct(product);
        return ResponseEntity.ok().build();
    }
}
