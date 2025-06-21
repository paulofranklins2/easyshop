package org.yearup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.model.Product;
import org.yearup.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductsController {
    private final ProductRepository productDao;

    @Autowired
    public ProductsController(ProductRepository productDao) {
        this.productDao = productDao;
    }

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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Product getById(@PathVariable int id) {
        try {
            var product = productDao.findById(id).orElse(null);

            if (product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return product;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product addProduct(@RequestBody Product product) {
        try {
            return productDao.save(product);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateProduct(@PathVariable int id, @RequestBody Product product) {
        try {
            product.setProductId(id);
            productDao.save(product);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProduct(@PathVariable int id) {
        try {
            var product = productDao.findById(id).orElse(null);

            if (product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            productDao.deleteById(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
