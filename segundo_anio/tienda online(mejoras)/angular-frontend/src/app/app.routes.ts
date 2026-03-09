import { Routes } from '@angular/router';

import { ProductPageComponent } from './pages/products/page.product-component';
import { CustomerPageComponent } from './pages/customers/page.customer-component';
import { OrderPageComponent } from './pages/orders/page.order-component';
import { CategoryPageComponent } from './pages/categories/page.category-component';

export const routes: Routes = [

  {
    path: '',
    redirectTo: 'products',
    pathMatch: 'full'
  },

  {
    path: 'products',
    component: ProductPageComponent
  },

  {
    path: 'customers',
    component: CustomerPageComponent
  },

  {
    path: 'orders',
    component: OrderPageComponent
  },

  {
    path: 'categories',
    component: CategoryPageComponent
  }

];