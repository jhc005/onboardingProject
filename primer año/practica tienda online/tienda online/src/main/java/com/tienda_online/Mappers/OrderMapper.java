package com.tienda_online.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.tienda_online.DTO.OrderDto;
import com.tienda_online.domain.entities.Orders;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "customerId", source = "customer.id")
    OrderDto toDto(Orders order);

    @Mapping(target = "customer", ignore = true) // Lo setearemos manualmente en el service
    Orders toEntity(OrderDto orderDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void update(OrderDto orderDto, @MappingTarget Orders order);
}

