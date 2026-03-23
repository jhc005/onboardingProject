import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ValidateTokenResponse } from '../domain/acessTokens';
import { ACESS_TOKENS_REPOSITORY } from '../infrastructure/config/acessTokens.token';

@Injectable({ providedIn: 'root' })
export class AcessTokensService {
  private repository = inject(ACESS_TOKENS_REPOSITORY);

  validateToken(token: string): Observable<ValidateTokenResponse> {
    return this.repository.validateToken({ token });
  }
}
