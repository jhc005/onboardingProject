package com.onboarding.candidates.application.commands;

import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCandidateCommand implements Command {
    private final String email;
    private final String firstName;
    private final String lastName;
}
