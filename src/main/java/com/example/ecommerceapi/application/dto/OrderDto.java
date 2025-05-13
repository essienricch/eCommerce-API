package com.example.ecommerceapi.application.dto;

import lombok.Data;

@Data
public class OrderDto {

    private Long id;
    private Long userId;
    private Long cartId;
    private String status;
}
