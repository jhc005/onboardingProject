package com.onboarding.candidates.infrastructure.input;

import com.onboarding.candidates.application.commands.CreateCandidateCommand;
import com.onboarding.candidates.application.dto.CandidatesDto;
import com.onboarding.shared.cqrs.CommandBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/candidates")
@RequiredArgsConstructor
public class CandidatesCommandController {

    private final CommandBus commandBus;

    @PostMapping
    public ResponseEntity<CandidatesDto.Response> create(@RequestBody CandidatesDto.CreateRequest request) {
        CandidatesDto.Response response = commandBus.dispatch(
                new CreateCandidateCommand(request.getEmail(), request.getFirstName(), request.getLastName())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
