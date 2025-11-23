package com.kostavows.transaction.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Transaction Service API",
        version = "1.0",
        description = "API untuk Transfer, Top-up, Riwayat Transaksi, dll"
    ),
    security = @SecurityRequirement(name = "bearerAuth")
)
@Configuration
public class SwaggerConfig {
}