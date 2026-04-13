package com.onboarding.documentRequirements.application.queries;

import com.onboarding.shared.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DownloadTemplateQuery implements Query<DownloadTemplateQuery.Result> {

    private final Long id;

    public record Result(org.springframework.core.io.Resource resource, String filename) {}
}
