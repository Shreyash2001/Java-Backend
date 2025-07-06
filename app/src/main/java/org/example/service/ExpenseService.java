package org.example.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.example.entities.Category;
import org.example.entities.Expense;
import org.example.entities.UserInfo;
import org.example.model.ExpenseSummaryResponse;
import org.example.objectmapper.ExpenseMapper;
import org.example.repository.CategoryRepository;
import org.example.repository.ExpenseRepository;
import org.example.repository.UserRepository;
import org.example.request.CreateExpenseRequest;
import org.example.response.ExpenseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
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

    public ExpenseSummaryResponse getExpenseSummary(LocalDateTime startDate, LocalDateTime endDate) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userRepository.findByUsername(username);
        List<Expense> expenses = expenseRepository.findByUser_UserIdAndDateBetween(userInfo.getUserId(), startDate, endDate);
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<String, BigDecimal> totalByCategory = new HashMap<>();
        Map<YearMonth, BigDecimal> monthlyBreakdown = new HashMap<>();

        for(Expense expense: expenses) {
            totalAmount = totalAmount.add(expense.getAmount());
            String categoryName = expense.getCategory().getName();
            totalByCategory.put(categoryName, totalByCategory.getOrDefault(categoryName, BigDecimal.ZERO).add(expense.getAmount()));
            YearMonth yearMonth = YearMonth.from(expense.getDate());
            monthlyBreakdown.put(yearMonth, monthlyBreakdown.getOrDefault(yearMonth, BigDecimal.ZERO).add(expense.getAmount()));

        }
        ExpenseSummaryResponse response = new ExpenseSummaryResponse();
        response.setTotalAmountSpent(totalAmount);
        response.setMonthlyBreakdown(monthlyBreakdown);
        response.setTotalByCategory(totalByCategory);
        return response;
    }

    public List<ExpenseResponse> searchExpense(Long categoryId, Double minAmount, Double maxAmount, LocalDateTime startDate, LocalDateTime endDate, String keyword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo = userRepository.findByUsername(username);
        List<Expense> expenses = expenseRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add((Predicate) cb.equal(root.get("user").get("userId"), userInfo.getUserId()));
            if(categoryId != null) predicates.add((Predicate) cb.equal(root.get("category").get("id"), categoryId));
            if(minAmount != null) predicates.add((Predicate) cb.ge(root.get("amount"), minAmount));
            if(maxAmount != null) predicates.add((Predicate) cb.le(root.get("amount"), maxAmount));
            if(startDate != null) predicates.add((Predicate) cb.greaterThanOrEqualTo(root.get("date"), startDate));
            if(endDate != null) predicates.add((Predicate) cb.lessThanOrEqualTo(root.get("date"), endDate));
            if(keyword != null) predicates.add((Predicate) cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%"));
            return cb.and(predicates.toArray(new Predicate[0]));
        });
        return expenses.stream().map(expenseMapper::toDto).collect(Collectors.toList());
    }
}
