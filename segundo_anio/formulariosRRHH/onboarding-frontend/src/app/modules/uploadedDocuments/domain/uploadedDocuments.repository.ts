import { Observable } from 'rxjs';
import { UploadedDocument } from './uploadedDocuments';

export interface IUploadedDocumentsRepository {
  upload(processId: string, requirementId: number, file: File): Observable<UploadedDocument>;
  findByProcessId(processId: string): Observable<UploadedDocument[]>;
}
