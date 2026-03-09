package com.tienda_online.category.infrastructure.input;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tienda_online.category.application.CategoryService;
import com.tienda_online.category.application.dto.CategoryDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(
            @Valid @RequestBody CategoryDto dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(dto));
    }
}
