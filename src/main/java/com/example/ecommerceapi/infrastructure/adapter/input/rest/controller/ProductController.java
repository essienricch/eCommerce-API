package com.example.ecommerceapi.infrastructure.adapter.input.rest.controller;

import com.example.ecommerceapi.application.dto.ProductDto;
import com.example.ecommerceapi.application.mapper.EcommerceMapper;
import com.example.ecommerceapi.application.port.input.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;

    private final EcommerceMapper mapper;

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody ProductDto request) {
        return new ResponseEntity<>(mapper.toProductDTO(
                productUseCase.addProduct(mapper.toProductDomain(request))
        ), HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDto request) {
        return ResponseEntity.ok(mapper.toProductDTO(
                productUseCase.updateProduct(productId, mapper.toProductDomain(request))
        ));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> viewSingleProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(mapper.toProductDTO(
                productUseCase.viewSingleProduct(productId)
        ), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable String name) {
        return new ResponseEntity<>(mapper.toProductDTO(
                productUseCase.getProductByName(name)
        ), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> viewAllProducts() {
        return ResponseEntity.ok(
                productUseCase.viewAllProducts().stream()
                        .map(mapper::toProductDTO)
                        .collect(Collectors.toList())
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        productUseCase.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }
}
