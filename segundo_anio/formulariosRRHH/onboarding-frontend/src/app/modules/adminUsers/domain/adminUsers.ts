export interface AdminUser {
  id: number;
  email: string;
  fullName: string;
  createdAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  id: number;
  email: string;
  fullName: string;
  token: string;
}

export interface CreateAdminRequest {
  email: string;
  password: string;
  fullName: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}
