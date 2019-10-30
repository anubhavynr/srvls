package com.amazon.aws.partners.saasfactory.controller;

import java.util.Date;
import java.util.List;

import com.amazon.aws.partners.saasfactory.domain.Order;
import com.amazon.aws.partners.saasfactory.domain.OrderLineItem;
import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.service.OrderService;
import com.amazon.aws.partners.saasfactory.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrdersController {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductService productService;

	@GetMapping("/orders")
	public String orders(Model model) throws Exception {
		List<Order> orders = orderService.getOrders();
		List<Product> products = productService.getProducts();

		model.addAttribute("orders", orders);
		model.addAttribute("products", products);

		return "orders";
	}
	
	@PostMapping("/orders")
	public String insertOrder(@ModelAttribute Order order, BindingResult bindingResult, Model model) throws Exception {
		LOGGER.info("OrdersController::insertOrder " + order);
		
		model.addAttribute("order", order);
		
		Date date = new Date();
		
		order.setOrderDate(date);
		order.setBillAddress(order.getShipAddress());
		
		List<OrderLineItem> lineItems = order.getLineItems();
		OrderLineItem lineItem1 = lineItems.get(0);
		OrderLineItem lineItem2 = lineItems.get(1);
		OrderLineItem lineItem3 = lineItems.get(2);
		
		if(lineItem1 != null) {
			Product product1 = productService.getProduct(lineItem1.getProduct().getId());	
			lineItem1.setUnitPurchasePrice(product1.getPrice());
		}
		
		if(lineItem2 != null) {
			Product product2 = productService.getProduct(lineItem2.getProduct().getId());	
			lineItem2.setUnitPurchasePrice(product2.getPrice());
		}
		
		if(lineItem3 != null) {
			Product product3 = productService.getProduct(lineItem3.getProduct().getId());	
			lineItem3.setUnitPurchasePrice(product3.getPrice());
		}
		
		if(lineItem3.getQuantity() == 0) {
			lineItems.remove(2);
		}
		
		if(lineItem2.getQuantity() == 0) {
			lineItems.remove(1);
		}
		
		if(lineItem1.getQuantity() == 0) {
			lineItems.remove(0);
		}
		
		order = orderService.saveOrder(order);
		
		return "redirect:/orders";
	}
	
	@PostMapping("/updateOrder")
	public String updateOrder(@ModelAttribute Order order, BindingResult bindingResult, Model model) throws Exception {
		LOGGER.info("OrdersController::updateOrder " + order);

		model.addAttribute("order", order);
		
		order.setBillAddress(order.getShipAddress());
		
		List<OrderLineItem> lineItems = order.getLineItems();
		OrderLineItem lineItem1 = lineItems.get(0);
		OrderLineItem lineItem2 = lineItems.get(1);
		OrderLineItem lineItem3 = lineItems.get(2);
		
		if(lineItem1 != null) {
			Product product1 = productService.getProduct(lineItem1.getProduct().getId());	
			lineItem1.setUnitPurchasePrice(product1.getPrice());
		}
		
		if(lineItem2 != null) {
			Product product2 = productService.getProduct(lineItem2.getProduct().getId());	
			lineItem2.setUnitPurchasePrice(product2.getPrice());
		}
		
		if(lineItem3 != null) {
			Product product3 = productService.getProduct(lineItem3.getProduct().getId());	
			lineItem3.setUnitPurchasePrice(product3.getPrice());
		}
		
		if(lineItem3.getQuantity() == 0) {
			lineItems.remove(2);
		}
		
		if(lineItem2.getQuantity() == 0) {
			lineItems.remove(1);
		}
		
		if(lineItem1.getQuantity() == 0) {
			lineItems.remove(0);
		}

		order = orderService.saveOrder(order);
		
		return "redirect:/orders";
	}
	
	@PostMapping("/deleteOrder")
	public String deleteOrder(@ModelAttribute Order order) throws Exception {
		LOGGER.info("OrdersController::deleteOrder " + order.getId());
		
		orderService.deleteOrder(order);
		
		return "redirect:/orders";
	}
}