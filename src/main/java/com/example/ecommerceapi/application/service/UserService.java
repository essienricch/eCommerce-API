package com.example.ecommerceapi.application.service;

import com.example.ecommerceapi.application.dto.UserDto;
import com.example.ecommerceapi.application.mapper.EcommerceMapper;
import com.example.ecommerceapi.application.port.input.UserUseCase;
import com.example.ecommerceapi.application.port.output.UserRepository;
import com.example.ecommerceapi.domain.exception.ReuseableExceptionHandler;
import com.example.ecommerceapi.application.dto.SignUpDto;
import com.example.ecommerceapi.domain.model.UserDomainObject;
import com.example.ecommerceapi.domain.model.data_enum.USER_ROLE;
import com.example.ecommerceapi.infrastructure.adapter.output.keycloak.KeycloakServiceAdapter;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserUseCase {

    UserRepository userRepository;

    EcommerceMapper mapper;

    KeycloakServiceAdapter keycloak;
    ReuseableExceptionHandler exceptionHandler;


    @Override
    public UserDto signUp(SignUpDto request) {
        log.info("user about to sign-up");
       validateSignUpDetails(request);
       log.info("done validating");
       checkIfUserExistByEmail(request.getEmail());
       log.info("user does not exist");
       keycloak.registerKeycloakUser(request);
        UserDomainObject domain = mapper.toUserDomain(request);
        UserEntity savedEntity = userRepository.save(domain);
       return mapper.toUserDto(savedEntity);
    }

    private void validateSignUpDetails(SignUpDto request){
        if (request.getEmail() == null || request.getEmail().isBlank()){
            throw exceptionHandler.illegalArgumentException("email cannot be null or empty");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()){
            throw exceptionHandler.illegalArgumentException("password cannot be null or empty");
        }
        if (request.getRole() == null || request.getRole().isBlank()){
            throw exceptionHandler.illegalArgumentException("role cannot be null or empty");
        }
    }

    public void checkIfUserExistByEmail(String email){
        if (userRepository.existByEmail(email)){
            throw exceptionHandler.objectAlreadyExistException("User with email: "+email+ " already exist");
        }
    }

    @Override
    public UserDomainObject viewProfile(Long userId) {
        log.info("about to retrieve user");
        return userRepository.findById(userId).orElseThrow(() -> exceptionHandler.objectNotFoundException("User with ID: "+userId+" not found"));
    }

}
