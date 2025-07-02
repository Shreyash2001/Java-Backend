package org.example.controller;

import jakarta.validation.Valid;
import org.example.request.CreateExpenseRequest;
import org.example.response.ExpenseResponse;
import org.example.service.ExpenseService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private static  final Logger logger = (Logger) LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody CreateExpenseRequest request) {
        logger.info("Received request to create expense");
        ExpenseResponse response = expenseService.createExpense(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpenses(@RequestParam(required = false) String categoryId) {
        logger.info("Received request to get expenses");
        List<ExpenseResponse> expenses = expenseService.getExpensesByUser(categoryId);
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        logger.info("Received request to delete expense");
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
