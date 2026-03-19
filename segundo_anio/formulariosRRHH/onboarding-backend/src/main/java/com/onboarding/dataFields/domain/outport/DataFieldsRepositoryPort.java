package com.onboarding.dataFields.domain.outport;

import com.onboarding.dataFields.domain.model.DataFields;

import java.util.List;
import java.util.Optional;

public interface DataFieldsRepositoryPort {

    DataFields save(DataFields dataField);
    Optional<DataFields> findById(Long id);
    List<DataFields> findAll();
    List<DataFields> findAllOrderedByDisplayOrder();
    void deleteById(Long id);
}
