package com.tienda_online.validators;

import org.springframework.stereotype.Component;

import com.tienda_online.DTO.CustomerDto;

@Component
public class CustomerValidator {

    public void validateCustomerData(CustomerDto customerDto) {
        validateNonNullFields(customerDto);
        validateCustomerStrings(customerDto.getFirstName());
        validateCustomerStrings(customerDto.getLastName());
        validateEmail(customerDto.getEmail());
        validatePhoneNumber(customerDto.getPhone());
    }

    public void validateNonNullFields(CustomerDto customerDto) {
        if (customerDto.getFirstName() == null || customerDto.getLastName() == null || customerDto.getEmail() == null ||
            customerDto.getPhone() == null) {
            throw new RuntimeException("Invalid product data");
        }
    }

    public void validateCustomerStrings(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new RuntimeException("Product name cannot be empty");
        }
        if (string.length() < 3 || string.length() > 100) {
            throw new RuntimeException("Product name must be between 3 and 100 characters");
        }
    }

    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("Email cannot be empty");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.matches(emailRegex)) {
            throw new RuntimeException("Invalid email format");
        }
    }

    public void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new RuntimeException("Phone number cannot be empty");
        }
        if (phoneNumber.length() < 7) {
            throw new RuntimeException("Phone number must be at least 7 digits long");
        }
    }
}
