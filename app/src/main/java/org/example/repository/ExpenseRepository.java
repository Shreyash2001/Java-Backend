package org.example.repository;

import org.example.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {
    List<Expense> findByUser_UserId(String userId);
    List<Expense> findByUser_UserIdAndCategory_Id(String userId, Long categoryId);
    List<Expense> findByUser_UserIdAndDateBetween(String userId, LocalDateTime startDate, LocalDateTime endDate);
}
