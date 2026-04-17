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
  placeholder: string | null;
  isRequired: boolean;
  displayOrder: number;
  options: FieldOptionItem[];
}

export interface CreateDataFieldRequest {
  label: string;
  fieldType: FieldType;
  placeholder?: string;
  isRequired: boolean;
  displayOrder: number;
}
