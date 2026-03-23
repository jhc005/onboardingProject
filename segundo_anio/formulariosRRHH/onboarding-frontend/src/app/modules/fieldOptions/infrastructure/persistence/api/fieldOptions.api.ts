import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { FieldOption, CreateFieldOptionRequest } from '../../../domain/fieldOptions';

@Injectable({ providedIn: 'root' })
export class FieldOptionsApi {
  private readonly http = inject(HttpClient);

  private url(fieldId: number): string {
    return `${environment.apiUrl}/api/admin/fields/${fieldId}/options`;
  }

  create(fieldId: number, request: CreateFieldOptionRequest): Observable<FieldOption> {
    return this.http.post<FieldOption>(this.url(fieldId), request);
  }

  delete(fieldId: number, optionId: number): Observable<void> {
    return this.http.delete<void>(`${this.url(fieldId)}/${optionId}`);
  }
}
