package com.amazon.aws.partners.saasfactory.controller;

import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
	public String addProduct(@Valid Product product, Model model) throws Exception {
		productService.saveProduct(product);
		return "redirect:/products";
    }
    
    @PostMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable("id") Integer id, Model model) throws Exception {
        Product product = productService.getProduct(id);
		productService.deleteProduct(product);
		return "redirect:/products";
    }
}
