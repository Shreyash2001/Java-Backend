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
import java.util.logging.Logger;
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

}
