package com.example.tareaTecnica1.logic.entity.category;

import com.example.tareaTecnica1.logic.entity.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "category", orphanRemoval = true)
    private Set<Product> products = new LinkedHashSet<>();

}
