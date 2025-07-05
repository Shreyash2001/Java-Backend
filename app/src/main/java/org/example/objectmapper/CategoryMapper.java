package org.example.objectmapper;

import org.example.entities.Category;
import org.example.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mapstruct.DecoratedWith;

@Mapper(componentModel = "spring")
@DecoratedWith(CategoryMapperDecorator.class)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    CategoryResponse toDto(Category category);
}