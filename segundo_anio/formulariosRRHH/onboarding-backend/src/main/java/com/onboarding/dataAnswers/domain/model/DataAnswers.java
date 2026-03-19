package com.onboarding.dataAnswers.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataAnswers {

    private Long id;
    private String onboardingProcessId;
    private Long dataFieldId;
    private String answerValue;
    private Long fieldOptionId;
    private LocalDateTime createdAt;
}
