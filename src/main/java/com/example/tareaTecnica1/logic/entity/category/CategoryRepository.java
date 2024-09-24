package com.example.tareaTecnica1.logic.entity.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    void deleteById(Long id);
    Optional<Category> findById(Long id);
}
