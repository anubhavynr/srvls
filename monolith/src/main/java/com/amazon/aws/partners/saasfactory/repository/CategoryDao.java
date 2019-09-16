package com.amazon.aws.partners.saasfactory.repository;

import com.amazon.aws.partners.saasfactory.domain.Category;

import java.util.List;

public interface CategoryDao {

    public Category getCategory(Long categoryId) throws Exception;

    public List<Category> getCategories() throws Exception;

    public Category saveCategory(Category category) throws Exception;

    public void deleteCategory(Category category) throws Exception;

}
