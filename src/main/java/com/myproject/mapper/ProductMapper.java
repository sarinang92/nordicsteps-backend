// src/main/java/com/myproject/mapper/ProductMapper.java
package com.myproject.mapper;

import com.myproject.dto.ProductBasicDTO;
import com.myproject.dto.ProductDetailDTO;
import com.myproject.model.Products; // <--- CHANGE THIS IMPORT to 'Products' (plural)
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget; // Import for @MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    // --- Mapping from Products entity to DTOs ---

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "currentPrice", target = "currentPrice")
    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "isOnSale", target = "isOnSale") // Add mapping for isOnSale for basic DTO
    ProductBasicDTO toProductBasicDTO(Products product); // <--- Use 'Products'
    List<ProductBasicDTO> toProductBasicDTOs(List<Products> products); // <--- Use 'Products'


    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price") // Map original price
    @Mapping(source = "currentPrice", target = "currentPrice")
    @Mapping(source = "totalStockQuantity", target = "totalStockQuantity")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "areaOfUse", target = "areaOfUse")
    @Mapping(source = "userTarget", target = "userTarget")
    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(source = "category.categoryId", target = "categoryId") // Map category ID from nested object
    @Mapping(source = "isNewArrival", target = "isNewArrival")
    @Mapping(source = "isBestseller", target = "isBestseller")
    @Mapping(source = "isOnSale", target = "isOnSale")
    @Mapping(source = "createdAt", target = "createdAt")
    ProductDetailDTO toProductDetailDTO(Products product); // <--- Use 'Products'

    // You might also need a list version of detailed DTOs if you retrieve multiple detailed products
    List<ProductDetailDTO> toProductDetailDTOs(List<Products> products);


    // --- Mapping from DTO to Products entity (for creation) ---
    // @Mapping(target = "productId", ignore = true) // ID is auto-generated
    // @Mapping(target = "category", ignore = true) // Category will be set separately in service
    // @Mapping(target = "createdAt", ignore = true) // Created at is set by @PrePersist in entity
    // This mapping helps create a new Products entity from a DTO.
    // The service will then handle setting the category and saving.
    Products toProduct(ProductDetailDTO productDTO); // <--- Use 'Products'

    // --- Mapping to update existing Products entity from DTO ---
    // @Mapping(target = "productId", ignore = true) // Never update ID from DTO
    // @Mapping(target = "category", ignore = true) // Category will be set separately in service
    // @Mapping(target = "createdAt", ignore = true) // Created at is set by @PrePersist/shouldn't be updated from DTO
    // This mapping helps update an existing Products entity with new DTO data.
    void updateProductFromDto(ProductDetailDTO productDTO, @MappingTarget Products product); // <--- Use 'Products'
}