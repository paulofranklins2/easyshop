package org.yearup.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void gettersAndSetters() {
        Category c = new Category();
        c.setCategoryId(7);
        c.setName("cat");
        c.setDescription("desc");

        assertEquals(7, c.getCategoryId());
        assertEquals("cat", c.getName());
        assertEquals("desc", c.getDescription());
    }
}
