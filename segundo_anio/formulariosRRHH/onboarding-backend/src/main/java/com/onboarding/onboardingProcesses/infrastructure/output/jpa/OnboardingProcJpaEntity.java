package com.onboarding.onboardingProcesses.infrastructure.output.jpa;

import com.onboarding.adminUsers.infrastructure.output.jpa.AdminUsersJpaEntity;
import com.onboarding.candidates.infrastructure.output.jpa.CandidatesJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "onboarding_processes")
@Getter
@Setter
public class OnboardingProcJpaEntity {

    @Id
    @UuidGenerator
    @Column(length = 36, updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidatesJpaEntity candidate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OnboardingStatus status = OnboardingStatus.PENDING_ACCESS;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validated_by")
    private AdminUsersJpaEntity validatedBy;

    public enum OnboardingStatus {
        PENDING_ACCESS, IN_PROGRESS, SUBMITTED, VALIDATED, REJECTED
    }
}
