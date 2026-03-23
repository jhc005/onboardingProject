import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { UploadedDocument } from '../domain/uploadedDocuments';
import { UPLOADED_DOCUMENTS_REPOSITORY } from '../infrastructure/config/uploadedDocuments.token';

@Injectable({ providedIn: 'root' })
export class UploadedDocumentsService {
  private readonly repository = inject(UPLOADED_DOCUMENTS_REPOSITORY);

  upload(processId: string, requirementId: number, file: File): Observable<UploadedDocument> {
    return this.repository.upload(processId, requirementId, file);
  }

  findByProcessId(processId: string): Observable<UploadedDocument[]> {
    return this.repository.findByProcessId(processId);
  }

  findByProcessAdmin(processId: string): Observable<UploadedDocument[]> {
    return this.repository.findByProcessAdmin(processId);
  }

  approve(docId: number): Observable<UploadedDocument> {
    return this.repository.approve(docId);
  }

  reject(docId: number, notes: string): Observable<UploadedDocument> {
    return this.repository.reject(docId, notes);
  }

  getDownloadUrl(docId: number): string {
    return this.repository.getDownloadUrl(docId);
  }
}
