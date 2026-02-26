package com.tienda_online.category.infrastructure.output.jpa;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository
        extends JpaRepository<CategoryJpaEntity, Long> {
}
