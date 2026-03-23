import { Observable } from 'rxjs';
import { DocumentRequirement, CreateDocumentRequirementRequest } from './documentRequirements';

export interface IDocumentRequirementsRepository {
  findAll(): Observable<DocumentRequirement[]>;
  findAllForCandidate(): Observable<DocumentRequirement[]>;
  create(request: CreateDocumentRequirementRequest): Observable<DocumentRequirement>;
  update(id: number, request: CreateDocumentRequirementRequest): Observable<DocumentRequirement>;
  delete(id: number): Observable<void>;
  uploadTemplate(id: number, file: File): Observable<DocumentRequirement>;
  getTemplateDownloadUrl(id: number): string;
}
