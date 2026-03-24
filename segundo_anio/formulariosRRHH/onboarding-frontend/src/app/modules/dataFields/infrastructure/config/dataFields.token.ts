import { InjectionToken } from '@angular/core';
import { IDataFieldsRepository } from '../../domain/dataFields.repository';

export const DATA_FIELDS_REPOSITORY = new InjectionToken<IDataFieldsRepository>('IDataFieldsRepository');
