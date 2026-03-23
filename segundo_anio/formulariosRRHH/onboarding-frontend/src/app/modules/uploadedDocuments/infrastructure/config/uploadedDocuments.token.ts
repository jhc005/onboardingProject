import { InjectionToken } from '@angular/core';
import { IUploadedDocumentsRepository } from '../../domain/uploadedDocuments.repository';

export const UPLOADED_DOCUMENTS_REPOSITORY = new InjectionToken<IUploadedDocumentsRepository>(
  'IUploadedDocumentsRepository'
);
