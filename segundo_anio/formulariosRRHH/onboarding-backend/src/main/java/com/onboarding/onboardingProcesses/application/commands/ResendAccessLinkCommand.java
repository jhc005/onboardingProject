package com.onboarding.onboardingProcesses.application.commands;

import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResendAccessLinkCommand implements Command {
    private final String processId;
}
