package com.tienda_online.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tienda_online.DTO.OrderDto;
import com.tienda_online.Mappers.OrderMapper;
import com.tienda_online.domain.entities.Customer;
import com.tienda_online.domain.entities.Orders;
import com.tienda_online.infrastructure.repositories.CustomerRepository;
import com.tienda_online.infrastructure.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
            .stream()
            .map(orderMapper::toDto)
            .toList();
    }

    public OrderDto createOrder(OrderDto orderDto) {
        Customer customer = customerRepository.findById(orderDto.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found with id: " + orderDto.getCustomerId()));

        Orders order = orderMapper.toEntity(orderDto);
        order.setCustomer(customer);

        if (order.getOrderDate() == null) {
            order.setOrderDate(null);
        }

        Orders savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }
}

