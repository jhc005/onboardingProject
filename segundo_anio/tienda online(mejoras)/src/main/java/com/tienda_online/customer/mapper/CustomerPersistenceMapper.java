package com.tienda_online.customer.mapper;

import org.springframework.stereotype.Component;

import com.tienda_online.customer.domain.model.Customer;
import com.tienda_online.customer.infrastructure.output.jpa.CustomerJpaEntity;
import com.tienda_online.customer.application.dto.CustomerDto;

@Component
public class CustomerPersistenceMapper {

    public Customer toDomain(CustomerJpaEntity entity) {
        return new Customer(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getRegistrationDate(),
                entity.isActive()
        );
    }

    public CustomerJpaEntity toJpaEntity(Customer domain) {
        CustomerJpaEntity entity = new CustomerJpaEntity();
        entity.setId(domain.getId());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setRegistrationDate(domain.getRegistrationDate());
        entity.setActive(domain.isActive());
        return entity;
    }

    public CustomerDto toDto(Customer domain) {
        return CustomerDto.builder()
                .id(domain.getId())
                .firstName(domain.getFirstName())
                .lastName(domain.getLastName())
                .email(domain.getEmail())
                .phone(domain.getPhone())
                .registrationDate(domain.getRegistrationDate())
                .active(domain.isActive())
                .build();
    }
}
