package org.example.objectmapper;

import org.example.entities.Expense;
import org.example.response.ExpenseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);
    ExpenseResponse toDto(Expense expense);
}
