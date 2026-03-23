import { Observable } from 'rxjs';
import { ValidateTokenRequest, ValidateTokenResponse } from './acessTokens';

export interface IAcessTokensRepository {
  validateToken(request: ValidateTokenRequest): Observable<ValidateTokenResponse>;
}
