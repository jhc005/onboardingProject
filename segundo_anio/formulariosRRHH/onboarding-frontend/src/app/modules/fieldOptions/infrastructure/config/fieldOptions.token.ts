import { InjectionToken } from '@angular/core';
import { IFieldOptionsRepository } from '../../domain/fieldOptions.repository';

export const FIELD_OPTIONS_REPOSITORY = new InjectionToken<IFieldOptionsRepository>('IFieldOptionsRepository');
