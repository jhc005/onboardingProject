import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Product } from '../../../domain/product';
import { ProductService } from '../../../application/product.service';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product.form-component.html',
  styleUrls: ['./product.form-component.css']
})
export class ProductFormComponent {

  private productService = inject(ProductService);

  @Input() product: Product | null = null;
  @Input() categories: any[] = [];

  @Output() saved = new EventEmitter<Product>();
  @Output() cancelled = new EventEmitter<void>();

  model: Product = {
    name: '',
    description: '',
    price: 0,
    stock: 0,
    categoryId: 0
  };

  ngOnChanges() {
    if (this.product) {
      this.model = { ...this.product };
    }
  }

  save() {

    if (this.model.id) {
      this.productService.update(this.model.id, this.model).subscribe(p => {
        this.saved.emit(p);
      });
    } else {
      this.productService.create(this.model).subscribe(p => {
        this.saved.emit(p);
      });
    }

  }

  cancel() {
    this.cancelled.emit();
  }

}