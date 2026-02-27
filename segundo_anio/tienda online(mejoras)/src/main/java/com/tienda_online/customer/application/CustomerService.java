package com.tienda_online.customer.application;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.tienda_online.customer.domain.model.Customer;
import com.tienda_online.customer.domain.outport.CustomerRepositoryPort;
import com.tienda_online.customer.application.dto.CustomerDto;
import com.tienda_online.customer.application.inport.CustomerUseCase;
import com.tienda_online.customer.mapper.CustomerPersistenceMapper;

@Service
@RequiredArgsConstructor
public class CustomerService implements CustomerUseCase {

    private final CustomerRepositoryPort repository;
    private final CustomerPersistenceMapper mapper;

    @Override
    public List<CustomerDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public CustomerDto create(CustomerDto dto) {

        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already regis");
        }

        Customer customer = new Customer(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPhone());

        Customer saved = repository.save(customer);
        return mapper.toDto(saved);
    }

    @Override
    public CustomerDto update(Long id, CustomerDto dto) {

        Customer customer = repository.findById(id);

        if (customer == null) {
        throw new RuntimeException("Customer not found");
    }

    Customer emailOwner = repository.findByEmail(dto.getEmail());
    if (emailOwner != null && !emailOwner.getId().equals(id)) {
        throw new RuntimeException("Email already registered");
    }

        customer.update(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.isActive());

        Customer updated = repository.save(customer);
        return mapper.toDto(updated);
    }

}
