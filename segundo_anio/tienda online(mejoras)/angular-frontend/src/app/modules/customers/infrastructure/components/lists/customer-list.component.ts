import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerService } from '../../../application/customer.service';
import { Customer } from '../../../domain/customer';
import { CustomerFormComponent } from "../forms/customer-form.component";
import { finalize } from 'rxjs';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [CommonModule, CustomerFormComponent],
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit {

  private customerService = inject(CustomerService);

  customer = signal<Customer[]>([]);
  loading = signal(false);
  showForm = signal(false);
  editingCustomer = signal<Customer | null>(null);

  ngOnInit() {
    this.fetchCustomers();
  }

  openCreateForm() {
    this.editingCustomer.set(null);
    this.showForm.set(true);
  }

  openEditForm(customer: Customer) {
    this.editingCustomer.set(customer);
    this.showForm.set(true);
  }

  closeForm() {
    this.showForm.set(false);
    this.editingCustomer.set(null);
  }

  handleCustomerCreated(customer: Customer) {
    this.customer.update(list => [...list, customer]);
    this.closeForm();
  }

  handleCustomerUpdated(customer: Customer) {
    this.customer.update(list => list.map(c => c.id === customer.id ? customer : c));
    this.closeForm();
  }

  fetchCustomers() {
    this.loading.set(true);

    this.customerService.getAll()
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({ 
        next: data => { this.customer.set(data)}
      });
  }
}