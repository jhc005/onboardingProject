package com.onboarding.dataAnswers.infrastructure.output.adapter;

import com.onboarding.dataAnswers.domain.model.DataAnswers;
import com.onboarding.dataAnswers.domain.outport.DataAnswersRepositoryPort;
import com.onboarding.dataAnswers.infrastructure.output.jpa.DataAnswersJpaEntity;
import com.onboarding.dataAnswers.infrastructure.output.jpa.DataAnswersJpaRepository;
import com.onboarding.dataAnswers.mapper.DataAnswersMapper;
import com.onboarding.dataFields.infrastructure.output.jpa.DataFieldsJpaRepository;
import com.onboarding.fieldOptions.infrastructure.output.jpa.FieldOptionsJpaRepository;
import com.onboarding.onboardingProcesses.infrastructure.output.jpa.OnboardingProcJpaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataAnswersAdapter implements DataAnswersRepositoryPort {

    private final DataAnswersJpaRepository repository;
    private final OnboardingProcJpaRepository processRepository;
    private final DataFieldsJpaRepository dataFieldsRepository;
    private final FieldOptionsJpaRepository fieldOptionsRepository;
    private final DataAnswersMapper mapper;

    @Override
    public DataAnswers save(DataAnswers answer) {
        DataAnswersJpaEntity entity = mapper.toEntity(answer);
        if (answer.getOnboardingProcessId() != null) {
            processRepository.findById(answer.getOnboardingProcessId()).ifPresent(entity::setOnboardingProcess);
        }
        if (answer.getDataFieldId() != null) {
            dataFieldsRepository.findById(answer.getDataFieldId()).ifPresent(entity::setDataField);
        }
        if (answer.getFieldOptionId() != null) {
            fieldOptionsRepository.findById(answer.getFieldOptionId()).ifPresent(entity::setFieldOption);
        }
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<DataAnswers> saveAll(List<DataAnswers> answers) {
        return answers.stream().map(this::save).toList();
    }

    @Override
    public Optional<DataAnswers> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<DataAnswers> findByOnboardingProcessId(String onboardingProcessId) {
        return repository.findByOnboardingProcess_Id(onboardingProcessId)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<DataAnswers> findByOnboardingProcessIdAndDataFieldId(String processId, Long dataFieldId) {
        return repository.findByOnboardingProcess_IdAndDataField_Id(processId, dataFieldId)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteByOnboardingProcessId(String onboardingProcessId) {
        repository.deleteByOnboardingProcess_Id(onboardingProcessId);
    }
}
