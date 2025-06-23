package org.yearup.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yearup.dto.cart.UpdateCartItemRequest;
import org.yearup.exception.ApplicationException;
import org.yearup.exception.InternalServerErrorException;
import org.yearup.exception.NotFoundException;
import org.yearup.exception.UnauthorizedException;
import org.yearup.model.PromoCode;
import org.yearup.model.ShoppingCart;
import org.yearup.model.User;
import org.yearup.repository.UserRepository;
import org.yearup.service.PromoCodeService;
import org.yearup.service.ShoppingCartService;

import java.security.Principal;

/**
 * REST controller for managing a user's shopping cart.
 */
@RestController
public class ShoppingCartController {
    private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartController.class);
    private final ShoppingCartService shoppingCartService;
    private final UserRepository userDao;
    private final PromoCodeService promoService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartDao, UserRepository userDao, PromoCodeService promoService) {
        this.shoppingCartService = shoppingCartDao;
        this.userDao = userDao;
        this.promoService = promoService;
    }

    /**
     * Retrieve the current user's shopping cart.
     */
    @GetMapping("/cart")
    public ShoppingCart getCart(Principal principal) {
        try {
            int userId = getUserIdFromPrincipal(principal);
            LOG.debug("Retrieving cart for user {}", userId);
            return shoppingCartService.getCart(userId);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Error getting cart", e);
            throw new InternalServerErrorException("Unexpected error.", e);
        }
    }

    /**
     * Add a product to the user's cart.
     */
    @PostMapping("/cart/products/{productId}")
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        LOG.debug("Adding product {} to cart for user {}", productId, userId);
        return shoppingCartService.addProduct(userId, productId);
    }

    /**
     * Update the quantity of a product in the cart.
     */
    @PutMapping("/cart/products/{productId}")
    public ShoppingCart updateCartQuantity(@PathVariable int productId,
                                           @RequestBody UpdateCartItemRequest item,
                                           Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        LOG.debug("Updating product {} quantity to {} for user {}", productId, item.getQuantity(), userId);
        return shoppingCartService.updateQuantity(userId, productId, item.getQuantity());
    }

    /**
     * Remove a product from the cart.
     */
    @DeleteMapping("/cart/products/{productId}")
    public ShoppingCart deleteProduct(@PathVariable int productId, Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        LOG.debug("Deleting product {} from cart for user {}", productId, userId);
        return shoppingCartService.deleteProduct(userId, productId);
    }

    /**
     * Remove all items from the cart.
     */
    @DeleteMapping("/cart")
    public ShoppingCart clearCart(Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        LOG.debug("Clearing cart for user {}", userId);
        return shoppingCartService.clearCart(userId);
    }

    /**
     * Apply a promo code to the cart.
     */
    @PostMapping("/cart/promo/{code}")
    public ShoppingCart applyPromo(@PathVariable String code, Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        PromoCode promo = promoService.findByCode(code);
        if (promo != null) {
            return shoppingCartService.applyDiscount(userId, promo.getDiscountPercent());
        }
        return shoppingCartService.getCart(userId);
    }

    /**
     * Utility method to get the current user's id from the {@link Principal}.
     */
    private int getUserIdFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Authentication required");
        }
        String username = principal.getName();
        User user = userDao.findByUsername(username);
        if (user == null) throw new NotFoundException("User not found");
        return user.getId();
    }
}
