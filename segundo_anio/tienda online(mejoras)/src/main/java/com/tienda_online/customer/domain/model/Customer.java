package com.tienda_online.customer.domain.model;

import java.time.LocalDateTime;

public class Customer {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime registrationDate;
    private boolean active;

    public Customer(Long id, String firstName, String lastName,
                    String email, String phone,
                    LocalDateTime registrationDate,
                    boolean active) {

        validate(firstName, lastName, email, phone);

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.registrationDate = registrationDate;
        this.active = active;
    }

    public Customer(String firstName, String lastName,
                    String email, String phone) {

        this(null, firstName, lastName, email, phone,
                LocalDateTime.now(), true);
    }

    private void validate(String firstName, String lastName,
                          String email, String phone) {

        if (firstName == null || firstName.length() < 3)
            throw new IllegalArgumentException("Invalid first name");

        if (lastName == null || lastName.length() < 3)
            throw new IllegalArgumentException("Invalid last name");

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
            throw new IllegalArgumentException("Invalid email");

        if (phone == null || phone.length() < 7)
            throw new IllegalArgumentException("Invalid phone");
    }

    public void update(String firstName, String lastName,
                       String email, String phone, boolean active) {

        validate(firstName, lastName, email, phone);

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public boolean isActive() { return active; }
}
