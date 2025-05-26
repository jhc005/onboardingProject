package com.tienda_online.infrastructure.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

import com.tienda_online.DTO.CategoryDto;
import com.tienda_online.application.services.CategoryService;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);

    }

    @PostMapping
    public ResponseEntity<ResponseEntity<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDto) {
        if (categoryDto.getName() == null || categoryDto.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        ResponseEntity<CategoryDto> createdCategory = categoryService.createCategory(categoryDto);
        return ResponseEntity.ok(createdCategory);
        
    }
    
    
}

