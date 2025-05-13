package com.example.ecommerceapi.application.port.input;

import com.example.ecommerceapi.domain.model.CartDomainObject;
import com.example.ecommerceapi.domain.model.CartItemDomainObject;

public interface CartUseCase {

    CartDomainObject addToCart(Long userId, Long cartId, CartItemDomainObject cartItem);

    CartDomainObject viewCart(Long cartId);

    CartDomainObject removeItemFromCart(Long userId,Long cartId, CartItemDomainObject itemDomainObject);

    void clearCart(Long userId, Long cartId);
}
