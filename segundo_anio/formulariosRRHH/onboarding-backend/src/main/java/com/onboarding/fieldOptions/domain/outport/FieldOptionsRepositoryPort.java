package com.onboarding.fieldOptions.domain.outport;

import com.onboarding.fieldOptions.domain.model.FieldOptions;

import java.util.List;
import java.util.Optional;

public interface FieldOptionsRepositoryPort {

    FieldOptions save(FieldOptions fieldOption);
    Optional<FieldOptions> findById(Long id);
    List<FieldOptions> findByDataFieldId(Long dataFieldId);
    void deleteById(Long id);
    void deleteByDataFieldId(Long dataFieldId);
}
