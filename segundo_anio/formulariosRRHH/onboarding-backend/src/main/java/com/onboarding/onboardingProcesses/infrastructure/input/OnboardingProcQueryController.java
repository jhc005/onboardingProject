package com.onboarding.onboardingProcesses.infrastructure.input;

import com.onboarding.onboardingProcesses.application.dto.OnboardingProcDto;
import com.onboarding.onboardingProcesses.application.queries.GetAllProcessesQuery;
import com.onboarding.onboardingProcesses.application.queries.GetProcessByIdQuery;
import com.onboarding.shared.cqrs.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OnboardingProcQueryController {

    private final QueryBus queryBus;

    @GetMapping("/api/admin/processes")
    public ResponseEntity<List<OnboardingProcDto.Response>> findAll() {
        return ResponseEntity.ok(queryBus.ask(new GetAllProcessesQuery()));
    }

    @GetMapping("/api/admin/processes/{id}")
    public ResponseEntity<OnboardingProcDto.Response> findById(@PathVariable String id) {
        return ResponseEntity.ok(queryBus.ask(new GetProcessByIdQuery(id)));
    }
}
