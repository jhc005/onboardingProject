import { Category } from './category';
import { Observable } from 'rxjs';

export interface CategoryRepository {
  getAll(): Observable<Category[]>;
  create(category: Category): Observable<Category>;
}