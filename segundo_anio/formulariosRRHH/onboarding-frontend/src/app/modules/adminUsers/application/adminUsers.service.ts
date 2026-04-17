import { Injectable, inject } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';
import { LoginRequest, LoginResponse, AdminUser, CreateAdminRequest } from '../domain/adminUsers';
import type { ForgotPasswordRequest, ResetPasswordRequest } from '../domain/adminUsers';
import { ADMIN_USERS_REPOSITORY } from '../infrastructure/config/adminUsers.token';

@Injectable({ providedIn: 'root' })
export class AdminUsersService {
  private readonly repository = inject(ADMIN_USERS_REPOSITORY);
  private readonly auth = inject(AuthService);

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.repository.login(request).pipe(
      tap((res) =>
        this.auth.setSession(res.token, { id: res.id, email: res.email, fullName: res.fullName })
      )
    );
  }

  create(request: CreateAdminRequest): Observable<AdminUser> {
    return this.repository.create(request);
  }

  findAll(): Observable<AdminUser[]> {
    return this.repository.findAll();
  }

  delete(id: number): Observable<void> {
    return this.repository.delete(id);
  }

  forgotPassword(request: ForgotPasswordRequest): Observable<void> {
    return this.repository.forgotPassword(request);
  }

  resetPassword(request: ResetPasswordRequest): Observable<void> {
    return this.repository.resetPassword(request);
  }
}
