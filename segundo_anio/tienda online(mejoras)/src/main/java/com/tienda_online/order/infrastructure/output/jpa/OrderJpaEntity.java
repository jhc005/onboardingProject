package com.tienda_online.order.infrastructure.output.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.tienda_online.customer.infrastructure.output.jpa.CustomerJpaEntity;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="customer_id")
    private CustomerJpaEntity customer;
    private LocalDateTime orderDate;
    private String status;
    private double total;
}
