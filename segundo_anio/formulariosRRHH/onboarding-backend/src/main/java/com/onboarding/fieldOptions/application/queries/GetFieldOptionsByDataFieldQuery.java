package com.onboarding.fieldOptions.application.queries;

import com.onboarding.fieldOptions.application.dto.FieldOptionsDto;
import com.onboarding.shared.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetFieldOptionsByDataFieldQuery implements Query<List<FieldOptionsDto.Response>> {
    private final Long dataFieldId;
}
