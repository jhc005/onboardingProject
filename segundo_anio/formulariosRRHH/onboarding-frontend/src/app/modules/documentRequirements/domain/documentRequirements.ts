export interface DocumentRequirement {
  id: number;
  name: string;
  description: string | null;
  allowedFormats: string;
  fileNameRegex: string | null;
  isRequired: boolean;
  isConditional: boolean;
  conditionDescription: string | null;
  displayOrder: number;
  templateOriginalFilename: string | null;
  hasTemplate: boolean;
}

export interface CreateDocumentRequirementRequest {
  name: string;
  description?: string;
  allowedFormats: string;
  fileNameRegex?: string;
  isRequired: boolean;
  isConditional: boolean;
  conditionDescription?: string;
  displayOrder: number;
}
