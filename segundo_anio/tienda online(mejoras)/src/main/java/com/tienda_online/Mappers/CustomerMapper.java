package com.tienda_online.Mappers;

import org.mapstruct.Mapper;

import com.tienda_online.DTO.CustomerDto;
import com.tienda_online.domain.entities.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto toDto(Customer customer);

    Customer toEntity(CustomerDto customerDto);
}
