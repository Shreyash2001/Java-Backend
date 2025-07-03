package org.example.repository;

import org.example.entities.Category;
import org.example.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findByUserInfo(UserInfo userInfo);
    Optional<Category> findByNameAndUserInfo_UserId(String name, String userId);
}
