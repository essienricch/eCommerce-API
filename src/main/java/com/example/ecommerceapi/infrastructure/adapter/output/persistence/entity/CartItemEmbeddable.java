package com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CartItemEmbeddable {
    private long productId;

    private int quantity;

    private double unitPrice;
}
