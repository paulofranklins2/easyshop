package org.yearup.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Tracks a promo code used by a user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "promo_code_usage")
public class PromoCodeUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private int usageId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "promo_code_id")
    private int promoCodeId;

    @Column(name = "discount_percent")
    private BigDecimal discountPercent = BigDecimal.ZERO;

    @Column(name = "used_date")
    private LocalDateTime usedDate;
}
