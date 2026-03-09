package com.tienda_online.product.application.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Double price;

    @NotNull
    private Long categoryId;

    @NotNull
    private Integer stock;

    private LocalDateTime updateDate;
}
