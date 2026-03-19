package com.onboarding.accessTokens.infrastructure.output.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import com.onboarding.onboardingProcesses.infrastructure.output.jpa.OnboardingProcJpaEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_tokens")
@Getter
@Setter
public class AcessTokensJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_process_id", nullable = false)
    private OnboardingProcJpaEntity onboardingProcess;

    @Column(name = "token_hash", nullable = false, unique = true, length = 255)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;

    @Column(nullable = false)
    private Integer attempts = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
