package org.yearup.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yearup.model.*;
import org.yearup.repository.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrderRepository orderDao;
    private final ShoppingCartRepository cartDao;
    private final UserRepository userDao;
    private final ProfileRepository profileDao;

    public OrdersController(OrderRepository orderDao, ShoppingCartRepository cartDao, UserRepository userDao, ProfileRepository profileDao) {
        this.orderDao = orderDao;
        this.cartDao = cartDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        ShoppingCart cart = cartDao.getByUserId(userId);
        if (cart.getItems().isEmpty()) return null;
        Profile profile = profileDao.findById(userId);
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
        order = orderDao.create(order);
        for (ShoppingCartItem cartItem : cart.getItems().values()) {
            OrderLineItem item = new OrderLineItem();
            item.setOrderId(order.getOrderId());
            item.setProductId(cartItem.getProductId());
            item.setSalesPrice(cartItem.getProduct().getPrice());
            item.setQuantity(cartItem.getQuantity());
            item.setDiscount(java.math.BigDecimal.ZERO);
            orderDao.createLineItem(item);
        }
        cartDao.clearCart(userId);
        order.setItems(orderDao.getLineItemsByOrderId(order.getOrderId()));
        return order;
    }

    @GetMapping
    public List<Order> listOrders(Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return orderDao.listByUserId(userId);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable int id, Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        Order order = orderDao.getById(id);
        if (order != null && order.getUserId() == userId) {
            order.setItems(orderDao.getLineItemsByOrderId(id));
            return order;
        }
        return null;
    }

    private int getUserIdFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userDao.getIdByUsername(username);
    }
}