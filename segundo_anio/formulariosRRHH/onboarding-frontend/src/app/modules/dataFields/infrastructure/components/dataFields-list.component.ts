import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgClass } from '@angular/common';
import { DataFieldsService } from '../../application/dataFields.service';
import { ToastService } from '../../../../core/services/toast.service';
import { DataField, FieldType } from '../../domain/dataFields';

@Component({
  selector: 'app-data-fields-list',
  imports: [ReactiveFormsModule, NgClass],
  templateUrl: './dataFields-list.component.html',
  styleUrl: './dataFields-list.component.css',
})
export class DataFieldsListComponent implements OnInit {
  private readonly svc = inject(DataFieldsService);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  fields = signal<DataField[]>([]);
  loading = signal(false);
  showModal = signal(false);
  editingId = signal<number | null>(null);
  saving = signal(false);
  deletingId = signal<number | null>(null);

  fieldTypes: FieldType[] = ['TEXT', 'NUMBER', 'SELECT', 'TEXTAREA', 'RADIO'];
  fieldTypeLabels: Record<FieldType, string> = {
    TEXT: 'Texto', NUMBER: 'Número', SELECT: 'Desplegable', TEXTAREA: 'Área de texto', RADIO: 'Opción única',
  };

  form = this.fb.group({
    label: ['', Validators.required],
    fieldType: ['TEXT' as FieldType, Validators.required],
    placeholder: [''],
    isRequired: [true],
    displayOrder: [1, [Validators.required, Validators.min(1)]],
  });

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading.set(true);
    this.svc.findAll().subscribe({
      next: (data) => { this.fields.set(data.sort((a, b) => a.displayOrder - b.displayOrder)); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  openCreate(): void {
    this.editingId.set(null);
    this.form.reset({ label: '', fieldType: 'TEXT', placeholder: '', isRequired: true, displayOrder: this.fields().length + 1 });
    this.showModal.set(true);
  }

  openEdit(field: DataField): void {
    this.editingId.set(field.id);
    this.form.patchValue({
      label: field.label, fieldType: field.fieldType,
      placeholder: field.placeholder || '', isRequired: field.isRequired,
      displayOrder: field.displayOrder,
    });
    this.showModal.set(true);
  }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const val = this.form.value;
    const req = {
      label: val.label!, fieldType: val.fieldType!,
      placeholder: val.placeholder || undefined,
      isRequired: val.isRequired!,
      displayOrder: val.displayOrder!,
    };
    const id = this.editingId();
    const obs = id ? this.svc.update(id, req) : this.svc.create(req);
    obs.subscribe({
      next: () => {
        this.toast.success(id ? 'Campo actualizado' : 'Campo creado');
        this.showModal.set(false);
        this.load();
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
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
