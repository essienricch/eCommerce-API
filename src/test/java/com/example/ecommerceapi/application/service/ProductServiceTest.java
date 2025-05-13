package com.example.ecommerceapi.application.service;

import com.example.ecommerceapi.application.port.output.ProductRepository;
import com.example.ecommerceapi.domain.exception.ReuseableExceptionHandler;
import com.example.ecommerceapi.domain.model.ProductDomainObject;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReuseableExceptionHandler exceptionHandler;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProductSuccess() {
        ProductDomainObject productInput = new ProductDomainObject();
        productInput.setName("Test Product");
        productInput.setPrice(10.0);
        productInput.setStockCount(100);

        ProductDomainObject savedProduct = new ProductDomainObject();
        savedProduct.setId(1L);
        savedProduct.setName("Test Product");
        savedProduct.setPrice(10.0);
        savedProduct.setStockCount(100);

        when(productRepository.existByProductName("Test Product")).thenReturn(false);
        when(productRepository.save(any(ProductDomainObject.class))).thenReturn(savedProduct);

        ProductDomainObject result = productService.addProduct(productInput);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(10.0, result.getPrice());
        assertEquals(100, result.getStockCount());
        verify(productRepository).existByProductName("Test Product");
        verify(productRepository).save(any(ProductDomainObject.class));
    }

    @Test
    void testAddProductNameEmpty() {
        ProductDomainObject productInput = new ProductDomainObject();
        productInput.setName("");
        productInput.setPrice(10.0);
        productInput.setStockCount(100);

        when(exceptionHandler.illegalArgumentException("product name cannot be empty or null"))
                .thenThrow(new IllegalArgumentException("product name cannot be empty or null"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.addProduct(productInput));
        assertEquals("product name cannot be empty or null", exception.getMessage());
        verify(productRepository, never()).existByProductName(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void testAddProductAlreadyExists() {
        ProductDomainObject productInput = new ProductDomainObject();
        productInput.setName("Test Product");
        productInput.setPrice(10.0);
        productInput.setStockCount(100);

        when(productRepository.existByProductName("Test Product")).thenReturn(true);
        when(exceptionHandler.objectAlreadyExistException("Product with name: Test Product already exist"))
                .thenThrow(new RuntimeException("Product with name: Test Product already exist"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.addProduct(productInput));
        assertEquals("Product with name: Test Product already exist", exception.getMessage());
        verify(productRepository).existByProductName("Test Product");
        verify(productRepository, never()).save(any());
    }

    @Test
    void testUpdateProductSuccess() {
        Long productId = 1L;
        ProductDomainObject existingProduct = new ProductDomainObject();
        existingProduct.setId(productId);
        existingProduct.setName("Old Product");
        existingProduct.setPrice(10.0);
        existingProduct.setStockCount(100);

        ProductDomainObject updateInput = new ProductDomainObject();
        updateInput.setName("New Product");
        updateInput.setPrice(20.0);
        updateInput.setStockCount(200);

        ProductDomainObject updatedProduct = new ProductDomainObject();
        updatedProduct.setId(productId);
        updatedProduct.setName("New Product");
        updatedProduct.setPrice(20.0);
        updatedProduct.setStockCount(200);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(ProductDomainObject.class))).thenReturn(updatedProduct);

        ProductDomainObject result = productService.updateProduct(productId, updateInput);

        assertNotNull(result);
        assertEquals("New Product", result.getName());
        assertEquals(20.0, result.getPrice());
        assertEquals(200, result.getStockCount());
        verify(productRepository).findById(productId);
        verify(productRepository).save(any(ProductDomainObject.class));
    }

    @Test
    void testUpdateProductNotFound() {
        Long productId = 1L;
        ProductDomainObject updateInput = new ProductDomainObject();
        updateInput.setName("New Product");

        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        when(exceptionHandler.objectNotFoundException("Product with ID: " + productId + " not found"))
                .thenThrow(new RuntimeException("Product with ID: " + productId + " not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.updateProduct(productId, updateInput));
        assertEquals("Product with ID: " + productId + " not found", exception.getMessage());
        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void testViewSingleProductSuccess() {
        Long productId = 1L;
        ProductDomainObject product = new ProductDomainObject();
        product.setId(productId);
        product.setName("Test Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        ProductDomainObject result = productService.viewSingleProduct(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Test Product", result.getName());
        verify(productRepository).findById(productId);
    }

    @Test
    void testViewSingleProductNotFound() {
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        when(exceptionHandler.objectNotFoundException("product with Id: " + productId + " not found"))
                .thenThrow(new RuntimeException("product with Id: " + productId + " not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.viewSingleProduct(productId));
        assertEquals("product with Id: " + productId + " not found", exception.getMessage());
        verify(productRepository).findById(productId);
    }

    @Test
    void testGetProductByNameSuccess() {
        String name = "Test Product";
        ProductDomainObject product = new ProductDomainObject();
        product.setName(name);

        when(productRepository.findByName(name)).thenReturn(Optional.of(product));

        ProductDomainObject result = productService.getProductByName(name);

        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(productRepository).findByName(name);
    }

    @Test
    void testGetProductByNameNotFound() {
        String name = "Test Product";

        when(productRepository.findByName(name)).thenReturn(Optional.empty());
        when(exceptionHandler.objectNotFoundException("product with name: " + name + " not found"))
                .thenThrow(new RuntimeException("product with name: " + name + " not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getProductByName(name));
        assertEquals("product with name: " + name + " not found", exception.getMessage());
        verify(productRepository).findByName(name);
    }

    @Test
    void testViewAllProductsSuccess() {
        ProductDomainObject product = new ProductDomainObject();
        product.setId(1L);
        product.setName("Test Product");

        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<ProductDomainObject> result = productService.viewAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository).findAll();
    }

    @Test
    void testDeleteProductByIdSuccess() {
        Long productId = 1L;

        when(productRepository.existByProductId(productId)).thenReturn(true);

        productService.deleteProductById(productId);

        verify(productRepository).existByProductId(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void testDeleteProductByIdNotFound() {
        Long productId = 1L;

        when(productRepository.existByProductId(productId)).thenReturn(false);
        when(exceptionHandler.objectNotFoundException("Product with ID: " + productId + " does not exist"))
                .thenThrow(new RuntimeException("Product with ID: " + productId + " does not exist"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.deleteProductById(productId));
        assertEquals("Product with ID: " + productId + " does not exist", exception.getMessage());
        verify(productRepository).existByProductId(productId);
        verify(productRepository, never()).deleteById(any());
    }
}