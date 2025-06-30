package org.example.service;

import org.apache.catalina.User;
import org.example.entities.Expense;
import org.example.entities.UserInfo;
import org.example.repository.ExpenseRepository;
import org.example.repository.UserRepository;
import org.example.request.CreateExpenseRequest;
import org.example.response.ExpenseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public ExpenseResponse createExpense(CreateExpenseRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo user = userRepository.findByUsername(username);
        Category category = categoryRepository.findById(request.getCategoryId());

        if(!category.getUser().getId().equals(user.getUserId())) {
            throw new RuntimeException("Category does not belong to user");
        }

        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(category);
        expense.setUser(user);

        Expense savedExpense = expenseRepository.save(expense);

        return mapToResponse(savedExpense);
    }

    private ExpenseResponse mapToResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setDate(expense.getDate());
        response.setCategoryName(expense.getCategory().getName());
        response.setCreatedAt(expense.getCreatedAt());
        return response;
    }
}
