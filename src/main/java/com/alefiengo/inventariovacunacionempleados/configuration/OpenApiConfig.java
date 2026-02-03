package com.alefiengo.inventariovacunacionempleados.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventarioOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventario de Vacunacion de Empleados")
                        .description("Registro del inventario del estado de vacunacion de empleados con Spring Boot.")
                        .version("API V1")
                        .contact(new Contact()
                                .name("Jose Alejandro Fiengo Vega")
                                .url("linkedin.com/in/jose-alejandro-fiengo-vega-383560165")
                                .email("jose.fiengo.vega@gmail.com")));
    }
}
