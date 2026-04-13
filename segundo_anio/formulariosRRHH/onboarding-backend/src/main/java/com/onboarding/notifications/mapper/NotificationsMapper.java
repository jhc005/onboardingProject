package com.onboarding.notifications.mapper;

import com.onboarding.notifications.application.dto.NotificationsDto;
import com.onboarding.notifications.domain.model.Notifications;
import com.onboarding.notifications.infrastructure.output.jpa.NotificationsJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationsMapper {

        public Notifications toDomain(NotificationsJpaEntity entity) {
                if (entity == null)
                        return null;

                Notifications domain = new Notifications();
                domain.setId(entity.getId());
                domain.setOnboardingProcessId(entity.getOnboardingProcess() != null
                                ? entity.getOnboardingProcess().getId()
                                : null);
                domain.setType(entity.getType()); 
                domain.setRecipientEmail(entity.getRecipientEmail());
                domain.setSentAt(entity.getSentAt());
                domain.setStatus(entity.getStatus()); 
                domain.setErrorMessage(entity.getErrorMessage());

                return domain;
        }

        public NotificationsJpaEntity toEntity(Notifications domain) {
                if (domain == null)
                        return null;

                NotificationsJpaEntity entity = new NotificationsJpaEntity();
                entity.setId(domain.getId());
                entity.setType(domain.getType());
                entity.setRecipientEmail(domain.getRecipientEmail());
                entity.setStatus(domain.getStatus());
                entity.setErrorMessage(domain.getErrorMessage());

                return entity;
        }

        public NotificationsDto.Response toResponse(Notifications n) {
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