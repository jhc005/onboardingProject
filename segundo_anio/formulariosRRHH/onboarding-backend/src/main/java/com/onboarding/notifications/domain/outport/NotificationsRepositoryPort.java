package com.onboarding.notifications.domain.outport;

import com.onboarding.notifications.domain.model.Notifications;

import java.util.List;

public interface NotificationsRepositoryPort {

    Notifications save(Notifications notification);

    List<Notifications> findByOnboardingProcessId(String onboardingProcessId);

}