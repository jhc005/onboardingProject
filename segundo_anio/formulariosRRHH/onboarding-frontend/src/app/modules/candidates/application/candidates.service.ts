import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Candidate, CreateCandidateRequest } from '../domain/candidates';
import { CANDIDATES_REPOSITORY } from '../infrastructure/config/candidates.token';

@Injectable({ providedIn: 'root' })
export class CandidatesService {
  private repository = inject(CANDIDATES_REPOSITORY);

  create(request: CreateCandidateRequest): Observable<Candidate> {
    return this.repository.create(request);
  }

  findAll(): Observable<Candidate[]> {
    return this.repository.findAll();
  }

  findById(id: string): Observable<Candidate> {
    return this.repository.findById(id);
  }
}
