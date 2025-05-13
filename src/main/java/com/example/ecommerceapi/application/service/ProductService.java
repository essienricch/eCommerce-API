package com.example.ecommerceapi.application.service;

import com.example.ecommerceapi.application.port.input.ProductUseCase;
import com.example.ecommerceapi.application.port.output.ProductRepository;
import com.example.ecommerceapi.domain.exception.ReuseableExceptionHandler;
import com.example.ecommerceapi.domain.model.ProductDomainObject;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {


    private final ReuseableExceptionHandler exceptionHandler;

    private final ProductRepository productRepository;
    @Override
    public ProductDomainObject addProduct(ProductDomainObject product) {
        log.info("about to add new product");
        validateProductDomainObjectDetail(product);
        log.info("product is valid");
        checkIfProductExistByName(product.getName());
        ProductDomainObject newProduct = new ProductDomainObject();
        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        newProduct.setStockCount(product.getStockCount());
        log.info("about to save new product");
        return productRepository.save(newProduct);
    }

    private void checkIfProductExistByName(String name) {
        if (productRepository.existByProductName(name)){
            throw exceptionHandler.objectAlreadyExistException("Product with name: "+name+" already exist");
        }
    }

    private void checkIfProductExistById(Long productId) {
        if (!productRepository.existByProductId(productId)){
            throw exceptionHandler.objectNotFoundException("Product with ID: "+productId+" does not exist");
        }
    }

    private void validateProductDomainObjectDetail(ProductDomainObject product) {
        if (product.getName() == null || product.getName().isBlank()){
            throw exceptionHandler.illegalArgumentException("product name cannot be empty or null");
        }
    }

    @Override
    public ProductDomainObject updateProduct(Long productId, ProductDomainObject product) {
        log.info("in the update a product service");
        Optional <ProductDomainObject> oldProduct = productRepository.findById(productId);
        if (oldProduct.isPresent()){
            return mainUpdate(oldProduct.get(),product);
        }else {
            throw exceptionHandler.objectNotFoundException("Product with ID: "+productId+ " not found");
        }
    }

    private ProductDomainObject mainUpdate(ProductDomainObject productDomainObject, ProductDomainObject product) {
        if (product.getName() != null && !product.getName().isBlank()
                && !Objects.equals(productDomainObject.getName(), product.getName())){
            log.info("updating product name");
            productDomainObject.setName(product.getName());
        }
        if (product.getPrice() != productDomainObject.getPrice()){
            log.info("updating product price");
            productDomainObject.setPrice(product.getPrice());
        }
        if (product.getStockCount() != productDomainObject.getStockCount()){
            log.info("updating product stock count");
            productDomainObject.setStockCount(product.getStockCount());
        }
        return productRepository.save(productDomainObject);
    }

    @Override
    public ProductDomainObject viewSingleProduct(Long productId) {
        log.info("in the view product by Id");
        return productRepository.findById(productId).orElseThrow(() ->
                exceptionHandler.objectNotFoundException("product with Id: "+productId+" not found"));
    }

    @Override
    public ProductDomainObject getProductByName(String name) {
        log.info("in the view product by name");
        return productRepository.findByName(name).orElseThrow(() ->
                exceptionHandler.objectNotFoundException("product with name: "+name+ " not found"));
    }

    @Override
    public List<ProductDomainObject> viewAllProducts() {
        log.info("in the view all products");
        return productRepository.findAll();
    }

    @Override
    public void deleteProductById(Long productId) {
        log.info("in the delete by ID");
        checkIfProductExistById(productId);
        productRepository.deleteById(productId);
    }
}
