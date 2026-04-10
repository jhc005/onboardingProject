import { Observable } from 'rxjs';
import { DataField, CreateDataFieldRequest } from './dataFields';

export interface IDataFieldsRepository {
  findAll(): Observable<DataField[]>;
  findAllForCandidate(): Observable<DataField[]>;
  canModify(): Observable<boolean>;
  create(request: CreateDataFieldRequest): Observable<DataField>;
  update(id: number, request: CreateDataFieldRequest): Observable<DataField>;
  delete(id: number): Observable<void>;
}
