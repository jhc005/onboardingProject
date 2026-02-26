package com.tienda_online.customer.infrastructure.output.adapter;

import java.util.List;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.tienda_online.customer.domain.model.Customer;
import com.tienda_online.customer.domain.ports.out.CustomerRepositoryPort;
import com.tienda_online.customer.infrastructure.output.jpa.CustomerJpaRepository;
import com.tienda_online.customer.mapper.CustomerPersistenceMapper;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final CustomerJpaRepository jpaRepository;
    private final CustomerPersistenceMapper mapper;

    @Override
    public List<Customer> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Customer findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found with id " + id));
    }

    @Override
    public Customer save(Customer customer) {
        var entity = mapper.toJpaEntity(customer);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

}
