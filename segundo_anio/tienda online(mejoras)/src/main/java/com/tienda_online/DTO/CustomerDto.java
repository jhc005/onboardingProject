package com.tienda_online.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    @NotBlank(message = "Category name cannot be empty")
    private String firstName;
    @NotBlank(message = "Category last name cannot be empty")
    private String lastName;
    @NotBlank(message = "Category email cannot be empty")
    private String email;
    @NotBlank(message = "Category phone cannot be empty")
    private String phone;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;
    private boolean active;

}
