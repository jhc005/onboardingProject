package com.onboarding.documentRequirements.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentsReqJpaRepository extends JpaRepository<DocumentsReqJpaEntity, Long> {

    List<DocumentsReqJpaEntity> findAllByOrderByDisplayOrderAsc();
}
