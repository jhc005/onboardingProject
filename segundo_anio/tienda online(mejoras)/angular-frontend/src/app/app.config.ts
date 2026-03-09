import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { routes } from './app.routes';

import { ProductApi} from './modules/products/infrastructure/persistence/api/product.api';
import { CustomerApi } from './modules/customers/infrastructure/persistence/api/customer.api';
import { OrderApi } from './modules/orders/infrastructure/persistence/api/order.api';
import { CategoryApi } from './modules/categories/infrastructure/persistence/api/category.api';

import { PRODUCT_REPOSITORY } from './modules/products/infrastructure/config/product.tokens';
import { CATEGORY_REPOSITORY } from './modules/categories/infrastructure/config/category.token';
import { ORDER_REPOSITORY } from './modules/orders/infrastructure/config/order.token';
import { CUSTOMER_REPOSITORY } from './modules/customers/infrastructure/config/customer.token';

export const appConfig: ApplicationConfig = {
  providers: [

    provideRouter(routes),
    provideHttpClient(),

    {
      provide: PRODUCT_REPOSITORY,
      useClass: ProductApi
    },

    {
      provide: CUSTOMER_REPOSITORY,
      useClass: CustomerApi
    },

    {
      provide: ORDER_REPOSITORY,
      useClass: OrderApi
    },

    {
      provide: CATEGORY_REPOSITORY,
      useClass: CategoryApi
    }

  ]
};