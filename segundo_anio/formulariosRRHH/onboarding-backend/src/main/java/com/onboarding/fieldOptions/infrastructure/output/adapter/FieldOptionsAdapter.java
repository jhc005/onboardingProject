package com.onboarding.fieldOptions.infrastructure.output.adapter;

import com.onboarding.dataFields.infrastructure.output.jpa.DataFieldsJpaRepository;
import com.onboarding.fieldOptions.domain.model.FieldOptions;
import com.onboarding.fieldOptions.domain.outport.FieldOptionsRepositoryPort;
import com.onboarding.fieldOptions.infrastructure.output.jpa.FieldOptionsJpaEntity;
import com.onboarding.fieldOptions.infrastructure.output.jpa.FieldOptionsJpaRepository;
import com.onboarding.fieldOptions.mapper.FieldOptionsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FieldOptionsAdapter implements FieldOptionsRepositoryPort {

    private final FieldOptionsJpaRepository repository;
    private final DataFieldsJpaRepository dataFieldsRepository;
    private final FieldOptionsMapper mapper;

    @Override
    public FieldOptions save(FieldOptions fieldOption) {
        FieldOptionsJpaEntity entity = mapper.toEntity(fieldOption);
        if (fieldOption.getDataFieldId() != null) {
            dataFieldsRepository.findById(fieldOption.getDataFieldId()).ifPresent(entity::setDataField);
        }
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<FieldOptions> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<FieldOptions> findByDataFieldId(Long dataFieldId) {
        return repository.findByDataField_IdOrderByDisplayOrderAsc(dataFieldId)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByDataFieldId(Long dataFieldId) {
        repository.deleteByDataField_Id(dataFieldId);
    }
}
