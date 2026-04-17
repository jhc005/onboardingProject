import { Observable } from 'rxjs';
import { AdminUser, LoginRequest, LoginResponse, CreateAdminRequest, ForgotPasswordRequest, ResetPasswordRequest } from './adminUsers';

export interface IAdminUsersRepository {
  login(request: LoginRequest): Observable<LoginResponse>;
  create(request: CreateAdminRequest): Observable<AdminUser>;
  findAll(): Observable<AdminUser[]>;
  delete(id: number): Observable<void>;
  forgotPassword(request: ForgotPasswordRequest): Observable<void>;
  resetPassword(request: ResetPasswordRequest): Observable<void>;
}
