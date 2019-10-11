package com.amazon.aws.partners.saasfactory.controller;

import com.amazon.aws.partners.saasfactory.domain.Order;
import com.amazon.aws.partners.saasfactory.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrdersController {
	
	@Autowired
	private OrderService orderService;

	@GetMapping("/orders")
	public String orders(Model model) throws Exception {
		List<Order> orders = orderService.getOrders();
		model.addAttribute("orders", orders);
		return "orders";
	}
}
