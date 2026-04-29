export type ValidationStatus = 'PENDING_VALIDATION' | 'APPROVED' | 'REJECTED';

export interface UploadedDocument {
  id: number;
  onboardingProcessId: string;
  documentRequirementId: number;
  requirementName: string;
  originalFilename: string;
  mimeType: string;
  fileSizeBytes: number;
  validationStatus: ValidationStatus;
  uploadedAt: string;
}
