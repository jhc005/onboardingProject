import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { DataField, CreateDataFieldRequest } from '../../../domain/dataFields';

@Injectable({ providedIn: 'root' })
export class DataFieldsApi {
  private readonly http = inject(HttpClient);
  private readonly adminUrl = `${environment.apiUrl}/api/admin/fields`;
  private readonly candidateUrl = `${environment.apiUrl}/api/candidate/form/fields`;

  findAll(): Observable<DataField[]> {
    return this.http.get<DataField[]>(this.adminUrl);
  }

  findAllForCandidate(): Observable<DataField[]> {
    return this.http.get<DataField[]>(this.candidateUrl);
  }

  canModify(): Observable<boolean> {
    return this.http.get<boolean>(`${this.adminUrl}/can-modify`);
  }

  create(request: CreateDataFieldRequest): Observable<DataField> {
    return this.http.post<DataField>(this.adminUrl, request);
  }

  update(id: number, request: CreateDataFieldRequest): Observable<DataField> {
    return this.http.put<DataField>(`${this.adminUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.adminUrl}/${id}`);
  }
}
