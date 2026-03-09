package com.tienda_online.product.mapper;

import org.springframework.stereotype.Component;

import com.tienda_online.product.domain.model.Product;
import com.tienda_online.product.infrastructure.output.jpa.ProductJpaEntity;
import com.tienda_online.category.mapper.CategoryPersistenceMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductPersistenceMapper {

    private final CategoryPersistenceMapper categoryMapper;

    public Product toDomain(ProductJpaEntity entity) {
        return new Product(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice(),
            categoryMapper.toDomain(entity.getCategory()),
            entity.getStock(),
            entity.getUpdateDate()
        );
    }

    public ProductJpaEntity toJpaEntity(Product product) {
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStock(product.getStock());
        entity.setUpdateDate(product.getUpdateDate());
        entity.setCategory(categoryMapper.toJpaEntity(product.getCategory()));
        return entity;
    }
}
