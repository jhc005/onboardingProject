import { Observable } from 'rxjs';
import { OnboardingProcess, RejectRequest } from './onboardingProcesses';

export interface IOnboardingProcessesRepository {
  findAll(): Observable<OnboardingProcess[]>;
  findById(id: string): Observable<OnboardingProcess>;
  validate(id: string): Observable<OnboardingProcess>;
  reject(id: string, request: RejectRequest): Observable<OnboardingProcess>;
  submit(processId: string): Observable<OnboardingProcess>;
  getStatus(processId: string): Observable<{ status: string }>;
}
