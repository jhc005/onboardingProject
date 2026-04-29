package com.onboarding.notifications.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationsJpaRepository extends JpaRepository<NotificationsJpaEntity, Long> {

    List<NotificationsJpaEntity> findByOnboardingProcess_Id(String processId);
}
