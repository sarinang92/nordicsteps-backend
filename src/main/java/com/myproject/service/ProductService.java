package com.myproject.service;

import com.myproject.dto.ProductBasicDTO;
import com.myproject.dto.ProductDetailDTO;
import com.myproject.mapper.ProductMapper;
import com.myproject.model.Products;
import com.myproject.model.Categories; 
import com.myproject.repository.ProductRepository;
import com.myproject.repository.CategoryRepository; 
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

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
        Products product = productMapper.toProduct(productDTO);

        // IMPORTANT: Uncomment this entire block for category handling
        if (productDTO.getCategoryId() != null) {
            Categories category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));
            product.setCategory(category);
        } else {
            // Optional: If categoryId is null, decide if you want to set category to null
            // or if it should be handled as an error (e.g., categories are mandatory)
            product.setCategory(null);
        }

        Products savedProduct = productRepository.save(product);
        return productMapper.toProductDetailDTO(savedProduct);
    }

    // Update a product
    public ProductDetailDTO updateProduct(Long id, ProductDetailDTO productDTO) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        productMapper.updateProductFromDto(productDTO, product);

        if (productDTO.getCategoryId() != null) {
            Categories category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null); // Or keep existing if DTO categoryId is null and you don't want to change it
        }

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

    // Additional methods can be added here as needed
    public List<ProductBasicDTO> searchAndFilter(String query, Double minPrice, Double maxPrice,
                                                 String brand, String color, String size,
                                                 String campaign, String area) {
        List<Products> allProducts = productRepository.findAll();

        return allProducts.stream()
                .filter(p -> query == null || p.getName().toLowerCase().contains(query.toLowerCase()))
                .filter(p -> minPrice == null || p.getPrice().compareTo(BigDecimal.valueOf(minPrice)) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(BigDecimal.valueOf(maxPrice)) <= 0)
                .filter(p -> brand == null || brand.isBlank() || p.getBrand().equalsIgnoreCase(brand))
                .filter(p -> color == null || color.isBlank() || p.getColor().equalsIgnoreCase(color))
                .filter(p -> size == null || size.isBlank() || String.valueOf(p.getSize()).equals(size))
                .filter(p -> campaign == null || campaign.isBlank() || p.getCampaign().equalsIgnoreCase(campaign))
                .filter(p -> area == null || area.isBlank() || p.getArea().equalsIgnoreCase(area))
                .map(productMapper::toProductBasicDTO)
                .collect(Collectors.toList());
    }

}