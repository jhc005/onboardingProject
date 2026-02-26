package com.tienda_online.product.infrastructure.output.jpa;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.tienda_online.category.infrastructure.output.jpa.CategoryJpaEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="products")
@Getter
@Setter
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double price;

    private Integer stock;

    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name="category_id")
    private CategoryJpaEntity category;
}
