package com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository;

import com.example.ecommerceapi.application.port.output.UserRepository;
import com.example.ecommerceapi.domain.model.UserDomainObject;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {

    private final JpaUserRepositoryInterface jpaRepository;
    @Override
    public Optional<UserDomainObject> findById(Long userId) {
        return jpaRepository.findById(userId).map(this::toDomain);
    }

    @Override
    public Optional<UserDomainObject> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existById(Long userId) {
        return jpaRepository.existsById(userId);
    }

    @Override
    public UserEntity save(UserDomainObject user) {
        UserEntity entity = toEntity(user);
        return jpaRepository.save(entity);
    }

    private UserEntity toEntity(UserDomainObject user) {
        UserEntity entity = new UserEntity();
        entity.setEmail(user.getEmail());
        entity.setRole(user.getRole());
        entity.setPhoneNumber(user.getPhoneNumber());
        return entity;
    }

    private UserDomainObject toDomain(UserEntity entity) {
        UserDomainObject user = new UserDomainObject();
        user.setId(entity.getId());
        user.setEmail(entity.getEmail());
        user.setRole(entity.getRole());
        user.setPhoneNumber(entity.getPhoneNumber());
        return user;
    }
}
