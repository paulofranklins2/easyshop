package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.model.Category;

/**
 * JPA repository for {@link Category} entities.
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}