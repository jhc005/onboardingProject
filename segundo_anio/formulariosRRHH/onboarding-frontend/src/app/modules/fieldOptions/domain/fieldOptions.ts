export type FieldOptionDraft = { optionLabel: string; optionValue: string };

export interface FieldOption {
  id: number;
  dataFieldId: number;
  optionLabel: string;
  optionValue: string;
  displayOrder: number;
}

export interface CreateFieldOptionRequest {
  dataFieldId: number;
  optionLabel: string;
  optionValue: string;
  displayOrder: number;
}
