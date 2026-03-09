package com.tienda_online.order.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.tienda_online.order.domain.model.Order;
import com.tienda_online.order.infrastructure.output.jpa.OrderJpaEntity;
import com.tienda_online.order.application.dto.OrderDto;

import com.tienda_online.customer.mapper.CustomerPersistenceMapper;

@Component
@RequiredArgsConstructor
public class OrderPersistenceMapper {

    private final CustomerPersistenceMapper customerMapper;

    /* JPA → Domain */
    public Order toDomain(OrderJpaEntity entity) {
        return new Order(
                entity.getId(),
                customerMapper.toDomain(entity.getCustomer()),
                entity.getOrderDate(),
                entity.getStatus(),
                entity.getTotal()
        );
    }

    /* Domain → JPA */
    public OrderJpaEntity toJpaEntity(Order domain) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setId(domain.getId());
        entity.setCustomer(
                customerMapper.toJpaEntity(domain.getCustomer())
        );
        entity.setOrderDate(domain.getOrderDate());
        entity.setStatus(domain.getStatus());
        entity.setTotal(domain.getTotal());
        return entity;
    }

    /* Domain → DTO */
    public OrderDto toDto(Order domain) {
        return OrderDto.builder()
                .id(domain.getId())
                .customer(
                        customerMapper.toDto(domain.getCustomer())
                )
                .orderDate(domain.getOrderDate())
                .status(domain.getStatus())
                .total(domain.getTotal())
                .build();
    }
}
