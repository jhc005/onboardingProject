import { Injectable, inject } from '@angular/core';
import { Category } from '../domain/category';
import { Observable } from 'rxjs';
import { CATEGORY_REPOSITORY } from '../infrastructure/config/category.token';

@Injectable({ providedIn: 'root' })
export class CategoryService {
  private repository= inject(CATEGORY_REPOSITORY)

  getAll(): Observable<Category[]> {
    return this.repository.getAll();
  }

  create(category: Category): Observable<Category> {
    return this.repository.create(category);
  }
}