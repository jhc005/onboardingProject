export interface ValidateTokenRequest {
  token: string;
}

export interface ValidateTokenResponse {
  processId: string;
  candidateEmail: string;
  candidateFirstName: string;
  candidateLastName: string;
  status: string;
}
