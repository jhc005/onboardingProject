package com.tienda_online.infrastructure.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tienda_online.domain.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
} 
