import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { FieldOption, CreateFieldOptionRequest } from '../domain/fieldOptions';
import { FIELD_OPTIONS_REPOSITORY } from '../infrastructure/config/fieldOptions.token';

@Injectable({ providedIn: 'root' })
export class FieldOptionsService {
  private readonly repository = inject(FIELD_OPTIONS_REPOSITORY);

  create(fieldId: number, request: CreateFieldOptionRequest): Observable<FieldOption> {
    return this.repository.create(fieldId, request);
  }

  delete(fieldId: number, optionId: number): Observable<void> {
    return this.repository.delete(fieldId, optionId);
  }
}
