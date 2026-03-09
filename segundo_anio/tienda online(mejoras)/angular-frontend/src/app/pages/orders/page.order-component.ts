import { Component } from '@angular/core';

import { OrderListComponent } from '../../modules/orders/infrastructure/components//lists/order-list.component';

@Component({
  selector: 'app-order-page',
  standalone: true,
  imports: [
    OrderListComponent
  ],
  templateUrl: './page.order-component.html'
})
export class OrderPageComponent {}