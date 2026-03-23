import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { DataAnswer, SaveAnswersRequest } from '../domain/dataAnswers';
import { DATA_ANSWERS_REPOSITORY } from '../infrastructure/config/dataAnswers.token';

@Injectable({ providedIn: 'root' })
export class DataAnswersService {
  private repository = inject(DATA_ANSWERS_REPOSITORY);

  save(processId: string, request: SaveAnswersRequest): Observable<DataAnswer[]> {
    return this.repository.save(processId, request);
  }

  findByProcess(processId: string): Observable<DataAnswer[]> {
    return this.repository.findByProcess(processId);
  }

  findByProcessAdmin(processId: string): Observable<DataAnswer[]> {
    return this.repository.findByProcessAdmin(processId);
  }
}
