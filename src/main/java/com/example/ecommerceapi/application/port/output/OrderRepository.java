package com.example.ecommerceapi.application.port.output;

import com.example.ecommerceapi.domain.model.OrderDomainObject;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<OrderDomainObject> findById(Long orderId);
    OrderDomainObject save(OrderDomainObject order);
    List<OrderDomainObject> findByUserId(Long userId);
}
