package com.example.ecommerceapi.infrastructure.persistence.repository;

import com.example.ecommerceapi.domain.model.CartDomainObject;
import com.example.ecommerceapi.domain.model.CartItemDomainObject;
import com.example.ecommerceapi.domain.model.UserDomainObject;
import com.example.ecommerceapi.domain.model.data_enum.CART_STATUS;
import com.example.ecommerceapi.domain.model.data_enum.USER_ROLE;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.UserEntity;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository.JpaCartRepository;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository.JpaCartRepositoryInterface;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository.JpaUserRepository;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository.JpaUserRepositoryInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class JpaCartRepositoryTest {

    @Autowired
    private JpaCartRepositoryInterface jpaRepository;


    @Autowired
    private JpaUserRepositoryInterface jpaUserRepository;

    @Autowired
    private TestEntityManager entityManager;

    private JpaCartRepository repository;
    private Long userId;

    @BeforeEach
    void setUp() {
        repository = new JpaCartRepository(jpaRepository);
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setRole(USER_ROLE.CUSTOMER);
//        user.setPassword("test123");
        user.setPhoneNumber("1234567890");
        entityManager.persistAndFlush(user);
        userId = user.getId();
        assertNotNull(userId, "User ID should not be null after persisting");
    }

    @AfterEach
    void tearDown() {
        jpaRepository.deleteAll();
        entityManager.clear();
    }

    @Test
    void testSaveAndFindCart() {
        CartDomainObject cart = new CartDomainObject();
        cart.setUserId(userId);
        cart.setTotalAmount(100.0);
        cart.setTotalItemUnit(2);
        cart.setStatus(CART_STATUS.ACTIVE.toString());
        CartItemDomainObject item = new CartItemDomainObject();
        item.setProductId(1L);
        item.setQuantity(2);
        item.setUnitPrice(10.0);
        cart.setCartItems(new ArrayList<>(List.of(item)));

        CartDomainObject savedCart = repository.save(cart);

        assertNotNull(savedCart);
        assertNotNull(savedCart.getId());
        assertEquals(userId, savedCart.getUserId());
        assertEquals(100.0, savedCart.getTotalAmount());
        assertEquals(2, savedCart.getTotalItemUnit());
        assertEquals(CART_STATUS.ACTIVE.toString(), savedCart.getStatus());
        assertEquals(1, savedCart.getCartItems().size());
        assertEquals(1L, savedCart.getCartItems().get(0).getProductId());
        assertEquals(2, savedCart.getCartItems().get(0).getQuantity());
        assertEquals(10.0, savedCart.getCartItems().get(0).getUnitPrice());

        Optional<CartDomainObject> foundCart = repository.findByUserIdAndStatus(userId, CART_STATUS.ACTIVE.toString());
        assertTrue(foundCart.isPresent());
        assertEquals(savedCart.getId(), foundCart.get().getId());
        assertEquals(savedCart.getUserId(), foundCart.get().getUserId());
        assertEquals(savedCart.getTotalAmount(), foundCart.get().getTotalAmount());
        assertEquals(savedCart.getTotalItemUnit(), foundCart.get().getTotalItemUnit());
        assertEquals(savedCart.getStatus(), foundCart.get().getStatus());
        assertEquals(savedCart.getCartItems().size(), foundCart.get().getCartItems().size());
    }

    @Test
    void testFindByUserIdAndStatusNotFound() {
        JpaCartRepository repository = new JpaCartRepository(jpaRepository);
        Optional<CartDomainObject> foundCart = repository.findByUserIdAndStatus(1L, CART_STATUS.ACTIVE.toString());
        assertFalse(foundCart.isPresent());
    }
}