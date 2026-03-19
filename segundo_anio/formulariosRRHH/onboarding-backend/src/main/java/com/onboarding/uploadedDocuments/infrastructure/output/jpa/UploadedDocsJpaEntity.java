package com.onboarding.uploadedDocuments.infrastructure.output.jpa;

import com.onboarding.documentRequirements.infrastructure.output.jpa.DocumentsReqJpaEntity;
import com.onboarding.onboardingProcesses.infrastructure.output.jpa.OnboardingProcJpaEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "uploaded_documents")
@Getter
@Setter
public class UploadedDocsJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onboarding_process_id", nullable = false)
    private OnboardingProcJpaEntity onboardingProcess;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_requirement_id", nullable = false)
    private DocumentsReqJpaEntity documentRequirement;

    @Column(name = "file_path", nullable = false, length = 512)
    private String filePath;

    @Column(name = "original_filename", nullable = false, length = 255)
    private String originalFilename;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(name = "file_size_bytes", nullable = false)
    private Long fileSizeBytes;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_status", nullable = false)
    private ValidationStatus validationStatus = ValidationStatus.PENDING_VALIDATION;

    @Column(name = "validation_notes", columnDefinition = "TEXT")
    private String validationNotes;

    @CreationTimestamp
    @Column(name = "uploaded_at", updatable = false)
    private LocalDateTime uploadedAt;

    public enum ValidationStatus {
        PENDING_VALIDATION, APPROVED, REJECTED
    }
}
