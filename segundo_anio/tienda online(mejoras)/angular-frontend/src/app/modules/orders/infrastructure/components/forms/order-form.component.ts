import { Component, EventEmitter, inject, Output, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../../application/order.service';
import { CustomerService } from '../../../../customers/application/customer.service';
import { ProductService } from '../../../../products/application/product.service';
import { Order } from '../../../domain/order';
import { Customer } from '../../../../customers/domain/customer';
import { Product } from '../../../../products/domain/product';

@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './order-form.component.html',
  styleUrls: ['./order-form.component.css']
})
export class OrderFormComponent implements OnInit {

  private orderService = inject(OrderService);
  private customerService = inject(CustomerService);
  private productService = inject(ProductService);

  customers = signal<Customer[]>([]);
  products = signal<Product[]>([]);

  newOrder: Order = { customer: null!, status: 'pending', total: 0 };
  selectedProduct: Product | null = null;

  @Output() orderCreated = new EventEmitter<Order>();
  @Output() cancel = new EventEmitter<void>();

  ngOnInit() {
    this.fetchCustomers();
    this.fetchProducts();
  }

  fetchCustomers() {
    this.customerService.getAll().subscribe({
      next: data => this.customers.set(data),
      error: err => console.error(err)
    });
  }

  fetchProducts() {
    this.productService.getAll().subscribe({
      next: data => this.products.set(data),
      error: err => console.error(err)
    });
  }

  updateTotal() {
    this.newOrder.total = this.selectedProduct ? this.selectedProduct.price : 0;
  }

  createOrder() {
    if (!this.newOrder.customer || !this.selectedProduct) {
      return alert('Seleccione cliente y producto');
    }

    this.newOrder.status = 'pending';

    this.orderService.create(this.newOrder).subscribe({
      next: order => {
        this.orderCreated.emit(order);
        this.resetForm();
      },
      error: err => console.error(err)
    });
  }

  resetForm() {
    this.newOrder = { customer: null!, status: 'pending', total: 0 };
    this.selectedProduct = null;
  }

  cancelForm() {
    this.resetForm();
    this.cancel.emit();
  }
}