package com.tienda_online.validators;

import org.springframework.stereotype.Component;

import com.tienda_online.DTO.CategoryDto;

@Component
public class CategoryValidator {

    public void validateData(CategoryDto categoryDto) {
        validateProductName(categoryDto.getName());
    }

    public void validateProductName(String name) {
        if (name.length() < 3 || name.length() > 100) {
            throw new RuntimeException("Category name must be between 3 and 100 characters");
        }
    }
}
