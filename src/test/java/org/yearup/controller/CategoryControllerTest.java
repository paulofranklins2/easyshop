package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.yearup.model.Category;
import org.yearup.repository.CategoryRepository;
import org.yearup.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerTest {

    @Test
    void addCategory() {
        CategoryRepository catRepo = org.mockito.Mockito.mock(CategoryRepository.class);
        ProductRepository prodRepo = org.mockito.Mockito.mock(ProductRepository.class);
        CategoryController controller = new CategoryController(catRepo, prodRepo);
        Category c = new Category();
        org.mockito.Mockito.when(catRepo.save(c)).thenReturn(c);
        var response = controller.addCategory(c);
        org.mockito.Mockito.verify(catRepo).save(c);
        assertEquals(201, response.getStatusCodeValue());
    }
}
