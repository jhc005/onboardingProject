import { Observable } from 'rxjs';
import { FieldOption, CreateFieldOptionRequest } from './fieldOptions';

export interface IFieldOptionsRepository {
  create(fieldId: number, request: CreateFieldOptionRequest): Observable<FieldOption>;
  delete(fieldId: number, optionId: number): Observable<void>;
}
