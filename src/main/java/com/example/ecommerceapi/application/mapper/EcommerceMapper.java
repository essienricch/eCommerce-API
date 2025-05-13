package com.example.ecommerceapi.application.mapper;

import com.example.ecommerceapi.application.dto.*;
import com.example.ecommerceapi.domain.model.*;
import com.example.ecommerceapi.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EcommerceMapper {

    // User Mappings
//    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // Ignore password from SignUpDto
    UserDomainObject toUserDomain(SignUpDto dto);

    @Mapping(target = "role", expression = "java(domain.getRole().name())") // Convert USER_ROLE to String
    UserDto toUserDto(UserDomainObject domain);


    @Mapping(target = "role", expression = "java(entity.getRole().name())") // Convert USER_ROLE to String
    UserDto toUserDto(UserEntity entity);


    // Product Mappings
    @Mapping(target = "id", ignore = true)
    ProductDomainObject toProductDomain(ProductDto dto);

    ProductDto toProductDTO(ProductDomainObject domain);

    // Cart Mappings
    CartItemDomainObject toCartItemDomain(CartItemDto dto);

    CartItemDto toCartItemDTO(CartItemDomainObject domain);

    CartDto toCartDTO(CartDomainObject domain);

    // Order Mappings
    @Mapping(target = "id", ignore = true)
    OrderDomainObject toOrderDomain(OrderDto dto);

    OrderDto toOrderDTO(OrderDomainObject domain);
}
