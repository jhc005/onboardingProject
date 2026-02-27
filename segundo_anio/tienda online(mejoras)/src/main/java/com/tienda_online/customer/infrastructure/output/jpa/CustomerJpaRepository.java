package com.tienda_online.customer.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, Long> {
        boolean existsByEmail(String email);

        boolean existsByEmailAndIdNot(String email, Long id);
}
