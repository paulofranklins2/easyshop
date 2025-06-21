package org.yearup.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import javax.persistence.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;
    private String name;
    private BigDecimal price;
    @Column(name = "category_id")
    private int categoryId;
    private String description;
    private String color;
    private int stock;
    private boolean isFeatured;
    @Column(name = "image_url")
    private String imageUrl;
}