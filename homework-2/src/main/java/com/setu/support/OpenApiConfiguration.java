package com.setu.support;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    OpenAPI supportTicketOpenApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Homework 2 Support Ticket API")
                .version("0.0.1-SNAPSHOT")
                .description("CRUD, filtering, and multi-format import API for support tickets."));
    }
}
