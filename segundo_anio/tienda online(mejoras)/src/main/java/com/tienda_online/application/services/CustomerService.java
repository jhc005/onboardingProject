package com.tienda_online.application.services;

import java.util.List;
import java.util.Optional;

import com.tienda_online.DTO.CustomerDto;
import com.tienda_online.Mappers.CustomerMapper;
import com.tienda_online.domain.entities.Customer;
import com.tienda_online.infrastructure.repositories.CustomerRepository;
import com.tienda_online.validators.CustomerValidator;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerValidator customerValidator;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, CustomerValidator customerValidator) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.customerValidator = customerValidator;
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
            .map(customerMapper::toDto)
            .toList();
    }
    public CustomerDto createCustomer (CustomerDto customerDto) {
        Customer customer = customerMapper.toEntity(customerDto);

        customerValidator.validateCustomerData(customerDto);

        customer = customerRepository.save(customer);

        return customerMapper.toDto(customer);
    }

    public Optional <CustomerDto> updateCustomer(Long id, CustomerDto customerDtoDetails) {

        customerValidator.validateCustomerData(customerDtoDetails);

        return customerRepository.findById(id)
            .map(existingCustomer -> {
                existingCustomer.setFirstName(customerDtoDetails.getFirstName());
                existingCustomer.setLastName(customerDtoDetails.getLastName());
                existingCustomer.setEmail(customerDtoDetails.getEmail());
                existingCustomer.setPhone(customerDtoDetails.getPhone());
                existingCustomer.setRegistrationDate(customerDtoDetails.getRegistrationDate());
                existingCustomer.setActive(customerDtoDetails.isActive());

                Customer updatedCustomer = customerRepository.save(existingCustomer);

                return Optional.of(customerMapper.toDto(updatedCustomer));
            }).orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }
}
