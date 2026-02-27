package com.tienda_online.order.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository
        extends JpaRepository<OrderJpaEntity, Long> {
}
