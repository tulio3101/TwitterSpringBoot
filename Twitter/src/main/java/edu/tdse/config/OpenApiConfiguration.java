package edu.tdse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI twitterApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Twitter API")
                .description("Twitter clone API with Post, Stream, and User management")
                .version("v1.0.0")
                .contact(new Contact()
                    .name("Development Team")
                    .email("juan.puentes@mail.escuelaing.edu.co")));
    }
}
