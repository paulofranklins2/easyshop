package org.yearup.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void customOpenAPIHasBearerAuthScheme() {
        org.yearup.config.OpenApiConfig config = new org.yearup.config.OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();
        assertNotNull(openAPI.getComponents().getSecuritySchemes().get("bearerAuth"));
        assertTrue(openAPI.getSecurity().stream().anyMatch(req -> req.containsKey("bearerAuth")));
    }
}
