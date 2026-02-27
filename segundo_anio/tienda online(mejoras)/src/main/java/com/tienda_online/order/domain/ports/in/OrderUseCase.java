package com.tienda_online.order.domain.ports.in;

import java.util.List;
import com.tienda_online.order.application.dto.OrderDto;

public interface OrderUseCase {

    List<OrderDto> getAll();

    OrderDto create(OrderDto dto);

}
