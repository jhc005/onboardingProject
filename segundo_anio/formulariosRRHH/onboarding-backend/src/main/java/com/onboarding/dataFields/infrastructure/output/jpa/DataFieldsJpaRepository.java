package com.onboarding.dataFields.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataFieldsJpaRepository extends JpaRepository<DataFieldsJpaEntity, Long> {

    List<DataFieldsJpaEntity> findAllByOrderByDisplayOrderAsc();
}
