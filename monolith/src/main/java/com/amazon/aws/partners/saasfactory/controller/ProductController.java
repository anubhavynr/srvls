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

	@GetMapping("/addproduct")
	public String showAddNewProductForm(Product product) {
		return "addProduct";
    }
    
    @GetMapping("/deleteproduct/{id}")
	public String showDeleteProductForm(@PathVariable("id") Integer id, Model model) {
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);

		return "deleteProduct";
    }
    
    @GetMapping("/editproduct/{id}")
	public String showEditProductForm(@PathVariable("id") Integer id, Model model) {
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);

        return "editProduct";
	}

	@PostMapping("/addProduct")
	public String addProduct(@Valid Product product, Model model) throws Exception {
        productService.saveProduct(product);
        
		return "redirect:/products";
    }
    
    @PostMapping("/deleteProduct")
	public String deleteProduct(@Valid Product product, Model model) throws Exception {
        productService.deleteProduct(product);
        
		return "redirect:/products";
    }

    @PostMapping("/editProduct")
    public String editProduct(@Valid Product product, Model model) throws Exception {
        productService.saveProduct(product);

		return "redirect:/products";        
    }
}
