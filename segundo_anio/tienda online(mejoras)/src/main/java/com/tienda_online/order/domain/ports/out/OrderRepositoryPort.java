package com.tienda_online.order.domain.ports.out;

import java.util.List;
import com.tienda_online.order.domain.model.Order;

public interface OrderRepositoryPort {

    List<Order> findAll();

    Order save(Order order);
}
