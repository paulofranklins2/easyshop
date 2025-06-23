package org.yearup.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.model.PromoCode;
import org.yearup.model.PromoCodeUsage;
import org.yearup.repository.UserRepository;
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
    private final UserRepository userRepo;

    public PromoCodeController(PromoCodeService service, UserRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
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

    @GetMapping("/history")
    public List<PromoCodeUsage> history(java.security.Principal principal) {
        int userId = getUserIdFromPrincipal(principal);
        return service.getUsageForUser(userId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PromoCode update(@PathVariable int id,
                            @RequestParam(name = "percent", required = false) BigDecimal percent,
                            @RequestParam(name = "code", required = false) String code) {
        return service.update(id, percent, code);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    private int getUserIdFromPrincipal(java.security.Principal principal) {
        if (principal == null) return -1;
        String username = principal.getName();
        org.yearup.model.User user = userRepo.findByUsername(username);
        return user != null ? user.getId() : -1;
    }
}
