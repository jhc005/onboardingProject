package com.onboarding.onboardingProcesses.application;

import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.candidates.domain.outport.CandidatesRepositoryPort;
import com.onboarding.notifications.application.NotificationsService;
import com.onboarding.onboardingProcesses.application.dto.OnboardingProcDto;
import com.onboarding.onboardingProcesses.domain.model.OnboardingProc;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.onboardingProcesses.mapper.OnboardingProcMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OnboardingProcService {

    private final OnboardingProcRepositoryPort processPort;
    private final CandidatesRepositoryPort candidatesPort;
    private final NotificationsService notificationsService;
    private final OnboardingProcMapper mapper;

    public List<OnboardingProcDto.Response> findAll() {
        return processPort.findAll().stream().map(mapper::toResponse).toList();
    }

    public OnboardingProcDto.Response findById(String id) {
        OnboardingProc process = processPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));
        return mapper.toResponse(process);
    }

    @Transactional
    public OnboardingProcDto.Response submit(String processId) {
        OnboardingProc process = processPort.findById(processId)
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        if (process.getStatus() != OnboardingProc.OnboardingStatus.IN_PROGRESS) {
            throw new IllegalStateException("El proceso no está en estado IN_PROGRESS.");
        }

        process.setStatus(OnboardingProc.OnboardingStatus.SUBMITTED);
        process.setSubmittedAt(LocalDateTime.now());
        processPort.save(process);

        Candidates candidate = candidatesPort.findById(process.getCandidateId()).orElseThrow();
        String fullName = candidate.getFirstName() + " " + candidate.getLastName();
        notificationsService.sendFormSubmitted(processId, fullName);

        return mapper.toResponse(process);
    }

    @Transactional
    public OnboardingProcDto.Response validate(String processId, Long adminId) {
        OnboardingProc process = processPort.findById(processId)
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        if (process.getStatus() != OnboardingProc.OnboardingStatus.SUBMITTED) {
            throw new IllegalStateException("El proceso no está en estado SUBMITTED.");
        }

        process.setStatus(OnboardingProc.OnboardingStatus.VALIDATED);
        process.setValidatedAt(LocalDateTime.now());
        process.setValidatedById(adminId);
        processPort.save(process);

        Candidates candidate = candidatesPort.findById(process.getCandidateId()).orElseThrow();
        String fullName = candidate.getFirstName() + " " + candidate.getLastName();
        notificationsService.sendValidated(processId, candidate.getEmail(), fullName);

        return mapper.toResponse(process);
    }

    @Transactional
    public OnboardingProcDto.Response reject(String processId, Long adminId, String notes) {
        OnboardingProc process = processPort.findById(processId)
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        process.setStatus(OnboardingProc.OnboardingStatus.REJECTED);
        process.setValidatedAt(LocalDateTime.now());
        process.setValidatedById(adminId);
        processPort.save(process);

        Candidates candidate = candidatesPort.findById(process.getCandidateId()).orElseThrow();
        String fullName = candidate.getFirstName() + " " + candidate.getLastName();
        notificationsService.sendRejected(processId, candidate.getEmail(), fullName, notes);

        return mapper.toResponse(process);
    }
}
