package com.amazon.aws.partners.saasfactory.controller;

import com.amazon.aws.partners.saasfactory.domain.Category;
import com.amazon.aws.partners.saasfactory.domain.Product;
import com.amazon.aws.partners.saasfactory.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.beans.PropertyEditorSupport;
import java.util.List;

@Controller
public class ProductsController {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProductsController.class);

	@Autowired
	private ProductService productService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Category.class, new CategoryEditor());
	}

	@GetMapping("/products")
	public String getProducts(Model model) throws Exception {
		List<Category> categories = productService.getCategories();
		List<Product> products = productService.getProducts();
		model.addAttribute("categories", categories);
		model.addAttribute("products", products);
		return "products";
	}

	@PostMapping("/products")
	public String insertProduct(@ModelAttribute Product product, BindingResult bindingResult, Model model) throws Exception {
		LOGGER.info("ProductsController::insertProduct " + product);
		if (bindingResult.hasErrors()) {

		}
		model.addAttribute("product", product);

		product = productService.saveProduct(product);
		return "redirect:/products";
	}

	class CategoryEditor extends PropertyEditorSupport {
		@Override
		public String getAsText() {
			Category category = (Category) getValue();
			return category == null ? "" : category.getId().toString();
		}

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			Category category = null;
			try {
				//category = productService.getCategoryByName(text);
				category = productService.getCategory(Integer.valueOf(text));
			} catch (Exception e) {
				//LOGGER.error("Can't look up category by name " + text);
				LOGGER.error("Can't look up category by id " + text);
			}
			setValue(category);
		}
	}
}
