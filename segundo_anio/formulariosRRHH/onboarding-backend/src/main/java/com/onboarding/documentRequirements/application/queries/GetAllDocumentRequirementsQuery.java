package com.onboarding.documentRequirements.application.queries;

import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import com.onboarding.shared.cqrs.Query;

import java.util.List;

public class GetAllDocumentRequirementsQuery implements Query<List<DocumentsReqDto.Response>> {
}
