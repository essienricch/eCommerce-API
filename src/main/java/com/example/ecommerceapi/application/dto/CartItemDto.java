package com.example.ecommerceapi.application.dto;

import lombok.Data;

@Data
public class CartItemDto {

    private long productId;

    private int quantity;

    private double unitPrice;
}
