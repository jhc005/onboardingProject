import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Product } from '../domain/product';
import { PRODUCT_REPOSITORY } from '../infrastructure/config/product.tokens';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private repository = inject(PRODUCT_REPOSITORY);

  getAll(categoryId?: number): Observable<Product[]> {
    return this.repository.getAll(categoryId);
  }

  getById(id: number): Observable<Product> {
    return this.repository.getById(id);
  }

  create(product: Product): Observable<Product> {
    return this.repository.create(product);
  }

  update(id: number, product: Product): Observable<Product> {
    return this.repository.update(id, product);
  }

  delete(id: number): Observable<void> {
    return this.repository.delete(id);
  }

}