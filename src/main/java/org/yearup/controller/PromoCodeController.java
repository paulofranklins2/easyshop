package org.yearup.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.model.PromoCode;
import org.yearup.service.PromoCodeService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Endpoints for managing promo codes.
 */
@RestController
@RequestMapping("/promocodes")
public class PromoCodeController {
    private final PromoCodeService service;

    public PromoCodeController(PromoCodeService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PromoCode> list() {
        return service.getAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PromoCode create(@RequestParam(name = "percent", defaultValue = "0.1") BigDecimal percent) {
        return service.create(percent);
    }
}
