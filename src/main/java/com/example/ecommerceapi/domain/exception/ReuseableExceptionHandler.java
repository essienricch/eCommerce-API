package com.example.ecommerceapi.domain.exception;

import org.springframework.stereotype.Service;

@Service

public class ReuseableExceptionHandler {

    public ObjectAlreadyExistException objectAlreadyExistException(String message) {
        return new ObjectAlreadyExistException(message);
    }

    public ObjectNotFoundException objectNotFoundException(String message){
        return new ObjectNotFoundException(message);
    }
    public IllegalArgumentException illegalArgumentException(String message){
        return new IllegalArgumentException(message);
    }

    public BadRequestException badRequestException(String message){
        return new BadRequestException(message);
    }

    public NullPointerException nullPointerException(String message){
        return new NullPointerException(message);
    }
}
