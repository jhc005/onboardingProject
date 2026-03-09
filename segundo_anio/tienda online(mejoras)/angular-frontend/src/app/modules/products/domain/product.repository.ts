import { Observable } from 'rxjs';
import { Product } from './product';

export interface ProductRepository {

 getAll(categoryId?: number): Observable<Product[]>;

 getById(id: number): Observable<Product>;

 create(product: Product): Observable<Product>;

 update(id: number, product: Product): Observable<Product>;

 delete(id: number): Observable<void>;

}