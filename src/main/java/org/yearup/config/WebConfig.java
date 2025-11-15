package org.yearup.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // This will add '/api' prefix to all @RequestMapping paths
        configurer.addPathPrefix("/api", 
            c -> c.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow cross-origin access to the API. Adjust origins as needed.
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Type")
                .maxAge(3600);
        // Note: If you use cookies or need credentials, replace allowedOrigins("*") with
        // specific origins and add .allowCredentials(true).
    }
}
