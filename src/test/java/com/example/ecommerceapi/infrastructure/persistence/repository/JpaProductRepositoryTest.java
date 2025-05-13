package com.example.ecommerceapi.infrastructure.persistence.repository;

import com.example.ecommerceapi.domain.model.ProductDomainObject;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository.JpaProductRepository;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository.JpaProductRepositoryInterface;
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
public class JpaProductRepositoryTest {

    @Autowired
    private JpaProductRepositoryInterface jpaRepository;

    @AfterEach
    void tearDown() {
        jpaRepository.deleteAll();
    }

    @Test
    void testSaveAndFindProduct() {
        ProductDomainObject product = new ProductDomainObject();
        product.setName("Test Product");
        product.setDescription("Description");
        product.setPrice(10.0);
        product.setStockCount(100);

        JpaProductRepository repository = new JpaProductRepository(jpaRepository);
        ProductDomainObject savedProduct = repository.save(product);

        assertNotNull(savedProduct.getId());
        assertEquals("Test Product", savedProduct.getName());
        assertEquals(10.0, savedProduct.getPrice());

        Optional<ProductDomainObject> foundProduct = repository.findByName("Test Product");
        assertTrue(foundProduct.isPresent());
        assertEquals(savedProduct.getId(), foundProduct.get().getId());

        List<ProductDomainObject> allProducts = repository.findAll();
        assertEquals(1, allProducts.size());
        assertEquals("Test Product", allProducts.get(0).getName());
    }

    @Test
    void testFindByNameNotFound() {
        JpaProductRepository repository = new JpaProductRepository(jpaRepository);
        Optional<ProductDomainObject> foundProduct = repository.findByName("Nonexistent Product");
        assertFalse(foundProduct.isPresent());
    }
}
