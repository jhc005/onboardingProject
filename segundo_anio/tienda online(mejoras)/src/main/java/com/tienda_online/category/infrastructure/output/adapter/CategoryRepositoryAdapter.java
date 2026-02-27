package com.tienda_online.category.infrastructure.output.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.tienda_online.category.domain.model.Category;
import com.tienda_online.category.domain.model.outport.CategoryRepositoryPort;
import com.tienda_online.category.infrastructure.output.jpa.CategoryJpaRepository;
import com.tienda_online.category.mapper.CategoryPersistenceMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository jpaRepository;
    private final CategoryPersistenceMapper mapper;

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Category findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Override
    public Category save(Category category) {
        var entity = mapper.toJpaEntity(category);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
