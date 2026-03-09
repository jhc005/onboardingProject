import { Component } from '@angular/core';

import { ProductListComponent } from '../../modules/products/infrastructure/components/lists/product.list-component';

@Component({
  selector: 'app-product-page',
  standalone: true,
  imports: [ ProductListComponent],
  templateUrl: './page.product-component.html',
})
export class ProductPageComponent {}