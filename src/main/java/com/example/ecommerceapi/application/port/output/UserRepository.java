package com.example.ecommerceapi.application.port.output;

import com.example.ecommerceapi.domain.model.UserDomainObject;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.UserEntity;


import java.util.Optional;

public interface UserRepository {

    Optional<UserDomainObject> findById(Long userId);

    Optional<UserDomainObject> findByEmail(String email);


    boolean existByEmail(String email);

    boolean existById(Long userId);
    UserEntity save(UserDomainObject user);
}
