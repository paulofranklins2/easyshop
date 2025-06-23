package org.yearup.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.exception.InternalServerErrorException;
import org.yearup.exception.NotFoundException;
import org.yearup.model.Product;
import org.yearup.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller exposing product search and management endpoints.
 */
@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductController {
    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    private final ProductRepository productDao;

    @Autowired
    public ProductController(ProductRepository productDao) {
        this.productDao = productDao;
    }

    /**
     * Search for products using optional filters.
     */
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Product> search(@RequestParam(name = "cat", required = false) Integer categoryId,
                                @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
                                @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
                                @RequestParam(name = "color", required = false) String color,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "25") int size,
                                @RequestParam(name = "q", required = false) String query
    ) {
        LOG.debug("Searching products cat={}, minPrice={}, maxPrice={}, color={}, page={}, size={}, q={}",
            categoryId, minPrice, maxPrice, color, page, size, query);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> result;
            if (query != null && !query.isBlank()) {
                result = productDao.searchByQuery(query, pageable);
            } else {
                result = productDao.search(categoryId, minPrice, maxPrice, color, pageable);
            }
            return result.getContent();
        } catch (Exception ex) {
            LOG.error("Error searching products", ex);
            throw new InternalServerErrorException("Oops... our bad.", ex);
        }
    }

    /**
     * Retrieve a product by id.
     */
    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Product getById(@PathVariable int id) {
        LOG.debug("Getting product id={}", id);
        try {
            var product = productDao.findById(id).orElse(null);

            if (product == null)
                throw new NotFoundException("Product not found");

            return product;
        } catch (Exception ex) {
            LOG.error("Error getting product {}", id, ex);
            throw new InternalServerErrorException("Oops... our bad.", ex);
        }
    }

    /**
     * Create a new product.
     */
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product addProduct(@RequestBody Product product) {
        LOG.debug("Adding product {}", product.getName());
        try {
            return productDao.save(product);
        } catch (Exception ex) {
            LOG.error("Error adding product", ex);
            throw new InternalServerErrorException("Oops... our bad.", ex);
        }
    }

    /**
     * Update an existing product.
     */
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateProduct(@PathVariable int id, @RequestBody Product product) {
        LOG.debug("Updating product id={}", id);
        try {
            product.setProductId(id);
            productDao.save(product);
        } catch (Exception ex) {
            LOG.error("Error updating product {}", id, ex);
            throw new InternalServerErrorException("Oops... our bad.", ex);
        }
    }

    /**
     * Delete a product by id.
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProduct(@PathVariable int id) {
        LOG.debug("Deleting product id={}", id);
        try {
            var product = productDao.findById(id).orElse(null);

            if (product == null)
                throw new NotFoundException("Product not found");

            productDao.deleteById(id);
        } catch (Exception ex) {
            LOG.error("Error deleting product {}", id, ex);
            throw new InternalServerErrorException("Oops... our bad.", ex);
        }
    }
}
