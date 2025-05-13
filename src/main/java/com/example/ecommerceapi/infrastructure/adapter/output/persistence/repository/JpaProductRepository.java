package com.example.ecommerceapi.infrastructure.adapter.output.persistence.repository;

import com.example.ecommerceapi.application.port.output.ProductRepository;
import com.example.ecommerceapi.domain.model.ProductDomainObject;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaProductRepository implements ProductRepository {

    private final JpaProductRepositoryInterface jpaProductRepo;

    @Override
    public Optional<ProductDomainObject> findById(Long productId) {
        return jpaProductRepo.findById(productId).map(this::toDomain);
    }

    @Override
    public Optional<ProductDomainObject> findByName(String productName) {
        return jpaProductRepo.findByName(productName).map(this::toDomain);
    }

    @Override
    public ProductDomainObject save(ProductDomainObject product) {
        ProductEntity entity = toEntity(product);
        ProductEntity savedEntity = jpaProductRepo.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public List<ProductDomainObject> findAll() {
        return jpaProductRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existByProductName(String productName) {
        return jpaProductRepo.existsByName(productName);
    }

    @Override
    public boolean existByProductId(Long productId) {
        return jpaProductRepo.existsById(productId);
    }

    @Override
    public void deleteById(Long productId) {
        jpaProductRepo.deleteById(productId);
    }


    private ProductEntity toEntity(ProductDomainObject product) {
        ProductEntity entity = new ProductEntity();
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStockCount(product.getStockCount());
        return entity;
    }

    private ProductDomainObject toDomain(ProductEntity entity) {
        ProductDomainObject product = new ProductDomainObject();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        product.setPrice(entity.getPrice());
        product.setStockCount(entity.getStockCount());
        return product;
    }
}
