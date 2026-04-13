package com.onboarding.documentRequirements.application.commands;

import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class UploadTemplateCommand implements Command {
    private final Long id;
    private final MultipartFile file;
}
