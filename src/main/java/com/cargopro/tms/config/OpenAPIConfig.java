package com.cargopro.tms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI defineOpenApi() {
        // Defines the server URL for the API documentation (local)
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development Environment");

        Contact myContact = new Contact();
        myContact.setName("Your Name");
        myContact.setEmail("careers@cargopro.ai");

        // Defines the metadata for the Swagger UI page
        Info information = new Info()
                .title("Transport Management System (TMS) API")
                .version("1.0")
                .description("API for managing Loads, Transporters, Bids, and Bookings, enforcing business logic like optimistic locking and capacity validation.")
                .contact(myContact);

        return new OpenAPI().info(information).servers(List.of(server));
    }
}
