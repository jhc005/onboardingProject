import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { Candidate, CreateCandidateRequest } from '../../../domain/candidates';

@Injectable({ providedIn: 'root' })
export class CandidatesApi {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/api/admin/candidates`;

  create(request: CreateCandidateRequest): Observable<Candidate> {
    return this.http.post<Candidate>(this.url, request);
  }

  findAll(): Observable<Candidate[]> {
    return this.http.get<Candidate[]>(this.url);
  }

  findById(id: string): Observable<Candidate> {
    return this.http.get<Candidate>(`${this.url}/${id}`);
  }
}
