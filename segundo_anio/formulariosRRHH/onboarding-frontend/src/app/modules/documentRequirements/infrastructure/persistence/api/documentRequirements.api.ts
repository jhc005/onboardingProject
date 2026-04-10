import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { DocumentRequirement, CreateDocumentRequirementRequest } from '../../../domain/documentRequirements';

@Injectable({ providedIn: 'root' })
export class DocumentRequirementsApi {
  private readonly http = inject(HttpClient);
  private readonly adminUrl = `${environment.apiUrl}/api/admin/document-requirements`;
  private readonly candidateUrl = `${environment.apiUrl}/api/candidate/form/document-requirements`;

  findAll(): Observable<DocumentRequirement[]> {
    return this.http.get<DocumentRequirement[]>(this.adminUrl);
  }

  findAllForCandidate(): Observable<DocumentRequirement[]> {
    return this.http.get<DocumentRequirement[]>(this.candidateUrl);
  }

  canModify(): Observable<boolean> {
    return this.http.get<boolean>(`${this.adminUrl}/can-modify`);
  }

  create(request: CreateDocumentRequirementRequest): Observable<DocumentRequirement> {
    return this.http.post<DocumentRequirement>(this.adminUrl, request);
  }

  update(id: number, request: CreateDocumentRequirementRequest): Observable<DocumentRequirement> {
    return this.http.put<DocumentRequirement>(`${this.adminUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.adminUrl}/${id}`);
  }

  uploadTemplate(id: number, file: File): Observable<DocumentRequirement> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<DocumentRequirement>(`${this.adminUrl}/${id}/template`, formData);
  }

  downloadTemplate(id: number): Observable<Blob> {
    return this.http.get(`${this.adminUrl}/${id}/template/download`, { responseType: 'blob' });
  }

  downloadTemplateForCandidate(id: number): Observable<Blob> {
    return this.http.get(`${this.candidateUrl}/${id}/template/download`, { responseType: 'blob' });
  }
}
