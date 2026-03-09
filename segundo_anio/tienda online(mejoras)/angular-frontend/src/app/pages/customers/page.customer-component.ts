import { Component } from '@angular/core';

import { CustomerListComponent } from '../../modules/customers/infrastructure/components/lists/customer-list.component';

@Component({
  selector: 'app-customer-page',
  standalone: true,
  imports: [
    CustomerListComponent
  ],
  templateUrl: './page.customer-component.html'
})
export class CustomerPageComponent {}