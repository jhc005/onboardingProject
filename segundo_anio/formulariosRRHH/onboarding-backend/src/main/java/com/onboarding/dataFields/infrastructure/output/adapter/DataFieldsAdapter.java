package com.onboarding.dataFields.infrastructure.output.adapter;

import com.onboarding.dataFields.domain.model.DataFields;
import com.onboarding.dataFields.domain.outport.DataFieldsRepositoryPort;
import com.onboarding.dataFields.infrastructure.output.jpa.DataFieldsJpaRepository;
import com.onboarding.dataFields.mapper.DataFieldsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataFieldsAdapter implements DataFieldsRepositoryPort {

    private final DataFieldsJpaRepository repository;
    private final DataFieldsMapper mapper;

    @Override
    public DataFields save(DataFields dataField) {
        return mapper.toDomain(repository.save(mapper.toEntity(dataField)));
    }

    @Override
    public Optional<DataFields> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<DataFields> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<DataFields> findAllOrderedByDisplayOrder() {
        return repository.findAllByOrderByDisplayOrderAsc().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
