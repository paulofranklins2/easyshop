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

    @Test
    void addProductCreatesNewEntityWhenNotExists() {
        ShoppingCartItemRepository repo = org.mockito.Mockito.mock(ShoppingCartItemRepository.class);
        ProductRepository productRepo = org.mockito.Mockito.mock(ProductRepository.class);
        org.mockito.Mockito.when(repo.findByUserId(1)).thenReturn(java.util.List.of());
        org.mockito.Mockito.when(repo.findById(new org.yearup.model.ShoppingCartItemId(1, 2)))
            .thenReturn(java.util.Optional.empty());

        ShoppingCartService svc = new ShoppingCartService(repo, productRepo);
        svc.addProduct(1,2);

        org.mockito.Mockito.verify(repo).save(org.mockito.Mockito.argThat(e -> e.getQuantity() == 1));
    }

    @Test
    void updateQuantityUpdatesExistingEntity() {
        ShoppingCartItemRepository repo = org.mockito.Mockito.mock(ShoppingCartItemRepository.class);
        ProductRepository productRepo = org.mockito.Mockito.mock(ProductRepository.class);
        org.yearup.model.ShoppingCartItemEntity entity = new org.yearup.model.ShoppingCartItemEntity();
        entity.setUserId(1); entity.setProductId(2); entity.setQuantity(1);
        org.mockito.Mockito.when(repo.findById(new org.yearup.model.ShoppingCartItemId(1,2)))
            .thenReturn(java.util.Optional.of(entity));
        org.mockito.Mockito.when(repo.findByUserId(1)).thenReturn(java.util.List.of());
        ShoppingCartService svc = new ShoppingCartService(repo, productRepo);
        svc.updateQuantity(1,2,5);
        org.mockito.Mockito.verify(repo).save(org.mockito.Mockito.argThat(e -> e.getQuantity()==5));
    }

    @Test
    void deleteProductCallsRepository() {
        ShoppingCartItemRepository repo = org.mockito.Mockito.mock(ShoppingCartItemRepository.class);
        ProductRepository productRepo = org.mockito.Mockito.mock(ProductRepository.class);
        org.mockito.Mockito.when(repo.findByUserId(1)).thenReturn(java.util.List.of());
        ShoppingCartService svc = new ShoppingCartService(repo, productRepo);
        svc.deleteProduct(1,2);
        org.mockito.Mockito.verify(repo).deleteById(new org.yearup.model.ShoppingCartItemId(1,2));
    }

    @Test
    void clearCartDeletesAll() {
        ShoppingCartItemRepository repo = org.mockito.Mockito.mock(ShoppingCartItemRepository.class);
        ProductRepository productRepo = org.mockito.Mockito.mock(ProductRepository.class);
        java.util.List<org.yearup.model.ShoppingCartItemEntity> list = java.util.List.of(new org.yearup.model.ShoppingCartItemEntity());
        org.mockito.Mockito.when(repo.findByUserId(1)).thenReturn(list);
        ShoppingCartService svc = new ShoppingCartService(repo, productRepo);
        svc.clearCart(1);
        org.mockito.Mockito.verify(repo).deleteAll(list);
    }
}
