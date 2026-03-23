export interface AnswerItem {
  dataFieldId: number;
  answerValue: string;
  fieldOptionId?: number;
}

export interface SaveAnswersRequest {
  answers: AnswerItem[];
}

export interface DataAnswer {
  id: number;
  onboardingProcessId: string;
  dataFieldId: number;
  fieldLabel: string;
  answerValue: string;
  fieldOptionId: number | null;
  optionLabel: string | null;
  createdAt: string;
}
