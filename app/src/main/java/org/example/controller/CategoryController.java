package org.example.controller;

import jakarta.validation.Valid;
import org.example.request.CreateCategoryRequest;
import org.example.response.CategoryResponse;
import org.example.service.CategoryService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        logger.info("Received request to create category");
        CategoryResponse categoryResponse = categoryService.createCategory(request);
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategory() {
        logger.info("Received request to get categories");
        List<CategoryResponse> categoryResponse = categoryService.getCategoriesByUser();
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable String id) {
        logger.info("Received request to fetch category by id" + id);
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable String id, @Valid @RequestBody CreateCategoryRequest request) {
        logger.info("Received request to update category by id" + id);
        CategoryResponse categoryResponse = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(categoryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        logger.info("Received request to delete category by id" + id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
