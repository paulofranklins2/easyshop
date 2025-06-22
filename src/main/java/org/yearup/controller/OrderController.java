package org.yearup.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yearup.model.*;
import org.yearup.repository.*;
import org.yearup.service.ShoppingCartService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Handles creation and retrieval of {@link Order} records for the current user.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
    private final OrderRepository orderDao;
    private final OrderLineItemRepository lineItemRepo;
    private final ShoppingCartService cartService;
    private final UserRepository userDao;
    private final ProfileRepository profileDao;

    public OrderController(OrderRepository orderDao, OrderLineItemRepository lineItemRepo, ShoppingCartService cartService,
                           UserRepository userDao, ProfileRepository profileDao) {
        this.orderDao = orderDao;
        this.lineItemRepo = lineItemRepo;
        this.cartService = cartService;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    /**
     * Convert the user's cart into a new order.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        LOG.debug("Creating order for user {}", userId);
        ShoppingCart cart = cartService.getCart(userId);
        if (cart.getItems().isEmpty()) return null;
        Profile profile = profileDao.findById(userId).orElse(null);
        Order order = new Order();
        order.setUserId(userId);
        order.setDate(LocalDateTime.now());
        if (profile != null) {
            order.setAddress(profile.getAddress());
            order.setCity(profile.getCity());
            order.setState(profile.getState());
            order.setZip(profile.getZip());
        }
        order.setShippingAmount(java.math.BigDecimal.ZERO);
        order = orderDao.save(order);
        for (ShoppingCartItem cartItem : cart.getItems().values()) {
            OrderLineItem item = new OrderLineItem();
            item.setOrderId(order.getOrderId());
            item.setProductId(cartItem.getProductId());
            item.setSalesPrice(cartItem.getProduct().getPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setDiscount(java.math.BigDecimal.ZERO);
            item.setDate(LocalDateTime.now());
            lineItemRepo.save(item);
        }
        cartService.clearCart(userId);
        order.setItems(lineItemRepo.findByOrderId(order.getOrderId()));
        order.getTotal();
        return order;
    }

    /**
     * List all orders belonging to the authenticated user.
     */
    @GetMapping
    public List<Order> listOrders(Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        LOG.debug("Listing orders for user {}", userId);
        List<Order> orders = orderDao.findByUserIdOrderByDateDesc(userId);
        for (Order order : orders) {
            order.setItems(lineItemRepo.findByOrderId(order.getOrderId()));
            order.getTotal();
        }
        return orders;
    }

    /**
     * Retrieve a single order if it belongs to the current user.
     */
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable int id, Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        LOG.debug("Retrieving order {} for user {}", id, userId);
        Order order = orderDao.findById(id).orElse(null);
        if (order != null && order.getUserId() == userId) {
            order.setItems(lineItemRepo.findByOrderId(id));
            order.getTotal();
            return order;
        }
        return null;
    }

    /**
     * Helper method to convert a {@link Principal} to a user id.
     */
    private int getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        User user = userDao.findByUsername(username);
        return user != null ? user.getId() : -1;
    }
}
