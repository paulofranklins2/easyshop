package org.yearup.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class DatabaseConfigTest {

    @Test
    void dataSource() {
        DatabaseConfig config = new DatabaseConfig("jdbc:h2:mem:test","u","p");
        assertNotNull(config.dataSource());
        assertEquals("jdbc:h2:mem:test", config.dataSource().getUrl());
    }
}
