package com.example.ecommerceapi.domain.model;

import com.example.ecommerceapi.domain.model.data_enum.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDomainObject {


    private Long id;

    private String password;

    private String email;

    private String phoneNumber;


    private USER_ROLE role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
