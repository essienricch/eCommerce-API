package com.example.ecommerceapi.application.service;

import com.example.ecommerceapi.application.port.input.CartUseCase;
import com.example.ecommerceapi.application.port.output.CartRepository;
import com.example.ecommerceapi.application.port.output.ProductRepository;
import com.example.ecommerceapi.application.port.output.UserRepository;
import com.example.ecommerceapi.domain.exception.ReuseableExceptionHandler;
import com.example.ecommerceapi.domain.model.CartDomainObject;
import com.example.ecommerceapi.domain.model.CartItemDomainObject;
import com.example.ecommerceapi.domain.model.data_enum.CART_STATUS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService implements CartUseCase {


    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ReuseableExceptionHandler exceptionHandler;
    @Override
    public CartDomainObject addToCart(Long userId, Long cartId, CartItemDomainObject cartItem) {
        log.info("in the add to cart service");
        if (userRepository.existById(userId)){
            if (cartRepository.existById(cartId) && cartRepository.findById(cartId)
                    .get().getStatus().equals(CART_STATUS.ACTIVE.toString())){
                log.info("cart already exist");
               CartDomainObject cart = cartRepository.findById(cartId).get();
               return addNewOrUpdateCart(cart, cartItem);
            }else {
                log.info("cart is new");
                List <CartItemDomainObject> item = new ArrayList<>();
                item.add(cartItem);
                CartDomainObject newCart = new CartDomainObject();
                newCart.setStatus(String.valueOf(CART_STATUS.ACTIVE));
                newCart.setUserId(userId);
                newCart.setCartItems(item);
                newCart.setTotalAmount(generateCartTotalAmount(newCart, cartItem));
                newCart.setTotalItemUnit(generateTotalItemUnit(item));
               return cartRepository.save(newCart);
            }
        }else {
            throw exceptionHandler.objectNotFoundException("User with Id: "+userId+" not found");
        }
    }

    private double generateCartTotalAmount(CartDomainObject newCart, CartItemDomainObject cartItem) {
        return newCart.getTotalAmount() + (cartItem.getUnitPrice() * cartItem.getQuantity());
    }

    private CartDomainObject addNewOrUpdateCart(CartDomainObject cart, CartItemDomainObject cartItem) {
        List <CartItemDomainObject> cartItems = cart.getCartItems();
        if (productRepository.existByProductId(cartItem.getProductId())){
            AtomicBoolean productMatches = new AtomicBoolean(false);
            cartItems.forEach(item -> {
                if (item.getProductId() == cartItem.getProductId()){
                    log.info("item exist in the cart");
                    item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                    cart.setTotalAmount(cart.getTotalAmount() + (cartItem.getUnitPrice() * cartItem.getQuantity()));
                    cart.setTotalItemUnit(cart.getTotalItemUnit() + cartItem.getQuantity());
                    productMatches.set(true);
                }
            });
            if (productMatches.equals(false)){
                log.info("item not found in the cart");
                cartItems.add(cartItem);
                cart.setTotalAmount(cart.getTotalAmount() + (cartItem.getUnitPrice() * cartItem.getQuantity()));
                cart.setTotalItemUnit(cart.getTotalItemUnit() + cartItem.getQuantity());
                cart.setCartItems(cartItems);
            }
            log.info("bout to save cart");
            //cart.setUpdatedAt(LocalDateTime.now());
           return cartRepository.save(cart);
        }else {
            throw exceptionHandler.objectNotFoundException("Product with ID; "+cartItem.getProductId()+" not found");
        }
    }

    @Override
    public CartDomainObject viewCart(Long cartId) {
        log.info("in the view cart by ID");
        return cartRepository.findById(cartId).
                orElseThrow(() ->
                        exceptionHandler.objectNotFoundException("Cart With ID: "+cartId+" not found"));
    }

    @Override
    public CartDomainObject removeItemFromCart(Long userId,Long cartId, CartItemDomainObject cartItem) {
        log.info("in the remove item from cart");
        if (userRepository.existById(userId)){
           Optional <CartDomainObject> cart = cartRepository.findById(cartId);
           if (cart.isPresent()){
               List <CartItemDomainObject> cartItems = cart.get().getCartItems();
                if (productRepository.existByProductId(cartItem.getProductId())) {
                    cartItems.forEach(item -> {
                        if (item.getProductId() == cartItem.getProductId()) {
                            log.info("item exist in the cart");
                            item.setQuantity(item.getQuantity() - cartItem.getQuantity());
                            cart.get().setTotalAmount(cart.get().getTotalAmount() - (cartItem.getUnitPrice() * cartItem.getQuantity()));
                            cart.get().setTotalItemUnit(cart.get().getTotalItemUnit() - cartItem.getQuantity());
                            cart.get().setUpdatedAt(LocalDateTime.now());
                            if (item.getQuantity() == 0){
                                cartItems.remove(item);
                            }
                        }
                    });
                           return cartRepository.save(cart.get());
                }else {
                    throw exceptionHandler.objectNotFoundException("Product with ID; "+cartItem.getProductId()+" not found");
                }
           }else {
              throw  exceptionHandler.objectNotFoundException("Cart With ID: "+cartId+" not found");
           }
        }
        else {
            throw exceptionHandler.objectNotFoundException("User with Id: "+userId+" not found");
        }
    }

    private int generateTotalItemUnit(List<CartItemDomainObject> cartItems) {
        AtomicInteger totalUnit = new AtomicInteger();
        cartItems.forEach(itemDomainObject -> {
           totalUnit.addAndGet(itemDomainObject.getQuantity());
        });
        return totalUnit.get();
    }

    @Override
    public void clearCart(Long userId, Long cartId) {
        log.info("in the clear a cart service");
        if (userRepository.existById(userId)){
            Optional <CartDomainObject> cart = cartRepository.findById(cartId);
            if (cart.isPresent()){
               cartRepository.deleteById(cartId);
            }else {
                throw  exceptionHandler.objectNotFoundException("Cart With ID: "+cartId+" not found");
            }
        }
        else {
            throw exceptionHandler.objectNotFoundException("User with Id: "+userId+" not found");
        }
    }
}
