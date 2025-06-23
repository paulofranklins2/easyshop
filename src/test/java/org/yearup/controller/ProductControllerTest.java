package org.yearup.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.yearup.repository.ProductRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    @Test
    void search() {
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        ProductController controller = new ProductController(repo);

        Mockito.when(repo.search(null, null, null, null, PageRequest.of(0,12)))
            .thenReturn(new PageImpl<>(List.of()));
        assertTrue(controller.search(null,null,null,null,0,12,null).isEmpty());
    }
}
