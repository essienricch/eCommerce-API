package com.example.ecommerceapi.infrastructure.adapter.output.keycloak;

import com.example.ecommerceapi.domain.exception.ReuseableExceptionHandler;
import com.example.ecommerceapi.application.dto.SignUpDto;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakServiceAdapter {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;


    public void registerKeycloakUser (SignUpDto request){
         log.info("keycloak instance started-------------");
        UserRepresentation user = getUserRepresentation(request);
        log.info("User representation created ----------------- {} , {}",realm,keycloak);
        Response response = keycloak.realm(realm).users().create(user);
        log.info("{}",response);
        log.info("keycloak user  created------------- {}", response.getStatus());
        if (response.getStatus() != HttpStatus.CREATED.value()) {
            throw  new RuntimeException("Failed to create user with email: " + request.getEmail());
        } else {
            String userId = keycloak.realm(realm).users().search(request.getEmail()).get(0).getId();
            RolesResource rolesResource = getRolesResource();
            RoleRepresentation role = rolesResource.get(request.getRole()).toRepresentation();
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Collections.singletonList(role));
        }
    }

    private RolesResource getRolesResource(){
        return  keycloak.realm(realm).roles();
    }

    private static @NotNull UserRepresentation getUserRepresentation(SignUpDto signUpDto) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setEmail(signUpDto.getEmail());
        user.setEmailVerified(true);
        user.setUsername(signUpDto.getEmail());
        log.info("user at this level: "+user.getUsername());

        if (signUpDto.getPhoneNumber() != null) {
            user.setAttributes(Collections.singletonMap("phone_number", Collections.singletonList(signUpDto.getPhoneNumber())));
        }
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(signUpDto.getPassword());

        user.setCredentials(Collections.singletonList(credential));
        return user;
    }

}
