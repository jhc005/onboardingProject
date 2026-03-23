import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../../../environments/environment';
import { ValidateTokenRequest, ValidateTokenResponse } from '../../../domain/acessTokens';

@Injectable({ providedIn: 'root' })
export class AccessTokensApi {
  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUrl}/api/auth`;

  validateToken(request: ValidateTokenRequest): Observable<ValidateTokenResponse> {
    return this.http.post<ValidateTokenResponse>(`${this.url}/validate-token`, request);
  }
}
