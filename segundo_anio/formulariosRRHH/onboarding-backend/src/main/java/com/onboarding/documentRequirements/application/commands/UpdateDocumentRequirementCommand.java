package com.onboarding.documentRequirements.application.commands;

import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateDocumentRequirementCommand implements Command {
    private final Long id;
    private final DocumentsReqDto.Request request;
}
