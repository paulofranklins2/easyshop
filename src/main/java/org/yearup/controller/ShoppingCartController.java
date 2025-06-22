package org.yearup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.model.ShoppingCart;
import org.yearup.model.User;
import org.yearup.model.dto.cart.UpdateCartItemRequest;
import org.yearup.repository.UserRepository;
import org.yearup.service.ShoppingCartService;

import java.security.Principal;

/**
 * REST controller for managing a user's shopping cart.
 */
@RestController
public class ShoppingCartController {
    // a shopping cart requires
    private final ShoppingCartService shoppingCartDao;
    private final UserRepository userDao;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartDao, UserRepository userDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
    }

    /**
     * Retrieve the current user's shopping cart.
     */
    @GetMapping("/cart")
    public ShoppingCart getCart(Principal principal) {
        try {
            int userId = getUserIdFromPrincipal(principal);
            return shoppingCartDao.getCart(userId);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error.");
        }
    }

    /**
     * Add a product to the user's cart.
     */
    @PostMapping("/cart/products/{productId}")
    public ShoppingCart addToCart(@PathVariable int productId, Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return shoppingCartDao.addProduct(userId, productId);
    }

    /**
     * Update the quantity of a product in the cart.
     */
    @PutMapping("/cart/products/{productId}")
    public ShoppingCart updateCartQuantity(@PathVariable int productId,
                                           @RequestBody UpdateCartItemRequest item,
                                           Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return shoppingCartDao.updateQuantity(userId, productId, item.getQuantity());
    }

    /**
     * Remove a product from the cart.
     */
    @DeleteMapping("/cart/products/{productId}")
    public ShoppingCart deleteProduct(@PathVariable int productId, Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return shoppingCartDao.deleteProduct(userId, productId);
    }

    /**
     * Remove all items from the cart.
     */
    @DeleteMapping("/cart")
    public ShoppingCart clearCart(Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return shoppingCartDao.clearCart(userId);
    }

    /**
     * Utility method to get the current user's id from the {@link Principal}.
     */
    private int getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userDao.findByUsername(username);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        return user.getId();
    }
}
