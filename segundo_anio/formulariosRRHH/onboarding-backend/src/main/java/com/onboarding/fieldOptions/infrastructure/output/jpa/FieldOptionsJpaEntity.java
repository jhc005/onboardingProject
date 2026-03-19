package com.onboarding.fieldOptions.infrastructure.output.jpa;

import com.onboarding.dataFields.infrastructure.output.jpa.DataFieldsJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "field_options")
@Getter
@Setter
public class FieldOptionsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_field_id", nullable = false)
    private DataFieldsJpaEntity dataField;

    @Column(name = "option_label", nullable = false, length = 100)
    private String optionLabel;

    @Column(name = "option_value", nullable = false, length = 100)
    private String optionValue;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;
}
