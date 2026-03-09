package com.tienda_online.order.domain.model;

import java.time.LocalDateTime;
import com.tienda_online.customer.domain.model.Customer;

public class Order {

    private Long id;
    private Customer customer;
    private LocalDateTime orderDate;
    private String status;
    private double total;

    public Order(Long id,
                 Customer customer,
                 LocalDateTime orderDate,
                 String status,
                 double total) {

        this.id = id;
        this.customer = customer;
        this.orderDate = orderDate;
        this.status = status;
        this.total = total;
    }

    public Order(Customer customer,
                 String status,
                 double total) {

        this(null, customer, LocalDateTime.now(), status, total);
    }

    public void update(String status, double total) {
        this.status = status;
        this.total = total;
    }

    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getStatus() { return status; }
    public double getTotal() { return total; }
}