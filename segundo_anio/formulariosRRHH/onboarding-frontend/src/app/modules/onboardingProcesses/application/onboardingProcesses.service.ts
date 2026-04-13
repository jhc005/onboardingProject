import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { OnboardingProcess } from '../domain/onboardingProcesses';
import { ONBOARDING_PROCESSES_REPOSITORY } from '../infrastructure/config/onboardingProcesses.token';

@Injectable({ providedIn: 'root' })
export class OnboardingProcessesService {
  private repository = inject(ONBOARDING_PROCESSES_REPOSITORY);

  findAll(): Observable<OnboardingProcess[]> {
    return this.repository.findAll();
  }

  findById(id: string): Observable<OnboardingProcess> {
    return this.repository.findById(id);
  }

  validate(id: string): Observable<OnboardingProcess> {
    return this.repository.validate(id);
  }

  reject(id: string, notes: string): Observable<OnboardingProcess> {
    return this.repository.reject(id, { notes });
  }

  submit(processId: string): Observable<OnboardingProcess> {
    return this.repository.submit(processId);
  }

  resendAccessLink(id: string): Observable<void> {
    return this.repository.resendAccessLink(id);
  }
}
