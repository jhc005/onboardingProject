package com.tienda_online.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tienda_online.DTO.CategoryDto;

import com.tienda_online.infrastructure.repositories.CategoryRepository;
import com.tienda_online.validators.CategoryValidator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;


    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .toList();
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        var category = new com.tienda_online.domain.entities.Category();
        categoryValidator.validateData(categoryDto);
        category.setName(categoryDto.getName());
        category = categoryRepository.save(category);
        
        return new CategoryDto(category.getId(), category.getName());
    }
}

