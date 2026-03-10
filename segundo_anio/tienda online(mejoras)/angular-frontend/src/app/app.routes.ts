import { Routes } from '@angular/router';

import { ProductPageComponent } from './pages/products/page.product-component';
import { CustomerPageComponent } from './pages/customers/page.customer-component';
import { OrderPageComponent } from './pages/orders/page.order-component';
import { CategoryPageComponent } from './pages/categories/page.category-component';
import { MainPageComponent } from './pages/main/page.main-component';

export const routes: Routes = [

  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },

  {
    path: 'home',
    component:MainPageComponent
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