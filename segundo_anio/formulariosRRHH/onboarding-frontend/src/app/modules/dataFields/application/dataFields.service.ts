import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { DataField, CreateDataFieldRequest } from '../domain/dataFields';
import { DATA_FIELDS_REPOSITORY } from '../infrastructure/config/dataFields.token';

@Injectable({ providedIn: 'root' })
export class DataFieldsService {
  private repository = inject(DATA_FIELDS_REPOSITORY);

  findAll(): Observable<DataField[]> {
    return this.repository.findAll();
  }

  findAllForCandidate(): Observable<DataField[]> {
    return this.repository.findAllForCandidate();
  }

  canModify(): Observable<boolean> {
    return this.repository.canModify();
  }

  create(request: CreateDataFieldRequest): Observable<DataField> {
    return this.repository.create(request);
  }

  update(id: number, request: CreateDataFieldRequest): Observable<DataField> {
    return this.repository.update(id, request);
  }

  delete(id: number): Observable<void> {
    return this.repository.delete(id);
  }
}
