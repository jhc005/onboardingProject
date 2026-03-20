package com.onboarding.notifications.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onboarding.notifications.domain.valueobject.NotificationType;

import java.util.List;

public interface NotificationsJpaRepository extends JpaRepository<NotificationsJpaEntity, Long> {

    List<NotificationsJpaEntity> findByOnboardingProcess_Id(String processId);
    List<NotificationsJpaEntity> findByOnboardingProcess_IdAndType(String processId, NotificationType type);
}
