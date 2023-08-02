package com.example.apilavanderia.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerDoc {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Lavanderia de condominios")
                        .description("API para o sistema de agendamentos de uma lavanderia")
                        .summary("Alguma coisa deve aparecer em algum lugar")
                        .contact(new Contact()
                                .name("Diener Dornelas")
                                .email("diener.dornelas@growdev.academy")));
    }
}


