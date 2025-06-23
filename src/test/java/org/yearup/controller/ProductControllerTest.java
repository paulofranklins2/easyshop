package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.yearup.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    @Test
    void search() {
        ProductRepository repo = org.mockito.Mockito.mock(ProductRepository.class);
        ProductController controller = new ProductController(repo);
        org.mockito.Mockito.when(repo.search(null, null, null, null, org.springframework.data.domain.PageRequest.of(0,25)))
            .thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.List.of()));
        assertTrue(controller.search(null,null,null,null,0,25,null).isEmpty());
    }
}
