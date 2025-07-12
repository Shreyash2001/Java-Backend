package org.example.config;

import org.example.entity.ExpenseCreatedEvent;
import org.example.service.CategoryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {
    @Autowired
    private CategoryService categoryService;

    @RabbitListener(queues = "expense.queue")
    public void handleExpenseCreated(ExpenseCreatedEvent expenseCreatedEvent) {
        categoryService.updateCategoryTotal(expenseCreatedEvent.getCategoryId(), expenseCreatedEvent.getAmount());
    }
}
