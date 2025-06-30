package org.example.repository;

import org.example.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findByUserId(String userId);
    Optional<Category> findByNameAndUserId(String name, String userId);
}
