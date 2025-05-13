package com.example.ecommerceapi.infrastructure.adapter.input.rest.controller;

import com.example.ecommerceapi.application.dto.OrderDto;
import com.example.ecommerceapi.application.mapper.EcommerceMapper;
import com.example.ecommerceapi.application.port.input.OrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderUseCase orderUseCase;

    private final EcommerceMapper mapper;

    @PostMapping("/{userId}")
    public ResponseEntity<?> createOrder(@PathVariable Long userId, @RequestBody OrderDto request) {
        return ResponseEntity.ok(mapper.toOrderDTO(
                orderUseCase.createOrder(userId, mapper.toOrderDomain(request))
        ));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> viewOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(
                orderUseCase.viewOrders(userId).stream()
                        .map(mapper::toOrderDTO)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{userId}/{orderId}")
    public ResponseEntity<?> viewSingleOrder(@PathVariable Long userId, @PathVariable Long orderId) {
        return ResponseEntity.ok(mapper.toOrderDTO(
                orderUseCase.viewSingleOrder(userId, orderId)
        ));
    }
}
