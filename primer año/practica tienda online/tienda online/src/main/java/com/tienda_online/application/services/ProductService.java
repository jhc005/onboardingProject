package com.tienda_online.application.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.tienda_online.infrastructure.repositories.ProductRepository;
import com.tienda_online.DTO.ProductDto;
import com.tienda_online.Mappers.ProductMapper;
import com.tienda_online.domain.entities.Product;
import com.tienda_online.infrastructure.repositories.CategoryRepository;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public List<ProductDto> getAllProducts(@RequestParam(name = "categoryId", required = false) Long categoryId) {
        List<Product> products = (categoryId != null) 
            ? productRepository.findByCategoryId(categoryId)
            : productRepository.findAll();

        return products.stream().map(productMapper::toDto).toList();
    }

    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(productMapper.toDto(product)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto, UriComponentsBuilder uriBuilder) {
        if (productDto.getName() == null || productDto.getPrice() == null || productDto.getCategoryId() == null) {
            return ResponseEntity.badRequest().build();
        }

        var category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + productDto.getCategoryId()));

        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);
        product.setUpdateDate(LocalDateTime.now());

        product = productRepository.save(product);

        var savedProductDto = productMapper.toDto(product);

        var uri = uriBuilder.path("/products/{id}")
                .buildAndExpand(savedProductDto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(savedProductDto);
    }

    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Optional<Product> existingProductOpt = productRepository.findById(id);
        Optional<com.tienda_online.domain.entities.Category> categoryOpt = categoryRepository.findById(productDto.getCategoryId());

        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (existingProductOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product product = existingProductOpt.get();
        productMapper.update(productDto, product);
        product.setCategory(categoryOpt.get());
        product.setUpdateDate(LocalDateTime.now());

        product = productRepository.save(product);
        var updatedProductDto = productMapper.toDto(product);

        return ResponseEntity.ok(updatedProductDto);
    }

    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(productOpt.get());

        return ResponseEntity.noContent().build();
    }
}