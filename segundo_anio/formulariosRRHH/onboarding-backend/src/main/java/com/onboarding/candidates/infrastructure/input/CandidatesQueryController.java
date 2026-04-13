package com.onboarding.candidates.infrastructure.input;

import com.onboarding.candidates.application.dto.CandidatesDto;
import com.onboarding.candidates.application.queries.GetAllCandidatesQuery;
import com.onboarding.shared.cqrs.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/candidates")
@RequiredArgsConstructor
public class CandidatesQueryController {

    private final QueryBus queryBus;

    @GetMapping
    public ResponseEntity<List<CandidatesDto.Response>> findAll() {
        return ResponseEntity.ok(queryBus.ask(new GetAllCandidatesQuery()));
    }
}
