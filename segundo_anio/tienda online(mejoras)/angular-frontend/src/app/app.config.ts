import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { errorInterceptor } from './core/interceptors/error.interceptor';
import { successInterceptor } from './core/interceptors/success.interceptor';

import { routes } from './app.routes';

import { ProductApi} from './modules/products/infrastructure/persistence/api/product.api';
import { CustomerApi } from './modules/customers/infrastructure/persistence/api/customer.api';
import { OrderApi } from './modules/orders/infrastructure/persistence/api/order.api';
import { CategoryApi } from './modules/categories/infrastructure/persistence/api/category.api';

import { PRODUCT_REPOSITORY } from './modules/products/infrastructure/config/product.tokens';
import { CATEGORY_REPOSITORY } from './modules/categories/infrastructure/config/category.token';
import { ORDER_REPOSITORY } from './modules/orders/infrastructure/config/order.token';
import { CUSTOMER_REPOSITORY } from './modules/customers/infrastructure/config/customer.token';
import { provideToastr } from 'ngx-toastr';
import { provideAnimations } from '@angular/platform-browser/animations';

export const appConfig: ApplicationConfig = {
  providers: [

    provideRouter(routes),
    provideAnimations(),
    provideToastr({
      positionClass: 'toast-top-right',
      preventDuplicates:true,
      progressBar:true,
      timeOut:3000
    }),
    provideHttpClient(withInterceptors([errorInterceptor,successInterceptor])),

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