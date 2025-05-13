package com.example.ecommerceapi.application.service;

import com.example.ecommerceapi.application.port.output.CartRepository;
import com.example.ecommerceapi.application.port.output.OrderRepository;
import com.example.ecommerceapi.application.port.output.UserRepository;
import com.example.ecommerceapi.domain.exception.ReuseableExceptionHandler;
import com.example.ecommerceapi.domain.model.CartDomainObject;
import com.example.ecommerceapi.domain.model.OrderDomainObject;
import com.example.ecommerceapi.domain.model.data_enum.CART_STATUS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReuseableExceptionHandler exceptionHandler;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderSuccess() {
        Long userId = 1L;
        Long cartId = 1L;
        OrderDomainObject orderInput = new OrderDomainObject();
        orderInput.setCartId(cartId);

        CartDomainObject cart = new CartDomainObject();
        cart.setId(cartId);
        cart.setUserId(userId);
        cart.setStatus(CART_STATUS.ACTIVE.toString());

        OrderDomainObject savedOrder = new OrderDomainObject();
        savedOrder.setId(1L);
        savedOrder.setUserId(userId);
        savedOrder.setCartId(cartId);

        when(userRepository.existById(userId)).thenReturn(true);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(CartDomainObject.class))).thenReturn(cart);
        when(orderRepository.save(any(OrderDomainObject.class))).thenReturn(savedOrder);

        OrderDomainObject result = orderService.createOrder(userId, orderInput);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(cartId, result.getCartId());
        verify(userRepository).existById(userId);
        verify(cartRepository).findById(cartId);
        verify(cartRepository).save(any(CartDomainObject.class));
        verify(orderRepository).save(any(OrderDomainObject.class));
        assertEquals(CART_STATUS.CHECKED_OUT.toString(), cart.getStatus());
    }

    @Test
    void testCreateOrderCartNotFound() {
        Long userId = 1L;
        Long cartId = 1L;
        OrderDomainObject orderInput = new OrderDomainObject();
        orderInput.setCartId(cartId);

        when(userRepository.existById(userId)).thenReturn(true);
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());
        when(exceptionHandler.objectNotFoundException("Cart With ID: " + cartId + " not found"))
                .thenThrow(new RuntimeException("Cart With ID: " + cartId + " not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.createOrder(userId, orderInput));
        assertEquals("Cart With ID: " + cartId + " not found", exception.getMessage());
        verify(userRepository).existById(userId);
        verify(cartRepository).findById(cartId);
        verify(cartRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCreateOrderCartNotActive() {
        Long userId = 1L;
        Long cartId = 1L;
        OrderDomainObject orderInput = new OrderDomainObject();
        orderInput.setCartId(cartId);

        CartDomainObject cart = new CartDomainObject();
        cart.setId(cartId);
        cart.setUserId(userId);
        cart.setStatus(CART_STATUS.CHECKED_OUT.toString());

        when(userRepository.existById(userId)).thenReturn(true);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(exceptionHandler.objectNotFoundException("Cart With ID: " + cartId + " not found"))
                .thenThrow(new RuntimeException("Cart With ID: " + cartId + " not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.createOrder(userId, orderInput));
        assertEquals("Cart With ID: " + cartId + " not found", exception.getMessage());
        verify(userRepository).existById(userId);
        verify(cartRepository).findById(cartId);
        verify(cartRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCreateOrderUserNotFound() {
        Long userId = 1L;
        Long cartId = 1L;
        OrderDomainObject orderInput = new OrderDomainObject();
        orderInput.setCartId(cartId);

        when(userRepository.existById(userId)).thenReturn(false);
        when(exceptionHandler.objectNotFoundException("User with Id: " + userId + " not found"))
                .thenThrow(new RuntimeException("User with Id: " + userId + " not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.createOrder(userId, orderInput));
        assertEquals("User with Id: " + userId + " not found", exception.getMessage());
        verify(userRepository).existById(userId);
        verify(cartRepository, never()).findById(any());
        verify(cartRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testViewOrdersSuccess() {
        Long userId = 1L;
        OrderDomainObject order = new OrderDomainObject();
        order.setId(1L);
        order.setUserId(userId);

        when(userRepository.existById(userId)).thenReturn(true);
        when(orderRepository.findByUserId(userId)).thenReturn(Collections.singletonList(order));

        List<OrderDomainObject> result = orderService.viewOrders(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        verify(userRepository).existById(userId);
        verify(orderRepository).findByUserId(userId);
    }

    @Test
    void testViewOrdersUserNotFound() {
        Long userId = 1L;

        when(userRepository.existById(userId)).thenReturn(false);
        when(exceptionHandler.objectNotFoundException("User with Id: " + userId + " not found"))
                .thenThrow(new RuntimeException("User with Id: " + userId + " not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.viewOrders(userId));
        assertEquals("User with Id: " + userId + " not found", exception.getMessage());
        verify(userRepository).existById(userId);
        verify(orderRepository, never()).findByUserId(any());
    }

    @Test
    void testViewSingleOrderSuccess() {
        Long userId = 1L;
        Long orderId = 1L;
        OrderDomainObject order = new OrderDomainObject();
        order.setId(orderId);
        order.setUserId(userId);

        when(userRepository.existById(userId)).thenReturn(true);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        OrderDomainObject result = orderService.viewSingleOrder(userId, orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        assertEquals(userId, result.getUserId());
        verify(userRepository).existById(userId);
        verify(orderRepository).findById(orderId);
    }

    @Test
    void testViewSingleOrderNotFound() {
        Long userId = 1L;
        Long orderId = 1L;

        when(userRepository.existById(userId)).thenReturn(true);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        when(exceptionHandler.objectNotFoundException("Order with ID: " + orderId + " not found"))
                .thenThrow(new RuntimeException("Order with ID: " + orderId + " not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.viewSingleOrder(userId, orderId));
        assertEquals("Order with ID: " + orderId + " not found", exception.getMessage());
        verify(userRepository).existById(userId);
        verify(orderRepository).findById(orderId);
    }

    @Test
    void testViewSingleOrderUserNotFound() {
        Long userId = 1L;
        Long orderId = 1L;

        when(userRepository.existById(userId)).thenReturn(false);
        when(exceptionHandler.objectNotFoundException("User with Id: " + userId + " not found"))
                .thenThrow(new RuntimeException("User with Id: " + userId + " not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.viewSingleOrder(userId, orderId));
        assertEquals("User with Id: " + userId + " not found", exception.getMessage());
        verify(userRepository).existById(userId);
        verify(orderRepository, never()).findById(any());
    }
}