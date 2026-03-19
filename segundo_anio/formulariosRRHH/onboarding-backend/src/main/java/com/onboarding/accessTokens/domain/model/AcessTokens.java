package com.onboarding.accessTokens.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcessTokens {

    private Long id;
    private String onboardingProcessId;
    private String tokenHash;
    private LocalDateTime expiresAt;
    private LocalDateTime usedAt;
    private Boolean isUsed;
    private Integer attempts;
    private LocalDateTime createdAt;
}
