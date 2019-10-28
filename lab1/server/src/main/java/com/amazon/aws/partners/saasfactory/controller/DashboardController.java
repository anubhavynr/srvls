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