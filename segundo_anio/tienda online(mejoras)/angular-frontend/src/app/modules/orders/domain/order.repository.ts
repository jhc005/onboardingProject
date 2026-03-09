import { Observable } from 'rxjs';
import { Order } from './order';

export interface OrderRepository {

   getAll(): Observable<Order[]>;

   create(order: Order): Observable<Order>;

}