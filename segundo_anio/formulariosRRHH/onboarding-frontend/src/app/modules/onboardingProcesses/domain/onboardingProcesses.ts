export type OnboardingStatus =
  | 'PENDING_ACCESS'
  | 'IN_PROGRESS'
  | 'SUBMITTED'
  | 'VALIDATED'
  | 'REJECTED';

export interface OnboardingProcess {
  id: string;
  candidateId: string;
  candidateEmail: string;
  candidateFirstName: string;
  candidateLastName: string;
  status: OnboardingStatus;
  createdAt: string;
  submittedAt: string | null;
  validatedAt: string | null;
  notes: string | null;
}

export interface RejectRequest {
  notes: string;
}
