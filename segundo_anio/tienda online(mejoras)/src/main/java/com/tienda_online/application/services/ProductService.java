package com.tienda_online.application.services;

import org.springframework.stereotype.Service;

import com.tienda_online.infrastructure.repositories.ProductRepository;
import com.tienda_online.validators.ProductValidator;
import com.tienda_online.DTO.ProductDto;
import com.tienda_online.Mappers.ProductMapper;
import com.tienda_online.domain.entities.Product;
import com.tienda_online.infrastructure.repositories.CategoryRepository;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ProductValidator productValidator;

    public List<ProductDto> getAllProducts(Long categoryId) {
        List<Product> products = (categoryId != null) 
            ? productRepository.findByCategoryId(categoryId)
            : productRepository.findAll();

        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public ProductDto createProduct(ProductDto productDto) {
        productValidator.validateProductData(productDto);

        var category = getCategory(productDto.getCategoryId());

        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);
        product.setUpdateDate(LocalDateTime.now());

        product = productRepository.save(product);

        return productMapper.toDto(product);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        productValidator.validateProductData(productDto);

        var category = getCategory(productDto.getCategoryId());

        productMapper.update(productDto, existingProduct);
        existingProduct.setCategory(category);
        existingProduct.setUpdateDate(LocalDateTime.now());

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        productRepository.delete(product);
    }
    
    private com.tienda_online.domain.entities.Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
    }
}