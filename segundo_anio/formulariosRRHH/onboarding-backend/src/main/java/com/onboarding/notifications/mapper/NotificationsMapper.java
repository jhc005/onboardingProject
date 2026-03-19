package com.onboarding.notifications.mapper;

import com.onboarding.notifications.domain.model.Notifications;
import com.onboarding.notifications.infrastructure.output.jpa.NotificationsJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationsMapper {

    public Notifications toDomain(NotificationsJpaEntity entity) {
        Notifications domain = new Notifications();
        domain.setId(entity.getId());
        domain.setOnboardingProcessId(entity.getOnboardingProcess() != null
                ? entity.getOnboardingProcess().getId() : null);
        domain.setType(entity.getType() != null
                ? Notifications.NotificationType.valueOf(entity.getType().name()) : null);
        domain.setRecipientEmail(entity.getRecipientEmail());
        domain.setSentAt(entity.getSentAt());
        domain.setStatus(entity.getStatus() != null
                ? Notifications.NotificationStatus.valueOf(entity.getStatus().name()) : null);
        domain.setErrorMessage(entity.getErrorMessage());
        return domain;
    }

    public NotificationsJpaEntity toEntity(Notifications domain) {
        NotificationsJpaEntity entity = new NotificationsJpaEntity();
        entity.setId(domain.getId());
        entity.setType(domain.getType() != null
                ? NotificationsJpaEntity.NotificationType.valueOf(domain.getType().name()) : null);
        entity.setRecipientEmail(domain.getRecipientEmail());
        entity.setStatus(domain.getStatus() != null
                ? NotificationsJpaEntity.NotificationStatus.valueOf(domain.getStatus().name()) : null);
        entity.setErrorMessage(domain.getErrorMessage());
        return entity;
    }
}
