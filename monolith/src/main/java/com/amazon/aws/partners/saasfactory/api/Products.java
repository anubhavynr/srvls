package com.amazon.aws.partners.saasfactory.api;

import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path="/api")
public class Products {

    @Autowired
    private ProductService productService;

    @GetMapping(path = "/products/{id}")
    public Product getProduct(@PathVariable Long id) throws Exception {
        return productService.getProduct(id);
    }

    @GetMapping(path = "/products")
    public List<Product> getProducts() throws Exception {
        return productService.getProducts();
    }

    @PutMapping(path = "/products/{id}")
    public Product saveProduct(@PathVariable Long id, @RequestBody Product product) throws Exception {
        if (id == null || product == null || !id.equals(product.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productService.saveProduct(product);
    }

    @PostMapping(path = "/products")
    public Product saveProduct(@RequestBody Product product) throws Exception {
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return productService.saveProduct(product);
    }

    @DeleteMapping(path = "/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id, @RequestBody Product product) throws Exception {
        if (id == null || product == null || !id.equals(product.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        productService.deleteProduct(product);
        return ResponseEntity.ok().build();
    }
}
