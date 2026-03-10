import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoryService } from '../../../application/category.service';
import { Category } from '../../../domain/category';
import { CategoryFormComponent } from '../forms/category-form.component';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [CommonModule, CategoryFormComponent],
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit {
  private categoryService = inject(CategoryService);

  categories = signal<Category[]>([]);
  loading = signal(false);

  ngOnInit() {
    this.fetchCategories();
  }

  fetchCategories() {
    this.loading.set(true);

    this.categoryService.getAll()
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: data => this.categories.set(data)
      });
  }

  createCategory(name: string) {
    this.categoryService.create({ name }).subscribe({
      next: c => this.categories.update(list => [...list, c]),
    });
  }
}