package com.example.ecommerceapi.domain.model;

import com.example.ecommerceapi.domain.model.data_enum.CART_STATUS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDomainObject {

    private Long id;

    private Long userId;

    private List <CartItemDomainObject> cartItems = new ArrayList<>();

    private double totalAmount;

    private int totalItemUnit;

    private String status;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
