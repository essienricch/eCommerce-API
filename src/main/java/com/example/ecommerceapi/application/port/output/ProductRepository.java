package com.example.ecommerceapi.application.port.output;

import com.example.ecommerceapi.domain.model.ProductDomainObject;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<ProductDomainObject> findById(Long productId);

    Optional<ProductDomainObject> findByName(String productName);
    ProductDomainObject save(ProductDomainObject product);
    List<ProductDomainObject> findAll();

    boolean existByProductName(String productName);

    boolean existByProductId(Long productId);
    void deleteById(Long productId);
}
