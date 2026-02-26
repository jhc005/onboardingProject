package com.tienda_online.category.infrastructure.output.jpa;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.tienda_online.product.infrastructure.output.jpa.ProductJpaEntity;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class CategoryJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductJpaEntity> products = new HashSet<>();
}
