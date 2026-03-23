package com.onboarding.documentRequirements.infrastructure.output.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "document_requirements")
@Getter
@Setter
public class DocumentsReqJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "allowed_formats", nullable = false, length = 255)
    private String allowedFormats;

    @Column(name = "file_name_regex", length = 255)
    private String fileNameRegex;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired = true;

    @Column(name = "is_conditional", nullable = false)
    private Boolean isConditional = false;

    @Column(name = "condition_description", length = 255)
    private String conditionDescription;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "template_file_path", length = 512)
    private String templateFilePath;

    @Column(name = "template_original_filename", length = 255)
    private String templateOriginalFilename;
}
