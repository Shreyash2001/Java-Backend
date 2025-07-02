package org.example.service;

import jakarta.transaction.Transactional;
import org.example.entities.Category;
import org.example.entities.UserInfo;
import org.example.objectmapper.CategoryMapper;
import org.example.repository.CategoryRepository;
import org.example.repository.UserRepository;
import org.example.request.CreateCategoryRequest;
import org.example.response.CategoryResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        logger.info("Creating Category: {}");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo user = userRepository.findByUsername(username);
        categoryRepository.findByNameAndUserId(request.getName(), user.getUserId()).ifPresent(category -> {
            throw new RuntimeException("Category name already exists");
        });

        Category category = new Category();
        category.setName(request.getName());
        category.setUserInfo(user);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

        Category savedCategory = categoryRepository.save(category);
        logger.info("Category Created");
        return categoryMapper.toDto(savedCategory);
    }

    public List<CategoryResponse> getCategoriesByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userRepository.findByUsername((username));
        logger.info("Fetching categories");
        return categoryRepository.findByUserId(userInfo.getUserId()).stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    public CategoryResponse getCategoryById(String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userRepository.findByUsername(username);
        Optional<Category> category = categoryRepository.findById(id);
        if(!category.isPresent()) {
            throw new RuntimeException("Category not found!!");
        }
        Category categoryObj = category.get();
        if(!categoryObj.getUserInfo().getUserId().equals(userInfo.getUserId())) {
            logger.warn("Unautherized access to category");
            throw new RuntimeException("Unauthorized");
        }

        logger.info("Fetched category with ID");
        return categoryMapper.toDto(categoryObj);

    }

    @Transactional
    public CategoryResponse updateCategory(String id, CreateCategoryRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userRepository.findByUsername(username);

        Optional<Category> category = categoryRepository.findById(id);
        if(!category.isPresent()) {
            throw new RuntimeException("Category not found!!");
        }
        Category categoryObj = category.get();
        categoryRepository.findByNameAndUserId(request.getName(), userInfo.getUserId()).filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    logger.warn("Category name already exist");
                    throw new RuntimeException("Category name already exist");
                });

        categoryObj.setName(request.getName());
        categoryObj.setUpdatedAt(LocalDateTime.now());

        Category updatedCategory = categoryRepository.save(categoryObj);
        return categoryMapper.toDto(updatedCategory);
    }

    @Transactional
    public void deleteCategory(String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userRepository.findByUsername(username);
        Optional<Category> category = categoryRepository.findById(id);
        if(!category.isPresent()) {
            throw new RuntimeException("Category not found!!!");
        }
        Category categoryObj = category.get();
        if(!categoryObj.getUserInfo().getUserId().equals(userInfo.getUserId())) {
            logger.warn("Unauthorized delete attempted!!!");
            throw new RuntimeException("Unauthorized");
        }
        categoryRepository.deleteById(id);
        logger.info("Category deleted with ID" + id);
    }

}
