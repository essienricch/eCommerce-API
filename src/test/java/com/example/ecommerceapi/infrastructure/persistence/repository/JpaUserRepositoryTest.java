package com.example.ecommerceapi.infrastructure.persistence.repository;

import com.example.ecommerceapi.domain.model.UserDomainObject;
import com.example.ecommerceapi.domain.model.data_enum.USER_ROLE;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.UserEntity;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository.JpaUserRepository;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository.JpaUserRepositoryInterface;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class JpaUserRepositoryTest {

    @Autowired
    private JpaUserRepositoryInterface jpaRepository;

    @AfterEach
    void tearDown() {
        jpaRepository.deleteAll();
    }

    @Test
    void testSaveAndFindUser() {
        UserDomainObject user = new UserDomainObject();
        user.setEmail("test@example.com");
        user.setRole(USER_ROLE.CUSTOMER);

        JpaUserRepository repository = new JpaUserRepository(jpaRepository);
        UserEntity savedUser = repository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals(USER_ROLE.CUSTOMER, savedUser.getRole());

        Optional<UserDomainObject> foundUser = repository.findByEmail("test@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    void testFindByEmailNotFound() {
        JpaUserRepository repository = new JpaUserRepository(jpaRepository);
        Optional<UserDomainObject> foundUser = repository.findByEmail("nonexistent@example.com");
        assertFalse(foundUser.isPresent());
    }
}
