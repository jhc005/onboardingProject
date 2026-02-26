package com.tienda_online.customer.domain.ports.in;

import java.util.List;
import com.tienda_online.customer.application.dto.CustomerDto;

public interface CustomerUseCase {

    List<CustomerDto> getAll();

    CustomerDto create(CustomerDto dto);

    CustomerDto update(Long id, CustomerDto dto);

}
