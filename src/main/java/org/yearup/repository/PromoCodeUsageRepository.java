package org.yearup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yearup.model.PromoCodeUsage;

import java.util.List;

/**
 * Repository for {@link PromoCodeUsage} records.
 */
public interface PromoCodeUsageRepository extends JpaRepository<PromoCodeUsage, Integer> {
    List<PromoCodeUsage> findByUserIdOrderByUsedDateDesc(int userId);
}
