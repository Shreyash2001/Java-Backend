package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.example"})
@EnableJpaRepositories(basePackages = {"org.example.repository"})
public class CategoryMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(CategoryMainApplication.class, args);
    }
}