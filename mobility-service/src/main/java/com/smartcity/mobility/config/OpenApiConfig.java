package com.smartcity.mobility.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI mobilityServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart City Mobility Service API")
                        .description("REST API for managing urban transportation systems in smart cities")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Smart City Development Team")
                                .email("dev@smartcity.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}