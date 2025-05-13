package com.example.ecommerceapi.application.port.input;

import com.example.ecommerceapi.domain.model.ProductDomainObject;

import java.util.List;

public interface ProductUseCase {

    ProductDomainObject addProduct(ProductDomainObject product);

    ProductDomainObject updateProduct (Long productId, ProductDomainObject product);

    ProductDomainObject viewSingleProduct(Long productId);

    ProductDomainObject getProductByName(String name);

    List <ProductDomainObject> viewAllProducts();

    void deleteProductById(Long productId);
}
