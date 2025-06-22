package org.yearup.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Composite primary key for {@link ShoppingCartItemEntity}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartItemId implements Serializable {
    private int userId;
    private int productId;
}