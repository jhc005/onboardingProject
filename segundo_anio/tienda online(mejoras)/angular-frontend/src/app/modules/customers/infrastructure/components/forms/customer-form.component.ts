import { Component, inject, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../../application/customer.service';
import { Customer } from '../../../domain/customer';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-form.component.html',
  styleUrls: ['./customer-form.component.css']
})
export class CustomerFormComponent implements OnChanges {

  private customerService = inject(CustomerService);

  @Input() customerToEdit: Customer | null = null; 
  @Output() created = new EventEmitter<Customer>();
  @Output() updated = new EventEmitter<Customer>();
  @Output() cancelled = new EventEmitter<void>();

  newCustomer: Customer = this.emptyCustomer();

  ngOnChanges(changes: SimpleChanges) {
    if (changes['customerToEdit'] && this.customerToEdit) {
      this.newCustomer = { ...this.customerToEdit }; 
    }
  }

  emptyCustomer(): Customer {
    return { firstName: '', lastName: '', email: '', phone: '', active: true };
  }

  saveCustomer() {
    if (this.customerToEdit) {

      this.customerService.update(this.customerToEdit.id!, this.newCustomer).subscribe({
        next: (c: Customer) => {
          this.updated.emit(c);
          this.resetForm();
        }
      });
    } else {

      this.customerService.create(this.newCustomer).subscribe({
        next: (c: Customer) => {
          this.created.emit(c);
          this.resetForm();
        }
      });
    }
  }

  cancelForm() {
    this.cancelled.emit();
    this.resetForm();
  }

  private resetForm() {
    this.newCustomer = this.emptyCustomer();
    this.customerToEdit = null;
  }
}