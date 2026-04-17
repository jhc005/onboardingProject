export interface DocumentRequirement {
  id: number;
  name: string;
  description: string | null;
  allowedFormats: string;
  isRequired: boolean;
  displayOrder: number;
  templateOriginalFilename: string | null;
  hasTemplate: boolean;
}

export interface CreateDocumentRequirementRequest {
  name: string;
  description?: string;
  allowedFormats: string;
  isRequired: boolean;
  displayOrder: number;
}
