package com.tienda_online.order.infrastructure.output.adapter;

import java.util.List;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.tienda_online.order.domain.model.Order;
import com.tienda_online.order.domain.ports.out.OrderRepositoryPort;
import com.tienda_online.order.infrastructure.output.jpa.OrderJpaRepository;
import com.tienda_online.order.mapper.OrderPersistenceMapper;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository jpaRepository;
    private final OrderPersistenceMapper mapper;

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Order save(Order order) {
        var entity = mapper.toJpaEntity(order);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
