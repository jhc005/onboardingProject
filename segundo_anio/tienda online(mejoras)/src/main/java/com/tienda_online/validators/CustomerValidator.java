package com.tienda_online.validators;

import org.springframework.stereotype.Component;

import com.tienda_online.DTO.CustomerDto;

@Component
public class CustomerValidator {

    public void validateCustomerData(CustomerDto customerDto) {
        validateCustomerStrings(customerDto.getFirstName());
        validateCustomerStrings(customerDto.getLastName());
        validateEmail(customerDto.getEmail());
        validatePhoneNumber(customerDto.getPhone());
    }

    public void validateCustomerStrings(String string) {
        if (string.length() < 3 || string.length() > 100) {
            throw new RuntimeException("Customer name must be between 3 and 100 characters");
        }
    }

    public void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.matches(emailRegex)) {
            throw new RuntimeException("Invalid email format");
        }
    }

    public void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 7) {
            throw new RuntimeException("Phone number must be at least 7 digits long");
        }
    }
}
