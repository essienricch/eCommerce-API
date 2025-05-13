package com.example.ecommerceapi.application.service;

import com.example.ecommerceapi.application.port.input.OrderUseCase;
import com.example.ecommerceapi.application.port.output.CartRepository;
import com.example.ecommerceapi.application.port.output.OrderRepository;
import com.example.ecommerceapi.application.port.output.UserRepository;
import com.example.ecommerceapi.domain.exception.ReuseableExceptionHandler;
import com.example.ecommerceapi.domain.model.CartDomainObject;
import com.example.ecommerceapi.domain.model.OrderDomainObject;
import com.example.ecommerceapi.domain.model.data_enum.CART_STATUS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderService implements OrderUseCase {


    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final OrderRepository orderRepository;

    private final ReuseableExceptionHandler exceptionHandler;

    @Override
    public OrderDomainObject createOrder(Long userId, OrderDomainObject order) {
        log.info("in the create order service");
        if (userRepository.existById(userId)){
            Optional <CartDomainObject> cart = cartRepository.findById(order.getCartId());
            if (cart.isPresent() && cart.get().getStatus().equals(CART_STATUS.ACTIVE.toString())){
                log.info("cart exist and is active");
                OrderDomainObject newOrder = new OrderDomainObject();
                newOrder.setUserId(userId);
                newOrder.setCartId(order.getCartId());
                cart.get().setStatus(String.valueOf(CART_STATUS.CHECKED_OUT));
                cartRepository.save(cart.get());
                return orderRepository.save(newOrder);
            }else {
               throw exceptionHandler.objectNotFoundException("Cart With ID: "+order.getCartId()+" not found");
            }
        }else {
            throw exceptionHandler.objectNotFoundException("User with Id: "+userId+" not found");
        }
    }

    @Override
    public List<OrderDomainObject> viewOrders(Long userId) {
        log.info("in the get all order of a user");
        if (userRepository.existById(userId)){
            return orderRepository.findByUserId(userId);
        }else {
            throw exceptionHandler.objectNotFoundException("User with Id: "+userId+" not found");
        }
    }

    @Override
    public OrderDomainObject viewSingleOrder(Long userId, Long orderId) {
        if (userRepository.existById(userId)){
            return orderRepository.findById(orderId).orElseThrow(() -> exceptionHandler.objectNotFoundException("Order with ID: "+orderId+ " not found"));
        }else {
            throw exceptionHandler.objectNotFoundException("User with Id: "+userId+" not found");
        }
    }
}
