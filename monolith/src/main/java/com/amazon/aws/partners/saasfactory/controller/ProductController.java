package com.amazon.aws.partners.saasfactory.controller;

import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("/addProduct")
	public String productAddForm(@Valid Product product, BindingResult result, Model model) throws Exception {
		if (result.hasErrors()) {
			return "add-product";
		}

		productService.saveProduct(product);
		model.addAttribute("products", productService.getProducts());
		return "products";
	}
}
