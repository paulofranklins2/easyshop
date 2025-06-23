package org.yearup.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Simple promotional code entity storing a percent discount.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "promo_codes")
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promo_code_id")
    private int promoCodeId;

    @Column(unique = true)
    private String code;

    @Column(name = "discount_percent")
    private BigDecimal discountPercent = BigDecimal.ZERO;
}
