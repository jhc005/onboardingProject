package com.tienda_online.infrastructure.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tienda_online.domain.entities.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
