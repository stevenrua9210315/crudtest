package com.project.test.srj.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Clientes - SRJ")
                        .version("1.0.0")
                        .description("API REST para gestión de clientes y manejo centralizado de errores con SQL Server"));
    }
}
