package org.yearup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTest {

    @Test
    void gettersAndSetters() {
        Product p = new Product();
        p.setProductId(5);
        p.setName("Widget");
        p.setPrice(java.math.BigDecimal.ONE);
        p.setCategoryId(2);
        p.setDescription("desc");
        p.setColor("red");
        p.setStock(3);
        p.setFeatured(true);
        p.setImageUrl("/img.png");

        assertEquals(5, p.getProductId());
        assertEquals("Widget", p.getName());
        assertEquals(java.math.BigDecimal.ONE, p.getPrice());
        assertEquals(2, p.getCategoryId());
        assertEquals("desc", p.getDescription());
        assertEquals("red", p.getColor());
        assertEquals(3, p.getStock());
        assertTrue(p.isFeatured());
        assertEquals("/img.png", p.getImageUrl());
    }
}
