package org.yearup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.model.Category;
import org.yearup.model.Product;
import org.yearup.repository.CategoryRepository;
import org.yearup.repository.ProductRepository;

import java.net.URI;
import java.util.List;

/**
 * REST controller for CRUD operations on {@link Category} objects.
 */
@RestController
@RequestMapping("/categories")
public class CategoriesController {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoriesController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * Retrieve all categories.
     */
    @GetMapping("")
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    /**
     * Get a single category by its id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable int id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * Return all products for a particular category.
     */
    @GetMapping("/{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * Create a new category.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category createdCategory = categoryRepository.save(category);
        URI location = URI.create("/categories/" + createdCategory.getCategoryId());
        return ResponseEntity.created(location).body(createdCategory);
    }

    /**
     * Update an existing category.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category category) {
        return categoryRepository.findById(id)
                .map(existing -> {
                    category.setCategoryId(id);
                    Category saved = categoryRepository.save(category);
                    return ResponseEntity.ok(saved);
                }).orElse(ResponseEntity.notFound().build());
    }


    /**
     * Delete a category by id.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}