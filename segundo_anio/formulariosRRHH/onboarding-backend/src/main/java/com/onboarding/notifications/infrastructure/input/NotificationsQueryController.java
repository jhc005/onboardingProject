package com.onboarding.notifications.infrastructure.input;

import com.onboarding.notifications.application.dto.NotificationsDto;
import com.onboarding.notifications.application.queries.GetNotificationsByProcessQuery;
import com.onboarding.shared.cqrs.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/processes/{processId}/notifications")
@RequiredArgsConstructor
public class NotificationsQueryController {

    private final QueryBus queryBus;

    @GetMapping
    public ResponseEntity<List<NotificationsDto.Response>> findByProcess(@PathVariable String processId) {
        return ResponseEntity.ok(queryBus.ask(new GetNotificationsByProcessQuery(processId)));
    }
}
