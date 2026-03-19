package com.onboarding.fieldOptions.application;

import com.onboarding.fieldOptions.application.dto.FieldOptionsDto;
import com.onboarding.fieldOptions.domain.model.FieldOptions;
import com.onboarding.fieldOptions.domain.outport.FieldOptionsRepositoryPort;
import com.onboarding.fieldOptions.mapper.FieldOptionsMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldOptionsService {

    private final FieldOptionsRepositoryPort fieldOptionsPort;
    private final FieldOptionsMapper mapper;

    public List<FieldOptionsDto.Response> findByDataFieldId(Long dataFieldId) {
        return fieldOptionsPort.findByDataFieldId(dataFieldId).stream().map(mapper::toResponse).toList();
    }

    public FieldOptionsDto.Response create(FieldOptionsDto.Request request) {
        FieldOptions option = new FieldOptions();
        option.setDataFieldId(request.getDataFieldId());
        option.setOptionLabel(request.getOptionLabel());
        option.setOptionValue(request.getOptionValue());
        option.setDisplayOrder(request.getDisplayOrder());
        return mapper.toResponse(fieldOptionsPort.save(option));
    }

    public void delete(Long id) {
        fieldOptionsPort.deleteById(id);
    }
}
