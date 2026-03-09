import { Component } from '@angular/core';
import { CategoryListComponent } from '../../modules/categories/infrastructure/components/lists/category-list.component';


@Component({
  selector: 'app-category-page',
  standalone: true,
  imports: [CategoryListComponent],
  templateUrl: './page.category-component.html',
})
export class CategoryPageComponent {}