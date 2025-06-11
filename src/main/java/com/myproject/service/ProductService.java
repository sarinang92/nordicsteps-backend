// src/main/java/com/myproject/service/ProductService.java
package com.myproject.service;

import com.myproject.dto.ProductBasicDTO;
import com.myproject.dto.ProductDetailDTO;
import com.myproject.mapper.ProductMapper;
import com.myproject.model.Products; // Use 'Products' (plural)
// Removed import com.myproject.model.Categories;
import com.myproject.repository.ProductRepository;
// Removed import com.myproject.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    // Removed private final CategoryRepository categoryRepository;

    // Get all products (basic info)
    public List<ProductBasicDTO> getAllProductsBasic() {
        List<Products> products = productRepository.findAll();
        return productMapper.toProductBasicDTOs(products);
    }

    // Get product by ID (detailed info)
    public ProductDetailDTO getProductById(Long id) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return productMapper.toProductDetailDTO(product);
    }

    // Create a new product
    public ProductDetailDTO createProduct(ProductDetailDTO productDTO) {
        Products product = productMapper.toProduct(productDTO); // Use mapper for conversion
        // Removed category setting logic for simplification
        Products savedProduct = productRepository.save(product);
        return productMapper.toProductDetailDTO(savedProduct);
    }

    // Update a product
    public ProductDetailDTO updateProduct(Long id, ProductDetailDTO productDTO) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        productMapper.updateProductFromDto(productDTO, product); // Use mapper for updates
        // Removed category setting logic for simplification

        Products updatedProduct = productRepository.save(product);
        return productMapper.toProductDetailDTO(updatedProduct);
    }

    // Delete a product
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }
}