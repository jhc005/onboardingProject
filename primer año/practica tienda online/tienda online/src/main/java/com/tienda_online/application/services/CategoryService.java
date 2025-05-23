package com.tienda_online.application.services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.tienda_online.domain.entities.Category;
import com.tienda_online.infrastructure.repositories.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}
