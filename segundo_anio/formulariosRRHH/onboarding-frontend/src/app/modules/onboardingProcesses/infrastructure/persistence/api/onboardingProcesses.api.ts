import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { OnboardingProcess, RejectRequest } from '../../../domain/onboardingProcesses';

@Injectable({ providedIn: 'root' })
export class OnboardingProcessesApi {
  private readonly http = inject(HttpClient);
  private readonly adminUrl = `${environment.apiUrl}/api/admin/processes`;
  private readonly candidateUrl = `${environment.apiUrl}/api/candidate/form`;

  findAll(): Observable<OnboardingProcess[]> {
    return this.http.get<OnboardingProcess[]>(this.adminUrl);
  }

  findById(id: string): Observable<OnboardingProcess> {
    return this.http.get<OnboardingProcess>(`${this.adminUrl}/${id}`);
  }

  validate(id: string): Observable<OnboardingProcess> {
    return this.http.patch<OnboardingProcess>(`${this.adminUrl}/${id}/validate`, {});
  }

  reject(id: string, request: RejectRequest): Observable<OnboardingProcess> {
    return this.http.patch<OnboardingProcess>(`${this.adminUrl}/${id}/reject`, request);
  }

  submit(processId: string): Observable<OnboardingProcess> {
    return this.http.post<OnboardingProcess>(`${this.candidateUrl}/${processId}/submit`, {});
  }

  resendAccessLink(id: string): Observable<void> {
    return this.http.post<void>(`${this.adminUrl}/${id}/resend-access-link`, {});
  }
}
