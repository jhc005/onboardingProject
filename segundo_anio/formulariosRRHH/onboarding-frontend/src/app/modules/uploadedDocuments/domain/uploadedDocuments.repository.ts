import { Observable } from 'rxjs';
import { UploadedDocument } from './uploadedDocuments';

export interface IUploadedDocumentsRepository {
  upload(processId: string, requirementId: number, file: File): Observable<UploadedDocument>;
  findByProcessId(processId: string): Observable<UploadedDocument[]>;
  findByProcessAdmin(processId: string): Observable<UploadedDocument[]>;
  approve(docId: number): Observable<UploadedDocument>;
  reject(docId: number, notes: string): Observable<UploadedDocument>;
  download(docId: number): Observable<Blob>;
}
