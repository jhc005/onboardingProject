package com.onboarding.notifications.application.queries;

import com.onboarding.notifications.application.dto.NotificationsDto;
import com.onboarding.notifications.domain.outport.NotificationsRepositoryPort;
import com.onboarding.notifications.mapper.NotificationsMapper;
import com.onboarding.shared.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetNotificationsByProcessQueryHandler implements QueryHandler<GetNotificationsByProcessQuery, List<NotificationsDto.Response>> {

    private final NotificationsRepositoryPort notificationsPort;
    private final NotificationsMapper mapper;

    @Override
    public List<NotificationsDto.Response> handle(GetNotificationsByProcessQuery query) {
        return notificationsPort.findByOnboardingProcessId(query.getProcessId()).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
