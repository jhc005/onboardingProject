package com.tienda_online.category.mapper;

import org.springframework.stereotype.Component;
import java.util.stream.Collectors;
import com.tienda_online.category.domain.model.Category;
import com.tienda_online.category.infrastructure.output.jpa.CategoryJpaEntity;
import com.tienda_online.product.mapper.ProductPersistenceMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceMapper {

    private final ProductPersistenceMapper productMapper;

    public Category toDomain(CategoryJpaEntity entity) {
        Category category = new Category(entity.getId(), entity.getName());
        if (entity.getProducts() != null) {
            entity.getProducts().forEach(p -> category.addProduct(productMapper.toDomain(p)));
        }
        return category;
    }

    public CategoryJpaEntity toJpaEntity(Category category) {
        CategoryJpaEntity entity = new CategoryJpaEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        if (category.getProducts() != null) {
            entity.setProducts(category.getProducts().stream()
                .map(productMapper::toJpaEntity)
                .collect(Collectors.toSet()));
        }
        return entity;
    }
}