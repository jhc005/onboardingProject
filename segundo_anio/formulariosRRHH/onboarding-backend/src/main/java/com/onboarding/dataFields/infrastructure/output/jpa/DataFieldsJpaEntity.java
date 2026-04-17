package com.onboarding.dataFields.infrastructure.output.jpa;

import com.onboarding.dataFields.domain.valueobject.FieldType;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "data_fields")
@Getter
@Setter
public class DataFieldsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false)
    private FieldType fieldType;

    @Column(length = 255)
    private String placeholder;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired = true;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

}
