import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { DataAnswer, SaveAnswersRequest } from '../../../domain/dataAnswers';

@Injectable({ providedIn: 'root' })
export class DataAnswersApi {
  private readonly http = inject(HttpClient);
  private readonly candidateUrl = `${environment.apiUrl}/api/candidate/form`;
  private readonly adminUrl = `${environment.apiUrl}/api/admin/processes`;

  save(processId: string, request: SaveAnswersRequest): Observable<DataAnswer[]> {
    return this.http.post<DataAnswer[]>(`${this.candidateUrl}/${processId}/answers`, request);
  }

  findByProcess(processId: string): Observable<DataAnswer[]> {
    return this.http.get<DataAnswer[]>(`${this.candidateUrl}/${processId}/answers`);
  }

  findByProcessAdmin(processId: string): Observable<DataAnswer[]> {
    return this.http.get<DataAnswer[]>(`${this.adminUrl}/${processId}/answers`);
  }
}
