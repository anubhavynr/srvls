/**
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.amazon.aws.partners.saasfactory.controller;

import java.util.List;
import java.util.Random;

import com.amazon.aws.partners.saasfactory.domain.Order;
import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.service.OrderService;
import com.amazon.aws.partners.saasfactory.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class DashboardController {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);
    
    @Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

    @ModelAttribute("productCount")
    public Integer productCount() throws Exception {
        List<Product> products = productService.getProducts();
        
        return products.size();
    }

    @ModelAttribute("orderCount")
    public Integer orderCount() throws Exception {
        List<Order> orders = orderService.getOrders();
        
        return orders.size();
    }
    
    @ModelAttribute("trend")
    public String trend() {
        String[] trendArray = {
            "up",
            "down",
            "right"
        };
        
        Random rand = new Random(); 
        int trendIndex = rand.nextInt(3);
        
        return trendArray[trendIndex];
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) throws Exception {
        return "dashboard";
    }
}