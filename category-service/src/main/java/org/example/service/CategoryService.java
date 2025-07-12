package org.example.service;

import org.example.entity.Category;
import org.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void updateCategoryTotal(String categoryId, double amount) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setTotalAmount(category.getTotalAmount() + amount);
        categoryRepository.save(category);
    }
}
