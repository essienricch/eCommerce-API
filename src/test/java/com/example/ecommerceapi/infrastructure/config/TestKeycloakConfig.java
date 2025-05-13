package com.example.ecommerceapi.infrastructure.config;

import com.example.ecommerceapi.infrastructure.adapter.output.keycloak.KeycloakServiceAdapter;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestKeycloakConfig {

    @Bean
    public KeycloakServiceAdapter keycloakServiceAdapter() {
        return Mockito.mock(KeycloakServiceAdapter.class);
    }
}
