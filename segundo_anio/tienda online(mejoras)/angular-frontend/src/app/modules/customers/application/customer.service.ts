import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../domain/customer';
import { CUSTOMER_REPOSITORY} from '../infrastructure/config/customer.token';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private repository = inject(CUSTOMER_REPOSITORY);

  getAll(): Observable<Customer[]> {
    return this.repository.getAll();
  }

  create(customer: Customer): Observable<Customer> {
    return this.repository.create(customer);
  }

  update(id: number, customer: Customer): Observable<Customer> {
    return this.repository.update(id, customer);
  }

}