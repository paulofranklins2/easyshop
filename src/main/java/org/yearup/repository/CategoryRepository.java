package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
