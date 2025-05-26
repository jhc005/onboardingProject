package com.tienda_online.infrastructure.repositories;
import com.tienda_online.domain.entities.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product>findByCategoryId(Long categoryId);  
} 
