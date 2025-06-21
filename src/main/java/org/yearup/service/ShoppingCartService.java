package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.model.*;
import org.yearup.repository.ProductRepository;
import org.yearup.repository.ShoppingCartItemRepository;

import java.util.List;

@Service
public class ShoppingCartService {
    private final ShoppingCartItemRepository cartRepo;
    private final ProductRepository productRepository;

    public ShoppingCartService(ShoppingCartItemRepository cartRepo, ProductRepository productRepository) {
        this.cartRepo = cartRepo;
        this.productRepository = productRepository;
    }

    public ShoppingCart getCart(int userId) {
        List<ShoppingCartItemEntity> items = cartRepo.findByUserId(userId);
        ShoppingCart cart = new ShoppingCart();
        for (ShoppingCartItemEntity entity : items) {
            Product product = productRepository.findById(entity.getProductId()).orElse(null);
            if (product == null) continue;
            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(entity.getQuantity());
            cart.add(item);
        }
        return cart;
    }

    public ShoppingCart addProduct(int userId, int productId) {
        ShoppingCartItemEntity id = cartRepo.findById(new ShoppingCartItemId(userId, productId)).orElse(null);
        if (id == null) {
            ShoppingCartItemEntity entity = new ShoppingCartItemEntity();
            entity.setUserId(userId);
            entity.setProductId(productId);
            entity.setQuantity(1);
            cartRepo.save(entity);
        } else {
            id.setQuantity(id.getQuantity() + 1);
            cartRepo.save(id);
        }
        return getCart(userId);
    }

    public ShoppingCart updateQuantity(int userId, int productId, int quantity) {
        ShoppingCartItemEntity entity = cartRepo.findById(new ShoppingCartItemId(userId, productId)).orElse(null);
        if (entity != null) {
            entity.setQuantity(quantity);
            cartRepo.save(entity);
        }
        return getCart(userId);
    }

    public ShoppingCart deleteProduct(int userId, int productId) {
        cartRepo.deleteById(new ShoppingCartItemId(userId, productId));
        return getCart(userId);
    }

    public ShoppingCart clearCart(int userId) {
        List<ShoppingCartItemEntity> items = cartRepo.findByUserId(userId);
        cartRepo.deleteAll(items);
        return getCart(userId);
    }
}
