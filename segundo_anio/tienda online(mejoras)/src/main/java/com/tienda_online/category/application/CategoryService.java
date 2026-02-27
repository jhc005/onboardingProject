package com.tienda_online.category.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tienda_online.category.application.dto.CategoryDto;
import com.tienda_online.category.application.inport.CategoryUseCase;
import com.tienda_online.category.domain.model.Category;
import com.tienda_online.category.domain.model.outport.CategoryRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryUseCase {

    private final CategoryRepositoryPort repository;

    @Override
    public List<CategoryDto> getAllCategories() {
        return repository.findAll()
                .stream()
                .map(category ->
                        new CategoryDto(category.getId(), category.getName()))
                .toList();
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category category = new Category(categoryDto.getName());

        Category saved = repository.save(category);

        return new CategoryDto(saved.getId(), saved.getName());
    }
}
