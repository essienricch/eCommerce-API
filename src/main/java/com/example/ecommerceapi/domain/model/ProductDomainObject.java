package com.example.ecommerceapi.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDomainObject {


    private Long id;

    private String name;

    private String description;

    private double price;

    private int stockCount;


}
