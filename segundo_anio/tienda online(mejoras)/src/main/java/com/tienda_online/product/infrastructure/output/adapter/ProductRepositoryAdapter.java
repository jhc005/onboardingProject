package com.tienda_online.product.infrastructure.output.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.tienda_online.product.domain.model.Product;
import com.tienda_online.product.domain.ports.out.ProductRepositoryPort;
import com.tienda_online.product.infrastructure.output.jpa.ProductJpaEntity;
import com.tienda_online.product.infrastructure.output.jpa.ProductJpaRepository;
import com.tienda_online.product.mapper.ProductPersistenceMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository jpaRepository;
    private final ProductPersistenceMapper mapper;

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return jpaRepository.findByCategory_Id(categoryId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Product findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = mapper.toJpaEntity(product);
        ProductJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(Product product) {
        ProductJpaEntity entity = mapper.toJpaEntity(product);
        jpaRepository.delete(entity);
    }
}
