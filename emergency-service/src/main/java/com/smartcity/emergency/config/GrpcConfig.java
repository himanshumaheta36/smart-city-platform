package com.smartcity.emergency.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {
    // La configuration gRPC est gérée par grpc-server-spring-boot-starter
    // Le serveur gRPC écoute sur le port 9090 (défini dans application.yml)
    // Le serveur REST écoute sur le port 8083
}