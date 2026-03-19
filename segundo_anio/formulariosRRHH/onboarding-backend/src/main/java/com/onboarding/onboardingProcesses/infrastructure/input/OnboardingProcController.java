package com.onboarding.onboardingProcesses.infrastructure.input;

import com.onboarding.onboardingProcesses.application.OnboardingProcService;
import com.onboarding.onboardingProcesses.application.dto.OnboardingProcDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OnboardingProcController {

    private final OnboardingProcService processService;

    @GetMapping("/api/admin/processes")
    public ResponseEntity<List<OnboardingProcDto.Response>> findAll() {
        return ResponseEntity.ok(processService.findAll());
    }

    @GetMapping("/api/admin/processes/{id}")
    public ResponseEntity<OnboardingProcDto.Response> findById(@PathVariable String id) {
        return ResponseEntity.ok(processService.findById(id));
    }

    @PatchMapping("/api/admin/processes/{id}/validate")
    public ResponseEntity<OnboardingProcDto.Response> validate(
            @PathVariable String id,
            @RequestParam Long adminId) {
        return ResponseEntity.ok(processService.validate(id, adminId));
    }

    @PatchMapping("/api/admin/processes/{id}/reject")
    public ResponseEntity<OnboardingProcDto.Response> reject(
            @PathVariable String id,
            @RequestParam Long adminId,
            @RequestBody OnboardingProcDto.RejectRequest request) {
        return ResponseEntity.ok(processService.reject(id, adminId, request.getNotes()));
    }

    @PostMapping("/api/candidate/form/{processId}/submit")
    public ResponseEntity<OnboardingProcDto.Response> submit(@PathVariable String processId) {
        return ResponseEntity.ok(processService.submit(processId));
    }

    @GetMapping("/api/candidate/form/{processId}/status")
    public ResponseEntity<OnboardingProcDto.Response> getStatus(@PathVariable String processId) {
        return ResponseEntity.ok(processService.findById(processId));
    }
}
