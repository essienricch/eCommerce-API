package com.example.ecommerceapi.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
@Profile("test")
public class TestJwtConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return (token) -> null; // Mock JwtDecoder that does nothing
    }
}
