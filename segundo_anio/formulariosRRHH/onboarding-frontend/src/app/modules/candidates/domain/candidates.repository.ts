import { Observable } from 'rxjs';
import { Candidate, CreateCandidateRequest } from './candidates';

export interface ICandidatesRepository {
  create(request: CreateCandidateRequest): Observable<Candidate>;
  findAll(): Observable<Candidate[]>;
  findById(id: string): Observable<Candidate>;
}
