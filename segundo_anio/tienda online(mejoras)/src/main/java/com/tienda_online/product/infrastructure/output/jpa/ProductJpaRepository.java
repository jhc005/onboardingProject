package com.tienda_online.product.infrastructure.output.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {
    List<ProductJpaEntity> findByCategory_Id(Long categoryId);
}
