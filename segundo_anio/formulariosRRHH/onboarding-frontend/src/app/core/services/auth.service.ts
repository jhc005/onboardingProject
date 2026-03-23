import { Injectable, signal } from '@angular/core';

export interface AuthUser {
  id: number;
  email: string;
  fullName: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'civica_token';
  private readonly USER_KEY = 'civica_user';

  isAuthenticated = signal(!!localStorage.getItem(this.TOKEN_KEY));
  currentUser = signal<AuthUser | null>(
    JSON.parse(localStorage.getItem(this.USER_KEY) || 'null')
  );

  setSession(token: string, user: AuthUser): void {
    localStorage.setItem(this.TOKEN_KEY, token);
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    this.isAuthenticated.set(true);
    this.currentUser.set(user);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.isAuthenticated.set(false);
    this.currentUser.set(null);
  }
}
