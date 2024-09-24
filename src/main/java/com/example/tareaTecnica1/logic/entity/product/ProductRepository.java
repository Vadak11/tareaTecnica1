package com.example.tareaTecnica1.logic.entity.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    void deleteById(Long id);
    Optional<Product> findById(Long id);
}
