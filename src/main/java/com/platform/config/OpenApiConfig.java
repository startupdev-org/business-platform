package com.platform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String AUTH_METHOD = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Business Platform API")
                        .version("1.0")
                        .description("API for managing businesses, services, and employees"))
                .addSecurityItem(new SecurityRequirement().addList(AUTH_METHOD))
                .components(new Components()
                        .addSecuritySchemes(AUTH_METHOD, new SecurityScheme()
                                .name(AUTH_METHOD)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}