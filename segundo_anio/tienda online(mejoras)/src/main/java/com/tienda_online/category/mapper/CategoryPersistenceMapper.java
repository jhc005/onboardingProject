package com.tienda_online.category.mapper;

import org.springframework.stereotype.Component;
import com.tienda_online.category.domain.model.Category;
import com.tienda_online.category.infrastructure.output.jpa.CategoryJpaEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceMapper {


    public Category toDomain(CategoryJpaEntity entity) {
        Category category = new Category(entity.getId(), entity.getName());
        return category;
    }

    public CategoryJpaEntity toJpaEntity(Category category) {
        CategoryJpaEntity entity = new CategoryJpaEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        return entity;
    }
}