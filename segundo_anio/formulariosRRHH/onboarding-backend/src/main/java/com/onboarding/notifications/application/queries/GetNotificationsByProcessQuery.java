package com.onboarding.notifications.application.queries;

import com.onboarding.notifications.application.dto.NotificationsDto;
import com.onboarding.shared.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetNotificationsByProcessQuery implements Query<List<NotificationsDto.Response>> {
    private final String processId;
}
