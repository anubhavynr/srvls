package com.amazon.aws.partners.saasfactory.controller;

import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import javax.validation.Valid;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
public class ProductsController {

	@Autowired
	private ProductService productService;

	@GetMapping("/products")
	public String products(Model model) throws Exception {
		List<Product> products = productService.getProducts();
		model.addAttribute("products", products);
		return "products";
	}

	// @PostMapping("/addProduct")
	// public String addProduct(@Valid Product product, BindingResult result, Model model) throws Exception {
	// 	if (result.hasErrors()) {
	// 		return "add-product";
	// 	}

	// 	productService.saveProduct(product);
	// 	List<Product> products = productService.getProducts();
	// 	model.addAttribute("products", products);
	// 	return "products";
	// }
}
