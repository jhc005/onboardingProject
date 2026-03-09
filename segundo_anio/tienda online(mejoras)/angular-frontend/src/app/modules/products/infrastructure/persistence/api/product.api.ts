import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ProductRepository } from '../../../domain/product.repository';
import { Product } from '../../../domain/product';

@Injectable()
export class ProductApi implements ProductRepository {

    private baseUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) {}

  getAll(categoryId?: number): Observable<Product[]> {

    const url = categoryId
      ? `${this.baseUrl}?categoryId=${categoryId}`
      : this.baseUrl;

    return this.http.get<Product[]>(url);
  }

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/${id}`);
  }

  create(product: Product): Observable<Product> {
    return this.http.post<Product>(this.baseUrl, product);
  }

  update(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.baseUrl}/${id}`, product);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

}