import { InjectionToken } from '@angular/core';
import { ICandidatesRepository } from '../../domain/candidates.repository';

export const CANDIDATES_REPOSITORY = new InjectionToken<ICandidatesRepository>('ICandidatesRepository');
