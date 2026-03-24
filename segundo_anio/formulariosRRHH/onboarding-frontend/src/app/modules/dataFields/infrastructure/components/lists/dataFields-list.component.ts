import { Component, inject, OnInit, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { DataFieldsService } from '../../../application/dataFields.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { DataField, FieldType } from '../../../domain/dataFields';
import { DataFieldsFormsComponent } from '../forms/dataFields-forms.component';

@Component({
  selector: 'app-data-fields-list',
  imports: [NgClass, DataFieldsFormsComponent],
  templateUrl: './dataFields-list.component.html',
  styleUrl: './dataFields-list.component.css',
})
export class DataFieldsListComponent implements OnInit {
  private readonly svc = inject(DataFieldsService);
  private readonly toast = inject(ToastService);

  fields = signal<DataField[]>([]);
  loading = signal(false);
  showModal = signal(false);
  editingField = signal<DataField | null>(null);
  deletingId = signal<number | null>(null);

  fieldTypeLabels: Record<FieldType, string> = {
    TEXT: 'Texto', NUMBER: 'Número', SELECT: 'Desplegable', TEXTAREA: 'Área de texto', RADIO: 'Opción única',
  };

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading.set(true);
    this.svc.findAll().subscribe({
      next: (data) => { this.fields.set(data.sort((a, b) => a.displayOrder - b.displayOrder)); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  openCreate(): void {
    this.editingField.set(null);
    this.showModal.set(true);
  }

  openEdit(field: DataField): void {
    this.editingField.set(field);
    this.showModal.set(true);
  }

  onSaved(): void {
    this.showModal.set(false);
    this.load();
  }

  delete(id: number): void {
    if (!confirm('¿Seguro que quieres eliminar este campo? Se perderán las respuestas asociadas.')) return;
    this.deletingId.set(id);
    this.svc.delete(id).subscribe({
      next: () => { this.toast.success('Campo eliminado'); this.load(); this.deletingId.set(null); },
      error: () => this.deletingId.set(null),
    });
  }
}
