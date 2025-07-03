package org.example.service;

import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.example.entities.Category;
import org.example.entities.Expense;
import org.example.entities.UserInfo;
import org.example.objectmapper.ExpenseMapper;
import org.example.repository.CategoryRepository;
import org.example.repository.ExpenseRepository;
import org.example.repository.UserRepository;
import org.example.request.CreateExpenseRequest;
import org.example.response.ExpenseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseMapper expenseMapper;

    @Transactional
    public ExpenseResponse createExpense(CreateExpenseRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo user = userRepository.findByUsername(username);
        Optional<Category> category = categoryRepository.findById(request.getCategoryId());

        if(category.isEmpty()) {
            throw new RuntimeException("Category is not present");
        }
        Category categoryObj = category.get();
        if(!categoryObj.getUserInfo().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Category does not belong to user");
        }

        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(categoryObj);
        expense.setUser(user);

        Expense savedExpense = expenseRepository.save(expense);

        return expenseMapper.toDto(savedExpense);
    }

    public List<ExpenseResponse> getExpensesByUser(Long categoryId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userRepository.findByUsername(username);
        List<Expense> expenses = expenseRepository.findByUser_UserIdAndCategory_Id(userInfo.getUserId(), categoryId);
        return expenses.stream().map(expenseMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void deleteExpense(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userRepository.findByUsername(username);
        Optional<Expense> expense = expenseRepository.findById(id);
        if(expense.isEmpty()) throw new RuntimeException("Expense deleted with ID" + id);
        expenseRepository.deleteById(id);
    }
}
