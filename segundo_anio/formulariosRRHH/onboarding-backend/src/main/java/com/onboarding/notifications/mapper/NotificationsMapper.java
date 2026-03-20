package com.onboarding.notifications.mapper;

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
}