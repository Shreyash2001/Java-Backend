package org.example.objectmapper;

import org.example.entities.Expense;
import org.example.response.ExpenseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "createdAt", target = "createdAt")
    ExpenseResponse toDto(Expense expense);
}
