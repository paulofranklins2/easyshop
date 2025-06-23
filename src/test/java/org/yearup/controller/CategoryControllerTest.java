package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.yearup.model.Category;
import org.yearup.repository.CategoryRepository;
import org.yearup.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerTest {

    @Test
    void addCategory() {
        CategoryRepository catRepo = Mockito.mock(CategoryRepository.class);
        ProductRepository prodRepo = Mockito.mock(ProductRepository.class);
        CategoryController controller = new CategoryController(catRepo, prodRepo);
        Category c = new Category();
        Mockito.when(catRepo.save(c)).thenReturn(c);
        var response = controller.addCategory(c);
        Mockito.verify(catRepo).save(c);
        assertEquals(201, response.getStatusCodeValue());
    }
}
