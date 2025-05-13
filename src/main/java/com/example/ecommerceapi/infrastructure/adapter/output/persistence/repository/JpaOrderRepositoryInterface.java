package com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository;

import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.OrderEntity;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaOrderRepositoryInterface extends JpaRepository <OrderEntity, Long> {
    Optional <OrderEntity> findByCartId(Long cartId);

    List<OrderEntity> findAllByUserId(Long userId);
}
