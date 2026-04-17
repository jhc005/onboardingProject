import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { AdminUser, LoginRequest, LoginResponse, CreateAdminRequest } from '../../../domain/adminUsers';
import type { ForgotPasswordRequest, ResetPasswordRequest } from '../../../domain/adminUsers';

@Injectable({ providedIn: 'root' })
export class AdminUsersApi {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/api/admin`;

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.url}/auth/login`, request);
  }

  create(request: CreateAdminRequest): Observable<AdminUser> {
    return this.http.post<AdminUser>(`${this.url}/users`, request);
  }

  findAll(): Observable<AdminUser[]> {
    return this.http.get<AdminUser[]>(`${this.url}/users`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/users/${id}`);
  }

  forgotPassword(request: ForgotPasswordRequest): Observable<void> {
    return this.http.post<void>(`${this.url}/auth/forgot-password`, request);
  }

  resetPassword(request: ResetPasswordRequest): Observable<void> {
    return this.http.post<void>(`${this.url}/auth/reset-password`, request);
  }
}
