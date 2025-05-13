package com.example.ecommerceapi.application.port.input;

import com.example.ecommerceapi.domain.model.OrderDomainObject;

import java.util.List;

public interface OrderUseCase {
    OrderDomainObject createOrder(Long userId, OrderDomainObject order);

    List<OrderDomainObject> viewOrders(Long userId);

    OrderDomainObject viewSingleOrder(Long userId, Long orderId);

}
