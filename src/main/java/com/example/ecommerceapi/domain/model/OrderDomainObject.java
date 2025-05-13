package com.example.ecommerceapi.domain.model;

import com.example.ecommerceapi.domain.model.data_enum.ORDER_STATUS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDomainObject {


    private Long id;


    private Long userId;

    private Long cartId;

    private ORDER_STATUS status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
