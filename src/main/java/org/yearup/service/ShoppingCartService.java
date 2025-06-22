package org.yearup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.yearup.model.*;
import org.yearup.repository.ProductRepository;
import org.yearup.repository.ShoppingCartItemRepository;

import java.util.List;

/**
 * Business logic for manipulating shopping carts persisted in the database.
 */
@Service
public class ShoppingCartService {
    private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartService.class);
    private final ShoppingCartItemRepository cartRepo;
    private final ProductRepository productRepository;

    public ShoppingCartService(ShoppingCartItemRepository cartRepo, ProductRepository productRepository) {
        this.cartRepo = cartRepo;
        this.productRepository = productRepository;
    }

    /**
     * Retrieve a user's cart from the database.
     */
    public ShoppingCart getCart(int userId) {
        LOG.debug("Getting cart for user {}", userId);
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

    /**
     * Add a product to the user's cart, incrementing quantity if present.
     */
    public ShoppingCart addProduct(int userId, int productId) {
        LOG.debug("Adding product {} to cart for user {}", productId, userId);
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

    /**
     * Update the quantity of a product in the cart.
     */
    public ShoppingCart updateQuantity(int userId, int productId, int quantity) {
        LOG.debug("Updating product {} quantity to {} for user {}", productId, quantity, userId);
        ShoppingCartItemEntity entity = cartRepo.findById(new ShoppingCartItemId(userId, productId)).orElse(null);
        if (entity != null) {
            entity.setQuantity(quantity);
            cartRepo.save(entity);
        }
        return getCart(userId);
    }

    /**
     * Remove a product from the cart.
     */
    public ShoppingCart deleteProduct(int userId, int productId) {
        LOG.debug("Deleting product {} from cart for user {}", productId, userId);
        cartRepo.deleteById(new ShoppingCartItemId(userId, productId));
        return getCart(userId);
    }

    /**
     * Remove all items from a user's cart.
     */
    public ShoppingCart clearCart(int userId) {
        LOG.debug("Clearing cart for user {}", userId);
        List<ShoppingCartItemEntity> items = cartRepo.findByUserId(userId);
        cartRepo.deleteAll(items);
        return getCart(userId);
    }
}
