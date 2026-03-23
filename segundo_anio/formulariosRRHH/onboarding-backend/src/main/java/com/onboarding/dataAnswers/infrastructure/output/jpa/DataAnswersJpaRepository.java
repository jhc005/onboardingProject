package com.onboarding.dataAnswers.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DataAnswersJpaRepository extends JpaRepository<DataAnswersJpaEntity, Long> {

    List<DataAnswersJpaEntity> findByOnboardingProcess_Id(String onboardingProcessId);
    Optional<DataAnswersJpaEntity> findByOnboardingProcess_IdAndDataField_Id(String processId, Long dataFieldId);
    void deleteByOnboardingProcess_Id(String onboardingProcessId);
    void deleteByDataField_Id(Long dataFieldId);
}
