package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
    }

    @Override
    public boolean deleteCategory(Long categoryId) {
        for(Category category: categories){
            if(category.getCategoryId().equals(categoryId)){
                categories.remove(category);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateCategory(Long categoryId, Category category) {
        for(Category categoryIt: categories){
            if (categoryIt.getCategoryId().equals(categoryId)){
                categoryIt.setCategoryName(category.getCategoryName());
                return true;
            }
        }
        return false;
    }
}
