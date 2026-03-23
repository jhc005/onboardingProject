import { InjectionToken } from '@angular/core';
import { IDataAnswersRepository } from '../../domain/dataAnswers.repository';

export const DATA_ANSWERS_REPOSITORY = new InjectionToken<IDataAnswersRepository>(
  'IDataAnswersRepository'
);
