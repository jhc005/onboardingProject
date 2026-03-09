import { Observable } from 'rxjs';
import { Customer } from './customer';

export interface CustomerRepository {

 getAll(): Observable<Customer[]>;

 create(customer: Customer): Observable<Customer>;

 update(id: number, customer: Customer): Observable<Customer>;

}