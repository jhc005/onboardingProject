export type FieldType = 'TEXT' | 'NUMBER' | 'SELECT' | 'TEXTAREA' | 'RADIO';

export interface FieldOptionItem {
  id: number;
  optionLabel: string;
  optionValue: string;
  displayOrder: number;
}

export interface DataField {
  id: number;
  label: string;
  fieldType: FieldType;
  validationRegex: string | null;
  minLength: number | null;
  maxLength: number | null;
  placeholder: string | null;
  isRequired: boolean;
  displayOrder: number;
  options: FieldOptionItem[];
}

export interface CreateDataFieldRequest {
  label: string;
  fieldType: FieldType;
  validationRegex?: string;
  minLength?: number;
  maxLength?: number;
  placeholder?: string;
  isRequired: boolean;
  displayOrder: number;
}
