package com.onboarding.dataFields.application.queries;

import com.onboarding.dataFields.application.dto.DataFieldsDto;
import com.onboarding.shared.cqrs.Query;

import java.util.List;

public class GetAllDataFieldsQuery implements Query<List<DataFieldsDto.Response>> {
}
