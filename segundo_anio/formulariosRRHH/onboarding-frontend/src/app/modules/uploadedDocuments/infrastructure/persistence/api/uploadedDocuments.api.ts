import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { UploadedDocument } from '../../../domain/uploadedDocuments';

@Injectable({ providedIn: 'root' })
export class UploadedDocumentsApi {
  private readonly http = inject(HttpClient);
  private readonly base = `${environment.apiUrl}/api/candidate/form`;
  private readonly adminBase = `${environment.apiUrl}/api/admin`;

  upload(processId: string, requirementId: number, file: File): Observable<UploadedDocument> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<UploadedDocument>(
      `${this.base}/${processId}/documents/${requirementId}/upload`,
      formData
    );
  }

  findByProcessId(processId: string): Observable<UploadedDocument[]> {
    return this.http.get<UploadedDocument[]>(`${this.base}/${processId}/documents`);
  }

  findByProcessAdmin(processId: string): Observable<UploadedDocument[]> {
    return this.http.get<UploadedDocument[]>(`${this.adminBase}/processes/${processId}/documents`);
  }

  approve(docId: number): Observable<UploadedDocument> {
    return this.http.patch<UploadedDocument>(`${this.adminBase}/documents/${docId}/approve`, {});
  }

  reject(docId: number, notes: string): Observable<UploadedDocument> {
    return this.http.patch<UploadedDocument>(`${this.adminBase}/documents/${docId}/reject`, { notes });
  }

  download(docId: number): Observable<Blob> {
    return this.http.get(`${this.adminBase}/documents/${docId}/download`, { responseType: 'blob' });
  }
}
