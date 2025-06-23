package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.yearup.model.ShoppingCartItemEntity;
import org.yearup.model.ShoppingCartItemId;
import org.yearup.repository.ProductRepository;
import org.yearup.repository.ShoppingCartItemRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ShoppingCartServiceTest {

    @Test
    void basicFlow() {
        ShoppingCartItemRepository repo = Mockito.mock(ShoppingCartItemRepository.class);
        ProductRepository productRepo = Mockito.mock(ProductRepository.class);
        Mockito.when(repo.findByUserId(1)).thenReturn(List.of());
        ShoppingCartService svc = new ShoppingCartService(repo, productRepo);
        assertNotNull(svc.getCart(1));
    }

    @Test
    void addProductCreatesNewEntityWhenNotExists() {
        ShoppingCartItemRepository repo = Mockito.mock(ShoppingCartItemRepository.class);
        ProductRepository productRepo = Mockito.mock(ProductRepository.class);
        Mockito.when(repo.findByUserId(1)).thenReturn(List.of());
        Mockito.when(repo.findById(new ShoppingCartItemId(1, 2)))
            .thenReturn(java.util.Optional.empty());

        ShoppingCartService svc = new ShoppingCartService(repo, productRepo);
        svc.addProduct(1, 2);

        Mockito.verify(repo).save(Mockito.argThat(e -> e.getQuantity() == 1));
    }

    @Test
    void updateQuantityUpdatesExistingEntity() {
        ShoppingCartItemRepository repo = Mockito.mock(ShoppingCartItemRepository.class);
        ProductRepository productRepo = Mockito.mock(ProductRepository.class);
        ShoppingCartItemEntity entity = new ShoppingCartItemEntity();
        entity.setUserId(1);
        entity.setProductId(2);
        entity.setQuantity(1);
        Mockito.when(repo.findById(new ShoppingCartItemId(1, 2)))
            .thenReturn(java.util.Optional.of(entity));
        Mockito.when(repo.findByUserId(1)).thenReturn(List.of());
        ShoppingCartService svc = new ShoppingCartService(repo, productRepo);
        svc.updateQuantity(1, 2, 5);
        Mockito.verify(repo).save(Mockito.argThat(e -> e.getQuantity() == 5));
    }

    @Test
    void deleteProductCallsRepository() {
        ShoppingCartItemRepository repo = Mockito.mock(ShoppingCartItemRepository.class);
        ProductRepository productRepo = Mockito.mock(ProductRepository.class);
        Mockito.when(repo.findByUserId(1)).thenReturn(List.of());
        ShoppingCartService svc = new ShoppingCartService(repo, productRepo);
        svc.deleteProduct(1, 2);
        Mockito.verify(repo).deleteById(new ShoppingCartItemId(1, 2));
    }

    @Test
    void clearCartDeletesAll() {
        ShoppingCartItemRepository repo = Mockito.mock(ShoppingCartItemRepository.class);
        ProductRepository productRepo = Mockito.mock(ProductRepository.class);
        List<ShoppingCartItemEntity> list = List.of(new ShoppingCartItemEntity());
        Mockito.when(repo.findByUserId(1)).thenReturn(list);
        ShoppingCartService svc = new ShoppingCartService(repo, productRepo);
        svc.clearCart(1);
        Mockito.verify(repo).deleteAll(list);
    }
}
