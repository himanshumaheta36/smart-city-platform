package com.smartcity.urbanevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UrbanEventsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UrbanEventsApplication.class, args);
    }
}