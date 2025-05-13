package com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository;

import com.example.ecommerceapi.domain.model.data_enum.CART_STATUS;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCartRepositoryInterface extends JpaRepository <CartEntity, Long> {

    Optional <CartEntity> findByUser_IdAndStatus(Long userId, CART_STATUS status);
}
