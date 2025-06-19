package org.yearup.repository;

import org.yearup.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository {
    List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String color, int offset, int limit);

    List<Product> searchByQuery(String query, int offset, int limit);

    List<Product> listPaged(int offset, int limit);

    List<Product> listByCategoryId(int categoryId);

    Product getById(int productId);

    Product create(Product product);

    void update(int productId, Product product);

    void delete(int productId);
}