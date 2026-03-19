package com.onboarding.notifications.infrastructure.output.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import com.onboarding.onboardingProcesses.infrastructure.output.jpa.OnboardingProcJpaEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class NotificationsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_process_id", nullable = false)
    private OnboardingProcJpaEntity onboardingProcess;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(name = "recipient_email", nullable = false, length = 255)
    private String recipientEmail;

    @CreationTimestamp
    @Column(name = "sent_at", updatable = false)
    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status = NotificationStatus.SENT;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    public enum NotificationType {
        ACCESS_LINK, FORM_SUBMITTED, VALIDATED, REJECTED, DOCUMENT_REJECTED
    }

    public enum NotificationStatus {
        SENT, FAILED
    }
}
