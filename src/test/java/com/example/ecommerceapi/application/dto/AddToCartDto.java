package com.example.ecommerceapi.application.dto;

import lombok.Data;

@Data
public class AddToCartDto {

    private Long productId;

    private int quantity;
}
