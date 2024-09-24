package com.example.tareaTecnica1.logic.entity.product;

import com.example.tareaTecnica1.logic.entity.category.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
