package org.example.controller;

import jakarta.validation.Valid;
import org.example.entities.Expense;
import org.example.model.ExpenseSummaryResponse;
import org.example.request.CreateExpenseRequest;
import org.example.response.ExpenseResponse;
import org.example.service.ExpenseService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<List<ExpenseResponse>> getExpenses(@RequestParam(name = "categoryId", required = false) Long categoryId) {
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

    @GetMapping("/summary")
    public ResponseEntity<ExpenseSummaryResponse> getExpenseSummary(@RequestParam(name = "startDate", required = false) LocalDateTime startDate, @RequestParam(name = "endDate", required = false) LocalDateTime endDate) {
        ExpenseSummaryResponse expenseSummaryResponse = expenseService.getExpenseSummary(startDate, endDate);
        return ResponseEntity.ok(expenseSummaryResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExpenseResponse>> searchExpenses(@RequestParam(name = "categoryId", required = false) Long categoryId,
                                                            @RequestParam(name = "minAmount", required = false) Double minAmount,
                                                        @RequestParam(name = "maxAmount", required = false) Double maxAmount,
                                                        @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
                                                        @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
                                                        @RequestParam(name = "keyword", required = false) String keyword) {

        List<ExpenseResponse> searchResult = expenseService.searchExpense(categoryId, minAmount, maxAmount, startDate, endDate, keyword);
        return ResponseEntity.ok(searchResult);
    }
    @GetMapping("/search/pagination")
    public ResponseEntity<Page<ExpenseResponse>> searchExpensesPaginated(@RequestParam(name = "categoryId", required = false) Long categoryId,
                                                                @RequestParam(name = "minAmount", required = false) Double minAmount,
                                                                @RequestParam(name = "maxAmount", required = false) Double maxAmount,
                                                                @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
                                                                @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
                                                                @RequestParam(name = "keyword", required = false) String keyword,
                                                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                                                         @RequestParam(name = "size", defaultValue = "2") int size,
                                                                         @RequestParam(name = "sort", defaultValue = "date,desc") String sort) {
        String[] sortParams = sort.split(",");
        String sortfield = sortParams[0];
        Sort.Direction sortDirection = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortfield));
        Page<ExpenseResponse> searchResult = expenseService.searchExpensePaginated(categoryId, minAmount, maxAmount, startDate, endDate, keyword, pageable);
        return ResponseEntity.ok(searchResult);
    }
}
