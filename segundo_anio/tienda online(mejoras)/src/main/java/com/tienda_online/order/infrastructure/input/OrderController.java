package com.tienda_online.order.infrastructure.input;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.tienda_online.order.application.dto.OrderDto;
import com.tienda_online.order.application.inport.OrderUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderUseCase orderUseCase;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAll() {
        return ResponseEntity.ok(orderUseCase.getAll());
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@Valid @RequestBody OrderDto dto) {
        return ResponseEntity.ok(orderUseCase.create(dto));
    }
}
