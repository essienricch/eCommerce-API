package com.example.ecommerceapi.application.port.output;

import com.example.ecommerceapi.domain.model.CartDomainObject;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CartRepository {

    Optional<CartDomainObject> findById(Long cartId);
    Optional<CartDomainObject> findByUserIdAndStatus(Long userId, String status);
    CartDomainObject save(CartDomainObject cart);

    boolean existById(Long cartId);
    void deleteById(Long cartId);
}
