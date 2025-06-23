package org.yearup.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
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
        return new OpenAPI()
            .info(new io.swagger.v3.oas.models.info.Info()
                .title("EasyShop API")
                .version("v1")
                .description("REST API documentation for the EasyShop application"));
    }
}
