package com.onboarding.dataAnswers.infrastructure.output.jpa;

import com.onboarding.dataFields.infrastructure.output.jpa.DataFieldsJpaEntity;
import com.onboarding.onboardingProcesses.infrastructure.output.jpa.OnboardingProcJpaEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "data_answers")
@Getter
@Setter
public class DataAnswersJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_process_id", nullable = false)
    private OnboardingProcJpaEntity onboardingProcess;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_field_id", nullable = false)
    private DataFieldsJpaEntity dataField;

    @Column(name = "answer_value", columnDefinition = "TEXT")
    private String answerValue;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
