export interface AnswerItem {
  dataFieldId: number;
  answerValue: string;
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
  createdAt: string;
}
