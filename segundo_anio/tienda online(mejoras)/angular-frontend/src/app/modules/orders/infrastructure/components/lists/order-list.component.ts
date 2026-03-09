import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../../application/order.service';
import { Order } from '../../../domain/order';
import { OrderFormComponent } from '../forms/order-form.component';

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [CommonModule, OrderFormComponent],
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.css']
})
export class OrderListComponent implements OnInit {

  private orderService = inject(OrderService);

  orders = signal<Order[]>([]);
  loading = signal(false);
  showForm = signal(false);

  ngOnInit() {
    this.fetchOrders();
  }

  fetchOrders() {
    this.loading.set(true);
    this.orderService.getAll().subscribe({
      next: data => { this.orders.set(data); this.loading.set(false); },
      error: err => { console.error(err); this.loading.set(false); }
    });
  }

  openCreateForm() {
    this.showForm.set(true);
  }

  closeForm() {
    this.showForm.set(false);
  }

  handleOrderCreated(order: Order) {
    this.orders.update(list => [...list, order]);
    this.closeForm();
  }

}