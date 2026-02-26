package com.tienda_online.category.domain.ports.in;

import java.util.List;

import com.tienda_online.category.application.dto.CategoryDto;

public interface CategoryUseCase {
    List<CategoryDto> getAllCategories();

    CategoryDto createCategory(CategoryDto categoryDto);
}
