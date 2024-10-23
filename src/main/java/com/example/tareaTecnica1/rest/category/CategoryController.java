package com.example.tareaTecnica1.rest.category;

import com.example.tareaTecnica1.logic.entity.category.Category;
import com.example.tareaTecnica1.logic.entity.category.CategoryRepository;
import com.example.tareaTecnica1.logic.entity.http.GlobalResponseHandler;
import com.example.tareaTecnica1.logic.entity.http.Meta;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/categories")
@RestController
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Category> productsPage = categoryRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(productsPage.getTotalPages());
        meta.setTotalElements(productsPage.getTotalElements());
        meta.setPageNumber(productsPage.getNumber() + 1);
        meta.setPageSize(productsPage.getSize());

        return new GlobalResponseHandler().handleResponse("Categories retrieved successfully",
                productsPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public Category addCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(category.getName());
                    existingCategory.setDescription(category.getDescription());
                    return categoryRepository.save(existingCategory);
                })
                .orElseGet(() -> {
                    category.setId(id);
                    return categoryRepository.save(category);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public Category deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        categoryRepository.delete(category);
        return category;
    }
}
