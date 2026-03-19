package com.onboarding.notifications.domain.outport;

import com.onboarding.notifications.domain.model.Notifications;
import com.onboarding.notifications.domain.model.Notifications.NotificationType;

import java.util.List;

public interface NotificationsRepositoryPort {

    Notifications save(Notifications notification);
    List<Notifications> findByOnboardingProcessId(String onboardingProcessId);
    List<Notifications> findByOnboardingProcessIdAndType(String onboardingProcessId, NotificationType type);
}
