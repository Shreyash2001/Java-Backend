package org.example.repository;

import org.example.entities.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, String> {
    List<Category> findByUserId(String userId);
    Optional<Category> findByNameAndUserId(String name, String userId);
}
