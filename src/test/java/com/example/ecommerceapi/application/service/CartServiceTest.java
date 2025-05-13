package com.example.ecommerceapi.application.service;

import com.example.ecommerceapi.application.port.output.CartRepository;
import com.example.ecommerceapi.application.port.output.ProductRepository;
import com.example.ecommerceapi.application.port.output.UserRepository;
import com.example.ecommerceapi.domain.exception.ObjectNotFoundException;
import com.example.ecommerceapi.domain.exception.ReuseableExceptionHandler;
import com.example.ecommerceapi.domain.model.CartDomainObject;
import com.example.ecommerceapi.domain.model.CartItemDomainObject;
import com.example.ecommerceapi.domain.model.data_enum.CART_STATUS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CartServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ReuseableExceptionHandler exceptionHandler;

//    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        // Manually instantiate CartService to ensure mocked dependencies are used
        cartService = new CartService(cartRepository, productRepository, userRepository, exceptionHandler);
        System.out.println("Setting up mocks for test. ExceptionHandler: " + exceptionHandler.getClass().getName());
    }

    @Test
    void testAddToCartSuccess() {
        Long userId = 1L;
        Long cartId = 1L;
        CartItemDomainObject cartItem = new CartItemDomainObject();
        cartItem.setProductId(1L);
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(10.0);

        CartDomainObject newCart = new CartDomainObject();
        newCart.setUserId(userId);
        newCart.setStatus(CART_STATUS.ACTIVE.toString());
        List<CartItemDomainObject> items = new ArrayList<>();
        items.add(cartItem);
        newCart.setCartItems(items);
        newCart.setTotalAmount(20.0); // 2 * 10.0
        newCart.setTotalItemUnit(2);

        when(userRepository.existById(userId)).thenReturn(true);
        when(cartRepository.existById(cartId)).thenReturn(false);
        when(cartRepository.save(any(CartDomainObject.class))).thenReturn(newCart);

        CartDomainObject result = cartService.addToCart(userId, cartId, cartItem);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(CART_STATUS.ACTIVE.toString(), result.getStatus());
        assertEquals(1, result.getCartItems().size());
        assertEquals(1L, result.getCartItems().get(0).getProductId());
        assertEquals(2, result.getCartItems().get(0).getQuantity());
        assertEquals(20.0, result.getTotalAmount());
        assertEquals(2, result.getTotalItemUnit());

        verify(userRepository).existById(userId);
        verify(cartRepository).existById(cartId);
        verify(cartRepository).save(any(CartDomainObject.class));
        verifyNoInteractions(productRepository, exceptionHandler);
    }

    @Test
    void testAddToCartProductNotFound() {
        Long userId = 1L;
        Long cartId = 1L;
        CartItemDomainObject cartItem = new CartItemDomainObject();
        cartItem.setProductId(1L);
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(10.0);

        CartDomainObject existingCart = new CartDomainObject();
        existingCart.setId(cartId);
        existingCart.setUserId(userId);
        existingCart.setStatus(CART_STATUS.ACTIVE.toString());
        existingCart.setCartItems(new ArrayList<>());

        ObjectNotFoundException expectedException = new ObjectNotFoundException("Product with ID: " + cartItem.getProductId() + " not found");

        when(userRepository.existById(userId)).thenReturn(true);
        when(cartRepository.existById(cartId)).thenReturn(true);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));
        when(productRepository.existByProductId(cartItem.getProductId())).thenReturn(false);
        when(exceptionHandler.objectNotFoundException(anyString())).thenReturn(expectedException);

        System.out.println("Executing testAddToCartProductNotFound");

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> cartService.addToCart(userId, cartId, cartItem));
        assertEquals("Product with ID: " + cartItem.getProductId() + " not found", exception.getMessage());

        verify(userRepository).existById(userId);
        verify(cartRepository).existById(cartId);
        verify(cartRepository, times(2)).findById(cartId);
        verify(productRepository).existByProductId(cartItem.getProductId());
        verify(exceptionHandler).objectNotFoundException(anyString());
        verify(cartRepository, never()).save(any());
    }

    @Test
    void testAddToCartUserNotFound() {
        Long userId = 1L;
        Long cartId = 1L;
        CartItemDomainObject cartItem = new CartItemDomainObject();
        cartItem.setProductId(1L);
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(10.0);

        ObjectNotFoundException expectedException = new ObjectNotFoundException("User with Id: " + userId + " not found");

        when(userRepository.existById(userId)).thenReturn(false);
        when(exceptionHandler.objectNotFoundException(anyString())).thenReturn(expectedException);

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> cartService.addToCart(userId, cartId, cartItem));
        assertEquals("User with Id: " + userId + " not found", exception.getMessage());

        verify(userRepository).existById(userId);
        verify(exceptionHandler).objectNotFoundException(anyString());
        verifyNoInteractions(productRepository, cartRepository);
    }
}