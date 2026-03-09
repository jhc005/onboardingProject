import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { OrderRepository } from '../../../domain/order.repository';
import { Order } from '../../../domain/order';

@Injectable()
export class OrderApi implements OrderRepository {

    private baseUrl = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Order[]> {
    return this.http.get<Order[]>(this.baseUrl);
  }

  create(order: Order): Observable<Order> {
    return this.http.post<Order>(this.baseUrl, order);
  }

}