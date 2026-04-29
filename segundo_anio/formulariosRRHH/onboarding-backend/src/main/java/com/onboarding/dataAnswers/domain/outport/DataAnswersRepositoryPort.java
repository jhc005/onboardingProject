package com.onboarding.dataAnswers.domain.outport;

import com.onboarding.dataAnswers.domain.model.DataAnswers;

import java.util.List;
import java.util.Optional;

public interface DataAnswersRepositoryPort {

    DataAnswers save(DataAnswers answer);
    List<DataAnswers> findByOnboardingProcessId(String onboardingProcessId);
    Optional<DataAnswers> findByOnboardingProcessIdAndDataFieldId(String onboardingProcessId, Long dataFieldId);
    void deleteByDataFieldId(Long dataFieldId);
}
