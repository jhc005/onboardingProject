package com.onboarding.dataFields.application;

import com.onboarding.dataFields.application.dto.DataFieldsDto;
import com.onboarding.dataFields.domain.model.DataFields;
import com.onboarding.dataFields.domain.outport.DataFieldsRepositoryPort;
import com.onboarding.dataFields.domain.valueobject.FieldType;
import com.onboarding.dataFields.mapper.DataFieldsMapper;
import com.onboarding.fieldOptions.domain.outport.FieldOptionsRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataFieldsService {

    private final DataFieldsRepositoryPort dataFieldsPort;
    private final FieldOptionsRepositoryPort fieldOptionsPort;
    private final DataFieldsMapper mapper;

    public List<DataFieldsDto.Response> findAll() {
        return dataFieldsPort.findAllOrderedByDisplayOrder().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public DataFieldsDto.Response findById(Long id) {
        DataFields field = dataFieldsPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campo no encontrado."));
        return mapper.toResponse(field);
    }

    public DataFieldsDto.Response create(DataFieldsDto.Request request) {
        DataFields field = new DataFields();
        fillFromRequest(field, request);
        return mapper.toResponse(dataFieldsPort.save(field));
    }

    public DataFieldsDto.Response update(Long id, DataFieldsDto.Request request) {
        DataFields field = dataFieldsPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Campo no encontrado."));
        fillFromRequest(field, request);
        return mapper.toResponse(dataFieldsPort.save(field));
    }

    public void delete(Long id) {
        fieldOptionsPort.deleteByDataFieldId(id);
        dataFieldsPort.deleteById(id);
    }

    private void fillFromRequest(DataFields field, DataFieldsDto.Request request) {
        field.setLabel(request.getLabel());
        field.setFieldType(FieldType.valueOf(request.getFieldType()));
        field.setValidationRegex(request.getValidationRegex());
        field.setMinLength(request.getMinLength());
        field.setMaxLength(request.getMaxLength());
        field.setPlaceholder(request.getPlaceholder());
        field.setIsRequired(request.getIsRequired() != null ? request.getIsRequired() : true);
        field.setDisplayOrder(request.getDisplayOrder());
    }
}
