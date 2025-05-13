package com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository;

import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepositoryInterface extends JpaRepository <UserEntity, Long> {
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);

}
