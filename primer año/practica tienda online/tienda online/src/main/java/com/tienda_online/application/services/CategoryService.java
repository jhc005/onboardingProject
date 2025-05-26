package com.tienda_online.application.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tienda_online.DTO.CategoryDto;

import com.tienda_online.infrastructure.repositories.CategoryRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;


    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .toList();
    }

    public ResponseEntity<CategoryDto> createCategory(CategoryDto categoryDto) {
        if (categoryDto.getName() == null) {
            return ResponseEntity.badRequest().build();
        }

        var category = new com.tienda_online.domain.entities.Category();
        category.setName(categoryDto.getName());
        category = categoryRepository.save(category);
        
        var savedCategoryDto = new CategoryDto(category.getId(), category.getName());
        return ResponseEntity.ok(savedCategoryDto);
        
    }
}

