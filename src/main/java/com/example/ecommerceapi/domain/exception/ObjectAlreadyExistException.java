package com.example.ecommerceapi.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ObjectAlreadyExistException extends RuntimeException{
    public ObjectAlreadyExistException(String exception){
        super(exception);

    }
}
