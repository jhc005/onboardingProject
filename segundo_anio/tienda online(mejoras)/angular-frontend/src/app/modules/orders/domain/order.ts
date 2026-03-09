import { Customer } from '../../customers/domain/customer';

export interface Order {
  id?: number;
  customer: Customer;
  orderDate?: string;
  status: string;
  total: number;
}