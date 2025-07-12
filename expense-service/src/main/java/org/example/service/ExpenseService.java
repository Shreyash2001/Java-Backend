package org.example.service;

import org.example.entity.Expense;
import org.example.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Expense createExpense(Expense expense) {
        Expense savedExpense = expenseRepository.save(expense);
        rabbitTemplate.convertAndSend("expense.exchange", "expense.created", savedExpense);
        return savedExpense;
    }

    public List<Expense> getUserExpenses(String userId) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        rabbitTemplate.convertAndSend("expense.exchange", "expense.getExpenses", expenses);
        return expenses;
    }
}
