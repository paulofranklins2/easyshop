package org.yearup.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the OpenAPI/Swagger documentation for the application.
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "EasyShop API",
        version = "v1",
        description = "REST API documentation for the EasyShop application"
    )
)
public class OpenApiConfig {

    /**
     * Provides the base OpenAPI configuration bean used by springdoc.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("EasyShop API")
                .version("v1")
                .description("REST API documentation for the EasyShop application"))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
    }
}
