package com.example.assurancehabitation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Home Insurance API")
                        .version("1.0.0")
                        .description("""
                                REST API for Home Insurance management.
                                Allows managing policyholders, accommodations, coverages, contracts, and claims.
                                """));
    }
}
