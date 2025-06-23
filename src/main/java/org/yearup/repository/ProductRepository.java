package org.yearup.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.yearup.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository providing search operations for {@link Product} entities.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE (:categoryId IS NULL OR p.categoryId = :categoryId) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:color IS NULL OR p.color = :color)")
    Page<Product> search(@Param("categoryId") Integer categoryId,
                         @Param("minPrice") BigDecimal minPrice,
                         @Param("maxPrice") BigDecimal maxPrice,
                         @Param("color") String color,
                         Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:query,'%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%',:query,'%'))")
    Page<Product> searchByQuery(@Param("query") String query, Pageable pageable);

    List<Product> findByCategoryId(int categoryId);
}
