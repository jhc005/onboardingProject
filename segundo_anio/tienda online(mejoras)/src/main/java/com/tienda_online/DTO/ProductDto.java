package com.tienda_online.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    @NotBlank(message = "Product name cannot be empty")
    private String name;
    private String description;
    @NotBlank(message = "Product price cannot be empty")
    private Double price;
    @NotBlank(message = "Product category cannot be unselected")
    private Long categoryId;
    @NotBlank(message = "Product stock cannot be 0")
    private Integer stock;
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;
}
