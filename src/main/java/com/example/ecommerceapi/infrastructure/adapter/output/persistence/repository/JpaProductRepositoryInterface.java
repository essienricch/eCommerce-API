package com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository;

import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaProductRepositoryInterface extends JpaRepository<ProductEntity, Long> {
    boolean existsByName(String productName);

    Optional <ProductEntity> findByName(String productName);
}
