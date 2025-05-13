package com.example.ecommerceapi.application.port.input;

import com.example.ecommerceapi.application.dto.UserDto;
import com.example.ecommerceapi.domain.model.UserDomainObject;
import com.example.ecommerceapi.application.dto.SignUpDto;

public interface UserUseCase {

    UserDto signUp(SignUpDto request);
    UserDomainObject viewProfile(Long userId);
}
