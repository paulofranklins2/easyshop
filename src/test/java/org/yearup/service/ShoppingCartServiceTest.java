package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.yearup.repository.ProductRepository;
import org.yearup.repository.ShoppingCartItemRepository;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartServiceTest {

    @Test
    void basicFlow() {
        ShoppingCartItemRepository repo = org.mockito.Mockito.mock(ShoppingCartItemRepository.class);
        ProductRepository productRepo = org.mockito.Mockito.mock(ProductRepository.class);
        org.mockito.Mockito.when(repo.findByUserId(1)).thenReturn(java.util.List.of());
        ShoppingCartService svc = new ShoppingCartService(repo, productRepo);
        assertNotNull(svc.getCart(1));
    }
}
