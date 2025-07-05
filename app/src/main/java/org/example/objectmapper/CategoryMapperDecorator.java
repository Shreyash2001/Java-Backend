package org.example.objectmapper;

import org.example.entities.Category;
import org.example.response.CategoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class CategoryMapperDecorator implements CategoryMapper {
    private static final Logger logger = LoggerFactory.getLogger(CategoryMapperDecorator.class);

    @Autowired
    private CategoryMapper delegate;

    @Override
    public CategoryResponse toDto(Category category) {
        logger.debug("Starting conversion of Category to CategoryResponse: {}", category);
        try {
            if (category == null) {
                logger.warn("Category is null, returning null CategoryResponse");
                return null;
            }

            CategoryResponse response = delegate.toDto(category);
            logger.debug("Successfully converted Category to CategoryResponse: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error during Category to CategoryResponse conversion: {}", category, e);
            throw new RuntimeException("Failed to convert Category to CategoryResponse", e);
        }
    }
}