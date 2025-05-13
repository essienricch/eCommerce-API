package com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository;

import com.example.ecommerceapi.application.port.output.OrderRepository;
import com.example.ecommerceapi.domain.model.OrderDomainObject;
import com.example.ecommerceapi.domain.model.data_enum.ORDER_STATUS;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaOrderRepository implements OrderRepository {

    private final JpaOrderRepositoryInterface jpaRepository;

    @Override
    public Optional<OrderDomainObject> findById(Long orderId) {
        return jpaRepository.findById(orderId).map(this::toDomain);
    }


    @Override
    public List<OrderDomainObject> findByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public OrderDomainObject save(OrderDomainObject order) {
        OrderEntity entity = toEntity(order);
        OrderEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    private OrderEntity toEntity(OrderDomainObject order) {
        OrderEntity entity = new OrderEntity();
        entity.setUserId(order.getUserId());
        entity.setCartId(order.getCartId());
        entity.setStatus(ORDER_STATUS.valueOf(String.valueOf(order.getStatus())));
        return entity;
    }

    private OrderDomainObject toDomain(OrderEntity entity) {
        OrderDomainObject order = new OrderDomainObject();
        order.setId(entity.getId());
        order.setUserId(entity.getUserId());
        order.setCartId(entity.getCartId());
        order.setStatus(entity.getStatus());
        order.setCreatedAt(entity.getCreatedAt());
        order.setUpdatedAt(entity.getUpdatedAt());
        return order;
    }
}
