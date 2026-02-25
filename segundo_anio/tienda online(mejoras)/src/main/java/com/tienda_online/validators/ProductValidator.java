package com.tienda_online.validators;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.tienda_online.DTO.ProductDto;

@Component
public class ProductValidator {

    public void validateProductData(ProductDto productDto) {
        validateNonNullFields(productDto);
        validateProductName(productDto.getName());
        validateProductPrice(productDto.getPrice());
        validateProductStock(productDto.getStock());
        validateProductDescription(productDto.getDescription());
        validatePriceAndStock(productDto.getPrice(), productDto.getStock());
    }

    public void validateNonNullFields(ProductDto productDto) {
        if (productDto.getName() == null || productDto.getPrice() == null || productDto.getCategoryId() == null) {
            throw new RuntimeException("Invalid product data");
        }
    }

    public void validateProductName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Product name cannot be empty");
        }
        if (name.length() < 3 || name.length() > 100) {
            throw new RuntimeException("Product name must be between 3 and 100 characters");
        }
    }

    public void validateProductPrice(Double price) {
        if (price == null || price <= 0) {
            throw new RuntimeException("Product price must be greater than zero");
        }
    }

    public void validateProductStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new RuntimeException("Product stock cannot be negative");
        }
    }

    public void validateProductDescription(String description) {
        if (description != null && description.length() < 10 || description.length() > 1000) {
            throw new RuntimeException("Product description must be between 10 and 1000 characters");
        }
    }

    public void validateUpdateDate(LocalDateTime updateDate) {
        if (updateDate != null && updateDate.isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Update date cannot be in the future");
        }
    }

    public void validatePriceAndStock(Double price, Integer stock) {
        if (stock > 0 && (price == null || price <= 0)) {
            throw new RuntimeException("Product price must be greater than zero if stock is greater than zero");
        }
    }
}
