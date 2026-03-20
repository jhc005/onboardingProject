package com.onboarding.notifications.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.onboarding.notifications.domain.valueobject.NotificationStatus;
import com.onboarding.notifications.domain.valueobject.NotificationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {

    private Long id;
    private String onboardingProcessId;
    private NotificationType type;
    private String recipientEmail;
    private LocalDateTime sentAt;
    private NotificationStatus status;
    private String errorMessage;
}
