package com.onboarding.fieldOptions.domain.outport;

import com.onboarding.fieldOptions.domain.model.FieldOptions;

import java.util.List;

public interface FieldOptionsRepositoryPort {

    FieldOptions save(FieldOptions fieldOption);
    List<FieldOptions> findByDataFieldId(Long dataFieldId);
    void deleteById(Long id);
    void deleteByDataFieldId(Long dataFieldId);
}
