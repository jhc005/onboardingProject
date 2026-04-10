package com.onboarding.candidates.infrastructure.input;

import com.onboarding.candidates.application.CandidatesService;
import com.onboarding.candidates.application.dto.CandidatesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/candidates")
@RequiredArgsConstructor
public class CandidatesController {

    private final CandidatesService candidatesService;
    @GetMapping
    public ResponseEntity<List<CandidatesDto.Response>> findAll() {
        return ResponseEntity.ok(candidatesService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatesDto.Response> findById(@PathVariable String id) {
        return ResponseEntity.ok(candidatesService.findById(id));
    }
    @PostMapping
    public ResponseEntity<CandidatesDto.Response> create(@RequestBody CandidatesDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidatesService.createCandidate(request));
    }

    
}
