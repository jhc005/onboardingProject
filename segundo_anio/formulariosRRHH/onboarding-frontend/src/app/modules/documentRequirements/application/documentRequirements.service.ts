import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { DocumentRequirement, CreateDocumentRequirementRequest } from '../domain/documentRequirements';
import { DOCUMENT_REQUIREMENTS_REPOSITORY } from '../infrastructure/config/documentRequirements.token';

@Injectable({ providedIn: 'root' })
export class DocumentRequirementsService {
  private readonly repository = inject(DOCUMENT_REQUIREMENTS_REPOSITORY);

  findAll(): Observable<DocumentRequirement[]> {
    return this.repository.findAll();
  }

  findAllForCandidate(): Observable<DocumentRequirement[]> {
    return this.repository.findAllForCandidate();
  }

  canModify(): Observable<boolean> {
    return this.repository.canModify();
  }

  create(request: CreateDocumentRequirementRequest): Observable<DocumentRequirement> {
    return this.repository.create(request);
  }

  update(id: number, request: CreateDocumentRequirementRequest): Observable<DocumentRequirement> {
    return this.repository.update(id, request);
  }

  delete(id: number): Observable<void> {
    return this.repository.delete(id);
  }

  uploadTemplate(id: number, file: File): Observable<DocumentRequirement> {
    return this.repository.uploadTemplate(id, file);
  }

  downloadTemplate(id: number): Observable<Blob> {
    return this.repository.downloadTemplate(id);
  }

  downloadTemplateForCandidate(id: number): Observable<Blob> {
    return this.repository.downloadTemplateForCandidate(id);
  }
}
