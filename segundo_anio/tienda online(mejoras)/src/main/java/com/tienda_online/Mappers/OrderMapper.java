package com.tienda_online.Mappers;

import org.mapstruct.Mapper;

import com.tienda_online.DTO.OrderDto;
import com.tienda_online.domain.entities.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);

    Order toEntity(OrderDto orderDto);
}
