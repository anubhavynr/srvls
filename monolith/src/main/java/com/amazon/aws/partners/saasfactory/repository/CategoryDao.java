package com.amazon.aws.partners.saasfactory.repository;

import com.amazon.aws.partners.saasfactory.domain.Category;

import java.util.List;

public interface CategoryDao {

    public Category getCategory(Integer categoryId) throws Exception;

    public Category getCategoryByName(String name) throws Exception;

    public List<Category> getCategories() throws Exception;

    public Category saveCategory(Category category) throws Exception;

    public void deleteCategory(Category category) throws Exception;

}
