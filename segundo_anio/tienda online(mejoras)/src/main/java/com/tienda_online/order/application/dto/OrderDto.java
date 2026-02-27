package com.tienda_online.order.application.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import com.tienda_online.customer.application.dto.CustomerDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;
    @NotNull(message = "Customer is required")
    private CustomerDto customer;
    private LocalDateTime orderDate;
    @NotBlank(message = "Status is required")
    private String status;
    @NotBlank(message = "Total is required")
    private double total;
}