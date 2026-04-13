package com.onboarding.candidates.application.queries;

import com.onboarding.candidates.application.dto.CandidatesDto;
import com.onboarding.shared.cqrs.Query;

import java.util.List;

public class GetAllCandidatesQuery implements Query<List<CandidatesDto.Response>> {
}
