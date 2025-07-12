package org.example.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queue for created expenses
    @Bean
    public Queue expenseCreatedQueue() {
        return new Queue("expense.created.queue", true);
    }

    // Queue for fetching expenses
    @Bean
    public Queue expenseGetExpensesQueue() {
        return new Queue("expense.getExpenses.queue", true);
    }

    // Exchange
    @Bean
    public TopicExchange expenseExchange() {
        return new TopicExchange("expense.exchange");
    }

    // Binding for "expense.created"
    @Bean
    public Binding createdBinding(Queue expenseCreatedQueue, TopicExchange expenseExchange) {
        return BindingBuilder
                .bind(expenseCreatedQueue)
                .to(expenseExchange)
                .with("expense.created");
    }

    // Binding for "expense.getExpenses"
    @Bean
    public Binding getExpensesBinding(Queue expenseGetExpensesQueue, TopicExchange expenseExchange) {
        return BindingBuilder
                .bind(expenseGetExpensesQueue)
                .to(expenseExchange)
                .with("expense.getExpenses");
    }
}
