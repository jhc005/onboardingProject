package com.onboarding.fieldOptions.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldOptionsJpaRepository extends JpaRepository<FieldOptionsJpaEntity, Long> {

    List<FieldOptionsJpaEntity> findByDataField_IdOrderByDisplayOrderAsc(Long dataFieldId);
    void deleteByDataField_Id(Long dataFieldId);
}
