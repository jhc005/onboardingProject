package com.onboarding.uploadedDocuments.application.commands;

import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UploadDocumentCommand implements Command {
    private final String processId;
    private final Long requirementId;
    private final MultipartFile file;
}
