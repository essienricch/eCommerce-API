package com.example.ecommerceapi.infrastructure.adapter.input.rest.controller;

import com.example.ecommerceapi.application.dto.CartItemDto;
import com.example.ecommerceapi.application.mapper.EcommerceMapper;
import com.example.ecommerceapi.application.port.input.CartUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartUseCase cartUseCase;

    private final EcommerceMapper mapper;

    @PostMapping("/add/{userId}/{cartId}")
    public ResponseEntity<?> addToCart(@PathVariable Long userId, @PathVariable Long cartId, @RequestBody CartItemDto request) {
        return ResponseEntity.ok(mapper.toCartDTO(
                cartUseCase.addToCart(userId, cartId, mapper.toCartItemDomain(request))
        ));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<?> viewCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(mapper.toCartDTO(
                cartUseCase.viewCart(cartId)
        ));
    }

    @DeleteMapping("/remove/{userId}/{cartId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable Long userId, @PathVariable Long cartId, @RequestBody CartItemDto request) {
        return ResponseEntity.ok(mapper.toCartDTO(
                cartUseCase.removeItemFromCart(userId, cartId, mapper.toCartItemDomain(request))
        ));
    }

    @DeleteMapping("/clear/{userId}/{cartId}")
    public ResponseEntity<?> clearCart(@PathVariable Long userId, @PathVariable Long cartId) {
        cartUseCase.clearCart(userId, cartId);
        return ResponseEntity.noContent().build();
    }
}
