package com.example.ecommerceapi.infrastructure.persistence.repository;

import com.example.ecommerceapi.domain.model.OrderDomainObject;
import com.example.ecommerceapi.domain.model.data_enum.ORDER_STATUS;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository.JpaOrderRepository;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository.JpaOrderRepositoryInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class JpaOrderRepositoryTest {

    @Autowired
    private JpaOrderRepositoryInterface jpaRepository;

    @AfterEach
    void tearDown() {
        jpaRepository.deleteAll();
    }
    @Test
    void testSaveAndFindOrder() {
        OrderDomainObject order = new OrderDomainObject();
        order.setUserId(1L);
        order.setCartId(1L);
        order.setStatus(ORDER_STATUS.PENDING);

        JpaOrderRepository repository = new JpaOrderRepository(jpaRepository);
        OrderDomainObject savedOrder = repository.save(order);

        assertNotNull(savedOrder.getId());
        assertEquals(ORDER_STATUS.PENDING, savedOrder.getStatus());
        assertEquals(1L, savedOrder.getUserId());

        Optional<OrderDomainObject> foundOrder = repository.findById(savedOrder.getId());
        assertTrue(foundOrder.isPresent());
        assertEquals(savedOrder.getId(), foundOrder.get().getId());

        List<OrderDomainObject> userOrders = repository.findByUserId(1L);
        assertEquals(1, userOrders.size());
        assertEquals(savedOrder.getId(), userOrders.get(0).getId());
    }

    @Test
    void testFindByUserIdEmpty() {
        JpaOrderRepository repository = new JpaOrderRepository(jpaRepository);
        List<OrderDomainObject> userOrders = repository.findByUserId(1L);
        assertTrue(userOrders.isEmpty());
    }
}
