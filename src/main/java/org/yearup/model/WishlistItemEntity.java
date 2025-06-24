package org.yearup.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "wishlist")
@IdClass(WishlistItemId.class)
public class WishlistItemEntity implements Serializable {
    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "product_id")
    private int productId;
}
