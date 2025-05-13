package com.example.ecommerceapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDomainObject {

    private long productId;

    private int quantity;

    private double unitPrice;
}
