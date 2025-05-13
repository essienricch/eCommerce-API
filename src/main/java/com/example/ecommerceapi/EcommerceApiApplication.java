package com.example.ecommerceapi;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.ecommerceapi.application",
        "com.example.ecommerceapi.domain",
        "com.example.ecommerceapi.infrastructure"
})
public class EcommerceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApiApplication.class, args);
    }

//    @EnableWebSecurity
//    @EnableMethodSecurity
//    @KeycloakConfiguration
//    @ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true", matchIfMissing = true)
//    static class KeycloakSecurityConfig {
//
//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http
//                    .csrf(csrf -> csrf.disable())
//                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                    .authorizeHttpRequests(auth -> auth
//                            .requestMatchers("/public/**").permitAll()
//                            .anyRequest().authenticated()
//                    )
//                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {})); // Simplified: No custom JWT configuration needed
//            return http.build();
//        }
//    }
}
