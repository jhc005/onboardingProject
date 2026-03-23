import { InjectionToken } from '@angular/core';
import { IAcessTokensRepository } from '../../domain/acessTokens.repository';

export const ACESS_TOKENS_REPOSITORY = new InjectionToken<IAcessTokensRepository>(
  'IAcessTokensRepository'
);
