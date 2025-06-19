package org.yearup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.model.Category;
import org.yearup.model.Product;
import org.yearup.repository.CategoryRepository;
import org.yearup.repository.ProductRepository;

import java.util.List;

@RestController
public class CategoriesController {
    // create an Autowired controller to inject the categoryDao and ProductDao
    private final CategoryRepository categoryDao;
    private final ProductRepository productDao;

    @Autowired
    public CategoriesController(CategoryRepository categoryDao, ProductRepository productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    // add the appropriate annotation for a get action
    @GetMapping("categories")
    public List<Category> getAll() {
        return categoryDao.getAllCategories();
    }

    // add the appropriate annotation for a get action
    @GetMapping("/categories/{id}")
    public Category getById(@PathVariable int id) {
        return categoryDao.getById(id);
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
    public Category addCategory(@RequestBody Category category) {
        return categoryDao.create(category);
    }

    // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("categorids/{id}")
    public boolean updateCategory(@PathVariable int id, @RequestBody Category category) {
        return categoryDao.update(id, category);
    }


    // add annotation to call this method for a DELETE action - the url path must include the categoryId
    // add annotation to ensure that only an ADMIN can call this function
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("categorids/{id}")
    public boolean deleteCategory(@PathVariable int id) {
        return categoryDao.delete(id);
    }
}
