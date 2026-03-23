import { InjectionToken } from '@angular/core';
import { IOnboardingProcessesRepository } from '../../domain/onboardingProcesses.repository';

export const ONBOARDING_PROCESSES_REPOSITORY =
  new InjectionToken<IOnboardingProcessesRepository>('IOnboardingProcessesRepository');
