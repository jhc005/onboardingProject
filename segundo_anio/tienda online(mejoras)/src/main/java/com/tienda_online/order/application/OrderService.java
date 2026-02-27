package com.tienda_online.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.tienda_online.order.domain.model.Order;
import com.tienda_online.order.domain.ports.in.OrderUseCase;
import com.tienda_online.order.domain.ports.out.OrderRepositoryPort;
import com.tienda_online.order.application.dto.OrderDto;
import com.tienda_online.order.mapper.OrderPersistenceMapper;

import com.tienda_online.customer.domain.model.Customer;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {

    private final OrderRepositoryPort repository;
    private final OrderPersistenceMapper mapper;

    @Override
    public List<OrderDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public OrderDto create(OrderDto dto) {

        Customer customer = new Customer(
                dto.getCustomer().getId(),
                dto.getCustomer().getFirstName(),
                dto.getCustomer().getLastName(),
                dto.getCustomer().getEmail(),
                dto.getCustomer().getPhone(),
                dto.getCustomer().getRegistrationDate(),
                dto.getCustomer().isActive()
        );

        Order order = new Order(
                customer,
                dto.getStatus(),
                dto.getTotal()
        );

        Order saved = repository.save(order);
        return mapper.toDto(saved);
    }
}