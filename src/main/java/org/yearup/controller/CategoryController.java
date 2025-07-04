package org.yearup.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class CategoryController {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * Retrieve all categories.
     */
    @GetMapping("")
    public List<Category> getAll() {
        LOG.debug("Retrieving all categories");
        return categoryRepository.findAll();
    }

    /**
     * Get a single category by its id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable int id) {
        LOG.debug("Get category id={}", id);
        return categoryRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }


    /**
     * Return all products for a particular category.
     */
    @GetMapping("/{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        LOG.debug("Get products for category id={}", categoryId);
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * Create a new category.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        LOG.debug("Adding category {}", category.getName());
        Category createdCategory = categoryRepository.save(category);
        URI location = URI.create("/categories/" + createdCategory.getCategoryId());
        return ResponseEntity.created(location).body(createdCategory);
    }

    /**
     * Update an existing category.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category category) {
        LOG.debug("Updating category id={}", id);
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
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        LOG.debug("Deleting category id={}", id);
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
