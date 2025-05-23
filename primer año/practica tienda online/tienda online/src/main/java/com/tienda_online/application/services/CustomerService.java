package com.tienda_online.application.services;

import java.util.List;
import java.util.Optional;

import com.tienda_online.domain.entities.Customer;
import com.tienda_online.infrastructure.repositories.CustomerRepository;

import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    public Customer createCustomer (Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional <Customer> updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
            .map(customer -> {
                customer.setFirstName(customerDetails.getFirstName());
                customer.setLastName(customerDetails.getLastName());
                customer.setEmail(customerDetails.getEmail());
                customer.setPhone(customerDetails.getPhone());
                customer.setRegistrationDate(customerDetails.getRegistrationDate());
                customer.setActive(customerDetails.isActive());
                return customerRepository.save(customer);
            });
    }
}
