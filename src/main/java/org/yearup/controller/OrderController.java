package org.yearup.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yearup.exception.BadRequestException;
import org.yearup.exception.UnauthorizedException;
import org.yearup.model.*;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;
import org.yearup.repository.ProfileRepository;
import org.yearup.repository.UserRepository;
import org.yearup.service.InvoiceService;
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
    private final InvoiceService invoiceService;

    public OrderController(OrderRepository orderDao, OrderLineItemRepository lineItemRepo, ShoppingCartService cartService,
                           UserRepository userDao, ProfileRepository profileDao, InvoiceService invoiceService) {
        this.orderDao = orderDao;
        this.lineItemRepo = lineItemRepo;
        this.cartService = cartService;
        this.userDao = userDao;
        this.profileDao = profileDao;
        this.invoiceService = invoiceService;
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
        if (cart.getItems().isEmpty())
            throw new BadRequestException("Cart is empty");
        java.math.BigDecimal discountPercent = cartService.getDiscountPercent(userId);
        String promoCode = cartService.getPromoCode(userId);
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
        order.setPromoCode(promoCode);
        order.setDiscountPercent(discountPercent);
        order = orderDao.save(order);
        for (ShoppingCartItem cartItem : cart.getItems().values()) {
            OrderLineItem item = new OrderLineItem();
            item.setOrderId(order.getOrderId());
            item.setProductId(cartItem.getProductId());
            item.setSalesPrice(cartItem.getProduct().getPrice());
            item.setQuantity(cartItem.getQuantity());
            java.math.BigDecimal lineTotal = cartItem.getProduct().getPrice().multiply(new java.math.BigDecimal(cartItem.getQuantity()));
            java.math.BigDecimal lineDiscount = lineTotal.multiply(discountPercent == null ? java.math.BigDecimal.ZERO : discountPercent);
            item.setDiscount(lineDiscount);
            item.setDate(LocalDateTime.now());
            lineItemRepo.save(item);
        }
        cartService.clearCart(userId);
        order.setItems(lineItemRepo.findByOrderId(order.getOrderId()));
        order.getTotal();
        order.getDiscountTotal();
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
            order.getDiscountTotal();
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
            order.getDiscountTotal();
            return order;
        }
        return null;
    }

    /**
     * Download the invoice PDF for an order.
     */
    @GetMapping("/{id}/invoice")
    public org.springframework.http.ResponseEntity<byte[]> downloadInvoice(@PathVariable int id, Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        Order order = orderDao.findById(id).orElse(null);
        if (order == null || order.getUserId() != userId) {
            return org.springframework.http.ResponseEntity.notFound().build();
        }
        order.setItems(lineItemRepo.findByOrderId(id));
        order.getTotal();
        order.getDiscountTotal();
        byte[] pdf = invoiceService.generateInvoice(order);
        return org.springframework.http.ResponseEntity.ok()
            .header("Content-Type", "application/pdf")
            .header("Content-Disposition", "attachment; filename=invoice-" + id + ".pdf")
            .body(pdf);
    }

    /**
     * Helper method to convert a {@link Principal} to a user id.
     */
    private int getUserIdFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Authentication required");
        }
        String username = principal.getName();
        User user = userDao.findByUsername(username);
        return user != null ? user.getId() : -1;
    }
}
