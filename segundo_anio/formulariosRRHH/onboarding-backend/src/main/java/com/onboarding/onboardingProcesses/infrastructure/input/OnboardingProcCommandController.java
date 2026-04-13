package com.onboarding.onboardingProcesses.infrastructure.input;

import com.onboarding.onboardingProcesses.application.commands.*;
import com.onboarding.onboardingProcesses.application.dto.OnboardingProcDto;
import com.onboarding.shared.cqrs.CommandBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OnboardingProcCommandController {

    private final CommandBus commandBus;

    @PatchMapping("/api/admin/processes/{id}/validate")
    public ResponseEntity<OnboardingProcDto.Response> validate(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(commandBus.dispatch(new ValidateProcessCommand(id, authentication.getName())));
    }

    @PatchMapping("/api/admin/processes/{id}/reject")
    public ResponseEntity<OnboardingProcDto.Response> reject(
            @PathVariable String id,
            Authentication authentication,
            @RequestBody OnboardingProcDto.RejectRequest request) {
        return ResponseEntity.ok(commandBus.dispatch(new RejectProcessCommand(id, authentication.getName(), request.getNotes())));
    }

    @PostMapping("/api/admin/processes/{id}/resend-access-link")
    public ResponseEntity<Void> resendAccessLink(@PathVariable String id) {
        commandBus.dispatch(new ResendAccessLinkCommand(id));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/candidate/form/{processId}/submit")
    public ResponseEntity<OnboardingProcDto.Response> submit(@PathVariable String processId) {
        return ResponseEntity.ok(commandBus.dispatch(new SubmitProcessCommand(processId)));
    }
}
