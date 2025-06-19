package org.yearup.repository;

import org.yearup.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> getAllCategories();

    Category getById(int categoryId);

    Category create(Category category);

    boolean update(int categoryId, Category category);

    boolean delete(int categoryId);
}
