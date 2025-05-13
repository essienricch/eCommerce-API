package com.example.ecommerceapi.application.service;

import com.example.ecommerceapi.application.dto.SignUpDto;
import com.example.ecommerceapi.application.dto.UserDto;
import com.example.ecommerceapi.application.mapper.EcommerceMapper;
import com.example.ecommerceapi.application.port.output.UserRepository;
import com.example.ecommerceapi.domain.exception.ObjectAlreadyExistException;
import com.example.ecommerceapi.domain.exception.ObjectNotFoundException;
import com.example.ecommerceapi.domain.exception.ReuseableExceptionHandler;
import com.example.ecommerceapi.domain.model.UserDomainObject;
import com.example.ecommerceapi.infrastructure.adapter.output.keycloak.KeycloakServiceAdapter;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EcommerceMapper mapper;

    @Mock
    private KeycloakServiceAdapter keycloak;

    @Mock
    private ReuseableExceptionHandler exceptionHandler;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, mapper, keycloak, exceptionHandler);
        System.out.println("Setting up mocks for test. ExceptionHandler: " + exceptionHandler.getClass().getName());
    }

    @Test
    void testSignUpSuccess() {
        SignUpDto request = new SignUpDto();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setRole("USER");

        UserDomainObject domain = new UserDomainObject();
        domain.setId(1L);
        domain.setEmail("test@example.com");

        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setEmail("test@example.com");

        UserDto response = new UserDto();
        response.setId(1L);
        response.setEmail("test@example.com");

        when(userRepository.existByEmail("test@example.com")).thenReturn(false);
        when(mapper.toUserDomain(request)).thenReturn(domain);
        when(userRepository.save(domain)).thenReturn(entity);
        when(mapper.toUserDto(entity)).thenReturn(response);

        UserDto result = userService.signUp(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository).existByEmail("test@example.com");
        verify(keycloak).registerKeycloakUser(request);
        verify(mapper).toUserDomain(request);
        verify(userRepository).save(domain);
        verify(mapper).toUserDto(entity);
        verifyNoInteractions(exceptionHandler);
    }

    @Test
    void testSignUpEmailExists() {
        SignUpDto request = new SignUpDto();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setRole("USER");

        ObjectAlreadyExistException expectedException = new ObjectAlreadyExistException("User with email: test@example.com already exist");

        when(userRepository.existByEmail("test@example.com")).thenReturn(true);
        when(exceptionHandler.objectAlreadyExistException(anyString())).thenReturn(expectedException);

        ObjectAlreadyExistException exception = assertThrows(ObjectAlreadyExistException.class,
                () -> userService.signUp(request));
        assertEquals("User with email: test@example.com already exist", exception.getMessage());

        verify(userRepository).existByEmail("test@example.com");
        verify(exceptionHandler).objectAlreadyExistException(anyString());
//        verifyNoInteractions(keycloak, mapper, userRepository);

    }

    @Test
    void testSignUpInvalidEmail() {
        SignUpDto request = new SignUpDto();
        request.setEmail("");
        request.setPassword("password");
        request.setRole("USER");

        IllegalArgumentException expectedException = new IllegalArgumentException("email cannot be null or empty");

        when(exceptionHandler.illegalArgumentException(anyString())).thenReturn(expectedException);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.signUp(request));
        assertEquals("email cannot be null or empty", exception.getMessage());

        verify(exceptionHandler).illegalArgumentException(anyString());
        verifyNoInteractions(userRepository, keycloak, mapper);
    }

    @Test
    void testViewProfileSuccess() {
        Long userId = 1L;
        UserDomainObject user = new UserDomainObject();
        user.setId(userId);
        user.setEmail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDomainObject result = userService.viewProfile(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("test@example.com", result.getEmail());

        verify(userRepository).findById(userId);
        verifyNoInteractions(exceptionHandler, keycloak, mapper);
    }

    @Test
    void testViewProfileNotFound() {
        Long userId = 1L;
        ObjectNotFoundException expectedException = new ObjectNotFoundException("User with ID: " + userId + " not found");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(exceptionHandler.objectNotFoundException(anyString())).thenReturn(expectedException);

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> userService.viewProfile(userId));
        assertEquals("User with ID: " + userId + " not found", exception.getMessage());

        verify(userRepository).findById(userId);
        verify(exceptionHandler).objectNotFoundException(anyString());
        verifyNoInteractions(keycloak, mapper);
    }
}