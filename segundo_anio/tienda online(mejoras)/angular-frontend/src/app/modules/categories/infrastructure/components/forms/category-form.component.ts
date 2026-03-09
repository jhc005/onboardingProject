import { Component, EventEmitter, Output, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-category-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './category-form.component.html',
  styleUrls: ['./category-form.component.css']
})
export class CategoryFormComponent {
  newCategory = signal('');

  @Output() create = new EventEmitter<string>();

  onSubmit() {
    const name = this.newCategory();
    if (!name.trim()) return;
    this.create.emit(name.trim());
    this.newCategory.set('');
  }
}