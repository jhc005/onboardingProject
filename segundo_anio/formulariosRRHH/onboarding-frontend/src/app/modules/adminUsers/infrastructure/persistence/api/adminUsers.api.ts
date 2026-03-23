import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { AdminUser, LoginRequest, LoginResponse, CreateAdminRequest } from '../../../domain/adminUsers';

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
}
