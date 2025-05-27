package com.tienda_online.application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tienda_online.domain.entities.Orders;
import com.tienda_online.infrastructure.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders createOrders(Orders order){
        if (order.getOrderDate() == null) {
            order.setOrderDate(java.time.LocalDateTime.now());
        }
        return orderRepository.save(order);
    }
}