package com.amazon.aws.partners.saasfactory.controller;

import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping("/addnewproduct")
	public String showAddNewProductForm(Product product) {
		return "addProduct";
	}

	@PostMapping("/addProduct")
	public String addProduct(Product product, Model model) throws Exception {
        Product newProduct = new Product();
        newProduct.sku = product.sku;
        newProduct.name = product.name;
        newProduct.price = product.price;
        newProduct.category = new category(3, "Baseball");

		productService.saveProduct(newProduct);
		return "redirect:/";
	}
}
