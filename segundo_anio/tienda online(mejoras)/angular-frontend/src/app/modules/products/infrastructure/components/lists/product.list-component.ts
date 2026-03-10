import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Product } from '../../../domain/product';
import { ProductService } from '../../../application/product.service';

import { ProductFormComponent } from '../forms/product.form-component';

import { CategoryService } from '../../../../categories/application/category.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, ProductFormComponent],
  templateUrl: './product.list-component.html',
  styleUrls: ['./product.list-component.css']
})
export class ProductListComponent implements OnInit {

  private productService = inject(ProductService);
  private categoryService = inject(CategoryService);

  products = signal<Product[]>([]);
  categories = signal<any[]>([]);
  loading = signal(false);

  showForm = signal(false);
  editingProduct = signal<Product | null>(null);

  ngOnInit() {
    this.fetchProducts();
    this.fetchCategories();
  }

  fetchProducts() {

  this.loading.set(true);

  this.productService.getAll()
    .pipe( finalize( () => this.loading.set(false)))
    .subscribe({
      next: data => this.products.set(data)
    });

}

  fetchCategories() {

    this.categoryService.getAll().subscribe({
      next: data => this.categories.set(data)
    });

  }

  createProduct() {
    this.editingProduct.set(null);
    this.showForm.set(true);
  }

  editProduct(product: Product) {
    this.editingProduct.set(product);
    this.showForm.set(true);
  }

  deleteProduct(id: number) {

    if (!confirm('¿Eliminar producto?')) return;

    this.productService.delete(id).subscribe({
      next: () => {
        this.products.update(list =>
          list.filter(p => p.id !== id)
        );
      }
    });

  }

  onSaved(product: Product) {

    if (product.id && this.products().some(p => p.id === product.id)) {

      this.products.update(list =>
        list.map(p => p.id === product.id ? product : p)
      );
    } else {
      this.products.update(list => [...list, product]);
    }

    this.showForm.set(false);

  }

  cancelForm() {
    this.showForm.set(false);
  }

}