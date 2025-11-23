package com.kostavows.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Auth Service API",
        version = "1.0",
        description = "API untuk Register & Login + JWT"
    )
)
@Configuration
public class SwaggerConfig {
}