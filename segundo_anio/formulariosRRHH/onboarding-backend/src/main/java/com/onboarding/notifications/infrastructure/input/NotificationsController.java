package com.onboarding.notifications.infrastructure.input;

import com.onboarding.notifications.application.dto.NotificationsDto;
import com.onboarding.notifications.domain.model.Notifications;
import com.onboarding.notifications.domain.outport.NotificationsRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/processes/{processId}/notifications")
@RequiredArgsConstructor
public class NotificationsController {

    private final NotificationsRepositoryPort notificationsPort;

    @GetMapping
    public ResponseEntity<List<NotificationsDto.Response>> findByProcess(@PathVariable String processId) {
        List<NotificationsDto.Response> list = notificationsPort
                .findByOnboardingProcessId(processId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    private NotificationsDto.Response toResponse(Notifications n) {
        return NotificationsDto.Response.builder()
                .id(n.getId())
                .onboardingProcessId(n.getOnboardingProcessId())
                .type(n.getType() != null ? n.getType().name() : null)
                .recipientEmail(n.getRecipientEmail())
                .sentAt(n.getSentAt())
                .status(n.getStatus() != null ? n.getStatus().name() : null)
                .errorMessage(n.getErrorMessage())
                .build();
    }
}
