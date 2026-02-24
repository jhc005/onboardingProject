package com.tienda_online.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tienda_online.DTO.OrderDto;
import com.tienda_online.Mappers.OrderMapper;
import com.tienda_online.domain.entities.Order;
import com.tienda_online.infrastructure.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper; 

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> orderMapper.toDto(order))
                .toList();
    }

    public OrderDto createOrders(OrderDto orderDto){
        Order order = orderMapper.toEntity(orderDto);

        if(order.getOrderDate() == null){
            order.setOrderDate(java.time.LocalDateTime.now());
        }

        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }
}