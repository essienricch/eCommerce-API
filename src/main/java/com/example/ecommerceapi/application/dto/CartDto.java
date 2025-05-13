package com.example.ecommerceapi.application.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {

    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems;
    private double totalAmount;
    private int totalItemUnit;
    private String status;
    private String createdAt;
    private String updatedAt;
}
