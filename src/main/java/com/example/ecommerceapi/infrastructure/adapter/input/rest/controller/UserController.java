package com.example.ecommerceapi.infrastructure.adapter.input.rest.controller;

import com.example.ecommerceapi.application.mapper.EcommerceMapper;
import com.example.ecommerceapi.application.port.input.UserUseCase;
import com.example.ecommerceapi.application.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    private final EcommerceMapper ecommerceMapper;

    @PostMapping("/signup")
    public ResponseEntity <?> signUp(@RequestBody  SignUpDto request){
        return new ResponseEntity<>(userUseCase.signUp(request), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> viewProfile(@PathVariable Long userId) {
        return new ResponseEntity<>(ecommerceMapper.toUserDto(
                userUseCase.viewProfile(userId)
        ), HttpStatus.OK);
    }

}
