package com.tienda_online.customer.domain.ports.out;

import java.util.List;
import com.tienda_online.customer.domain.model.Customer;

public interface CustomerRepositoryPort {

    List<Customer> findAll();

    Customer findById(Long id);

    Customer save(Customer customer);

}
