package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategory(){
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<String> createCategory(@Valid @RequestBody Category category){
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category created successfully!", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        boolean isDeleted = categoryService.deleteCategory(categoryId);
        if(isDeleted) {
            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId, @RequestBody Category category){
        boolean isUpdated = categoryService.updateCategory(categoryId, category);
        if(isUpdated){
            return new ResponseEntity<>("Category updated successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
    }

}
