import { InjectionToken } from '@angular/core';
import { CategoryRepository } from '../../domain/category.repository';

export const CATEGORY_REPOSITORY = new InjectionToken<CategoryRepository>('CATEGORY_REPOSITORY');