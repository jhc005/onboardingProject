package com.onboarding.notifications.infrastructure.output.adapter;

import com.onboarding.notifications.domain.model.Notifications;
import com.onboarding.notifications.domain.outport.NotificationsRepositoryPort;
import com.onboarding.notifications.infrastructure.output.jpa.NotificationsJpaEntity;
import com.onboarding.notifications.infrastructure.output.jpa.NotificationsJpaRepository;
import com.onboarding.notifications.mapper.NotificationsMapper;
import com.onboarding.onboardingProcesses.infrastructure.output.jpa.OnboardingProcJpaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationsAdapter implements NotificationsRepositoryPort {

    private final NotificationsJpaRepository repository;
    private final OnboardingProcJpaRepository processRepository;
    private final NotificationsMapper mapper;

    @Override
    public Notifications save(Notifications notification) {
        NotificationsJpaEntity entity = mapper.toEntity(notification);

        if (notification.getOnboardingProcessId() != null) {
            processRepository
                    .findById(notification.getOnboardingProcessId())
                    .ifPresent(entity::setOnboardingProcess);
        }

        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<Notifications> findByOnboardingProcessId(String processId) {
        return repository.findByOnboardingProcess_Id(processId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

}
