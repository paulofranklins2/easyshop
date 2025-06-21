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

@RestController
public class CategoriesController {
    // create an Autowired controller to inject the categoryDao and ProductDao
    private final CategoryRepository categoryDao;
    private final ProductRepository productDao;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoriesController(CategoryRepository categoryDao, ProductRepository productDao, CategoryRepository categoryRepository) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.categoryRepository = categoryRepository;
    }

    // add the appropriate annotation for a get action
    @GetMapping("categories")
    public List<Category> getAll() {
        return categoryDao.getAllCategories();
    }

    // Correct annotation is already in place for a GET action
    @GetMapping("/categories/{id}")
    public ResponseEntity<Category> getById(@PathVariable int id) {
        Category category = categoryDao.getById(id);
        if (category != null) {
            return ResponseEntity.ok(category); // 200 OK with the category data
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found if no category found
        }
    }


    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("categories/{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        return productDao.listByCategoryId(categoryId);
    }

    // add annotation to call this method for a POST action
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category createdCategory = categoryRepository.create(category);
        URI location = URI.create("/categories/" + createdCategory.getCategoryId());
        return ResponseEntity.created(location).body(createdCategory);
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("categories/{id}")
    public boolean updateCategory(@PathVariable int id, @RequestBody Category category) {
        return categoryDao.update(id, category);
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        boolean deleted = categoryDao.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
