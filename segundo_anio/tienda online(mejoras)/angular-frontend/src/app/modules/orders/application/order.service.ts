import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { Order } from '../domain/order';
import { ORDER_REPOSITORY } from '../infrastructure/config/order.token';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private repository = inject(ORDER_REPOSITORY);

  getAll(): Observable<Order[]> {
    return this.repository.getAll();
  }

  create(order: Order): Observable<Order> {
    return this.repository.create(order);
  }

}