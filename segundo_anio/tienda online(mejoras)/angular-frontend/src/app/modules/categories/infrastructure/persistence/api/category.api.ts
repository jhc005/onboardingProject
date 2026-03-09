import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CategoryRepository } from '../../../domain/category.repository';
import { Category } from '../../../domain/category';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CategoryApi implements CategoryRepository {
  private baseUrl = 'http://localhost:8080/api/categories';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.baseUrl);
  }

  create(category: Category): Observable<Category> {
    return this.http.post<Category>(this.baseUrl, category);
  }
}