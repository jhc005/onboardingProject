package com.tienda_online.customer.infrastructure.input;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.tienda_online.customer.application.CustomerService;
import com.tienda_online.customer.application.dto.CustomerDto;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @PostMapping
    public ResponseEntity<CustomerDto> create(@Valid @RequestBody CustomerDto dto) {
        return ResponseEntity.ok(customerService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> update(@PathVariable Long id,
                                              @Valid @RequestBody CustomerDto dto) {
        return ResponseEntity.ok(customerService.update(id, dto));
    }
}
