package com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository;

import com.example.ecommerceapi.application.port.output.CartRepository;
import com.example.ecommerceapi.domain.model.CartDomainObject;
import com.example.ecommerceapi.domain.model.CartItemDomainObject;
import com.example.ecommerceapi.domain.model.data_enum.CART_STATUS;
import com.example.ecommerceapi.domain.model.data_enum.ORDER_STATUS;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.CartEntity;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.CartItemEmbeddable;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCartRepository implements CartRepository {

    private final JpaCartRepositoryInterface jpaRepository;
    @Override
    public Optional<CartDomainObject> findById(Long cartId) {
        return jpaRepository.findById(cartId).map(this::toDomain);
    }



    @Override
    public Optional<CartDomainObject> findByUserIdAndStatus(Long userId, String status) {
        return jpaRepository.findByUser_IdAndStatus(userId, CART_STATUS.valueOf(status)).map(this::toDomain);
    }

    @Override
    public CartDomainObject save(CartDomainObject cart) {
        CartEntity entity = toEntity(cart);
        CartEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public boolean existById(Long cartId) {
        return jpaRepository.existsById(cartId);
    }

    @Override
    public void deleteById(Long cartId) {
        jpaRepository.deleteById(cartId);
    }

    private CartEntity toEntity(CartDomainObject cart) {
        CartEntity entity = new CartEntity();
        UserEntity user = new UserEntity();
        user.setId(cart.getUserId());
        entity.setUser(user);
        entity.setTotalAmount(cart.getTotalAmount());
        entity.setTotalItemUnit(cart.getTotalItemUnit());
        entity.setStatus(CART_STATUS.valueOf(cart.getStatus()));
        List<CartItemEmbeddable> itemEntities = cart.getCartItems().stream()
                .map(this::toItemEntity)
                .toList();
        entity.setItems(itemEntities);
        return entity;
    }

    private CartItemEmbeddable toItemEntity(CartItemDomainObject item) {
        CartItemEmbeddable entity = new CartItemEmbeddable();
        entity.setProductId(item.getProductId());
        entity.setQuantity(item.getQuantity());
        entity.setUnitPrice(item.getUnitPrice());
        return entity;
    }

    private CartDomainObject toDomain(CartEntity entity) {
        CartDomainObject cart = new CartDomainObject();
        cart.setId(entity.getId());
        cart.setUserId(entity.getUser().getId());
        cart.setTotalAmount(entity.getTotalAmount());
        cart.setTotalItemUnit(entity.getTotalItemUnit());
        cart.setStatus(String.valueOf(entity.getStatus()));
        cart.setCreatedAt(entity.getCreatedAt());
        cart.setUpdatedAt(entity.getUpdatedAt());
        List<CartItemDomainObject> items = entity.getItems().stream()
                .map(this::toItemDomain)
                .toList();
        cart.setCartItems(items);
        return cart;
    }

    private CartItemDomainObject toItemDomain(CartItemEmbeddable entity) {
        CartItemDomainObject item = new CartItemDomainObject();
        item.setProductId(entity.getProductId());
        item.setQuantity(entity.getQuantity());
        item.setUnitPrice(entity.getUnitPrice());
        return item;
    }
}
