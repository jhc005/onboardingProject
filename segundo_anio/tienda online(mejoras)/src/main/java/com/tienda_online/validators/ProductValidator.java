package com.tienda_online.validators;

import org.springframework.stereotype.Component;

import com.tienda_online.DTO.ProductDto;

@Component
public class ProductValidator {

    public void validateProductData(ProductDto productDto) {
        validateProductName(productDto.getName());
        validatePriceAndStock(productDto.getPrice(), productDto.getStock());
    }

    public void validateProductName(String name) {
        if (name.length() < 3 || name.length() > 100) {
            throw new RuntimeException("Product name must be between 3 and 100 characters");
        }
    }

    public void validatePriceAndStock(Double price, Integer stock) {
        if (stock > 0 && (price == null || price <= 0)) {
            throw new RuntimeException("Product price must be greater than zero if stock is greater than zero");
        }
    }
}
