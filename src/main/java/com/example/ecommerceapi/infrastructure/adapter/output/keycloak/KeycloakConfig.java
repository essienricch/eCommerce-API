package com.example.ecommerceapi.infrastructure.adapter.output.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class KeycloakConfig {
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.admin.realm}")
    private String adminRealm;

    @Value("${keycloak.admin.client-id}")
    private String adminClientId;

    @Value("${keycloak.credentials.secret}")
    private String adminClientSecret;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Bean
    public Keycloak keycloakAdminClient() {
        log.info("Keycloak auth server url: " + serverUrl);
        log.info("Keycloak realm: " + adminRealm);
        log.info("Keycloak admin client id: " + adminClientId);
        log.info("Keycloak admin client secret: " + adminClientSecret);
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(adminRealm)
                .clientId(adminClientId)
                .clientSecret(adminClientSecret)
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }
}
