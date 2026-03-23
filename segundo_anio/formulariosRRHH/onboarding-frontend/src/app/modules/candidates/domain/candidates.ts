export interface Candidate {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  createdAt: string;
}

export interface CreateCandidateRequest {
  email: string;
  firstName: string;
  lastName: string;
}
