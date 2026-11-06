package com.example.auditconsumer.config;

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
                        .title("Audit Consumer API")
                        .version("1.0.0")
                        .description("JMS event consumer and audit logging service for Home Insurance"));
    }
}
