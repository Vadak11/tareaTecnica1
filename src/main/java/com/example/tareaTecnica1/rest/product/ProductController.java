package com.example.tareaTecnica1.rest.product;

import com.example.tareaTecnica1.logic.entity.category.Category;
import com.example.tareaTecnica1.logic.entity.category.CategoryRepository;
import com.example.tareaTecnica1.logic.entity.http.GlobalResponseHandler;
import com.example.tareaTecnica1.logic.entity.http.Meta;
import com.example.tareaTecnica1.logic.entity.product.Product;
import com.example.tareaTecnica1.logic.entity.product.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/products")
@RestController
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page-1, size);
        Page<Product> productsPage = productRepository.findAll(pageable);
        Meta meta = new Meta(request.getMethod(), request.getRequestURL().toString());
        meta.setTotalPages(productsPage.getTotalPages());
        meta.setTotalElements(productsPage.getTotalElements());
        meta.setPageNumber(productsPage.getNumber() + 1);
        meta.setPageSize(productsPage.getSize());

        return new GlobalResponseHandler().handleResponse("Products retrieved successfully",
                productsPage.getContent(), HttpStatus.OK, meta);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public ResponseEntity<String> addProduct(@RequestBody Product productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategory().getId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body("Categoría no encontrada");
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setCategory(category);

        productRepository.save(product);
        return ResponseEntity.ok("Producto creado exitosamente: ID:" + product.getId());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setStock(product.getStock());
                    existingProduct.setCategory(product.getCategory());
                    return productRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    product.setId(id);
                    return productRepository.save(product);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN_ROLE')")
    public Product deleteProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        productRepository.delete(product);
        return product;
    }

}
