import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgClass } from '@angular/common';
import { forkJoin, of, switchMap } from 'rxjs';
import { DataFieldsService } from '../../application/dataFields.service';
import { FieldOptionsService } from '../../../fieldOptions/application/fieldOptions.service';
import { ToastService } from '../../../../core/services/toast.service';
import { DataField, FieldType } from '../../domain/dataFields';
import { FieldOptionDraft } from '../../../fieldOptions/domain/fieldOptions';
import { FieldOptionsFormsComponent } from '../../../fieldOptions/infrastructure/components/fieldOptions-forms.component';

@Component({
  selector: 'app-data-fields-list',
  imports: [ReactiveFormsModule, NgClass, FieldOptionsFormsComponent],
  templateUrl: './dataFields-list.component.html',
  styleUrl: './dataFields-list.component.css',
})
export class DataFieldsListComponent implements OnInit {
  private readonly svc = inject(DataFieldsService);
  private readonly optionsSvc = inject(FieldOptionsService);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  fields = signal<DataField[]>([]);
  loading = signal(false);
  showModal = signal(false);
  editingId = signal<number | null>(null);
  saving = signal(false);
  deletingId = signal<number | null>(null);

  currentOptions = signal<FieldOptionDraft[]>([]);
  existingOptionIds = signal<number[]>([]);

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

  get isSelectType(): boolean {
    const t = this.form.get('fieldType')?.value;
    return t === 'SELECT' || t === 'RADIO';
  }

  openCreate(): void {
    this.editingId.set(null);
    this.currentOptions.set([]);
    this.existingOptionIds.set([]);
    this.form.reset({ label: '', fieldType: 'TEXT', placeholder: '', isRequired: true, displayOrder: this.fields().length + 1 });
    this.showModal.set(true);
  }

  openEdit(field: DataField): void {
    this.editingId.set(field.id);
    this.existingOptionIds.set(field.options.map(o => o.id));
    this.currentOptions.set(
      [...field.options]
        .sort((a, b) => a.displayOrder - b.displayOrder)
        .map(o => ({ optionLabel: o.optionLabel, optionValue: o.optionValue }))
    );
    this.form.patchValue({
      label: field.label, fieldType: field.fieldType,
      placeholder: field.placeholder || '', isRequired: field.isRequired,
      displayOrder: field.displayOrder,
    });
    this.showModal.set(true);
  }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const val = this.form.value;
    const isSelect = val.fieldType === 'SELECT' || val.fieldType === 'RADIO';
    if (isSelect && this.currentOptions().length === 0) {
      this.toast.error('Añade al menos una opción para este tipo de campo');
      return;
    }
    this.saving.set(true);
    const req = {
      label: val.label!, fieldType: val.fieldType!,
      placeholder: val.placeholder || undefined,
      isRequired: val.isRequired!,
      displayOrder: val.displayOrder!,
    };
    const id = this.editingId();
    const fieldObs = id ? this.svc.update(id, req) : this.svc.create(req);

    fieldObs.pipe(
      switchMap(savedField => {
        if (!isSelect) return of(null);

        const deleteOps = this.existingOptionIds().length > 0
          ? forkJoin(this.existingOptionIds().map(oid => this.optionsSvc.delete(savedField.id, oid)))
          : of([]);

        return deleteOps.pipe(
          switchMap(() =>
            forkJoin(
              this.currentOptions().map((o, i) =>
                this.optionsSvc.create(savedField.id, {
                  dataFieldId: savedField.id,
                  optionLabel: o.optionLabel,
                  optionValue: o.optionValue,
                  displayOrder: i + 1,
                })
              )
            )
          )
        );
      })
    ).subscribe({
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
