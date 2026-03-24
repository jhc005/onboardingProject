import { Component, inject, Input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { forkJoin, of, switchMap } from 'rxjs';
import { DataFieldsService } from '../../../application/dataFields.service';
import { FieldOptionsService } from '../../../../fieldOptions/application/fieldOptions.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { DataField, FieldType } from '../../../domain/dataFields';
import { FieldOptionDraft } from '../../../../fieldOptions/domain/fieldOptions';
import { FieldOptionsFormsComponent } from '../../../../fieldOptions/infrastructure/components/fieldOptions-forms.component';

@Component({
  selector: 'app-data-fields-form',
  imports: [ReactiveFormsModule, FieldOptionsFormsComponent],
  templateUrl: './dataFields-forms.component.html',
})
export class DataFieldsFormsComponent implements OnInit {
  private readonly svc = inject(DataFieldsService);
  private readonly optionsSvc = inject(FieldOptionsService);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  @Input() field: DataField | null = null;
  @Input() nextOrder: number = 1;

  saved = output<void>();
  cancelled = output<void>();

  saving = signal(false);
  currentOptions = signal<FieldOptionDraft[]>([]);
  existingOptionIds = signal<number[]>([]);

  private editingId: number | null = null;

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

  ngOnInit(): void {
    if (this.field) {
      this.editingId = this.field.id;
      this.existingOptionIds.set(this.field.options.map(o => o.id));
      this.currentOptions.set(
        [...this.field.options]
          .sort((a, b) => a.displayOrder - b.displayOrder)
          .map(o => ({ optionLabel: o.optionLabel, optionValue: o.optionValue }))
      );
      this.form.patchValue({
        label: this.field.label, fieldType: this.field.fieldType,
        placeholder: this.field.placeholder || '', isRequired: this.field.isRequired,
        displayOrder: this.field.displayOrder,
      });
    } else {
      this.editingId = null;
      this.currentOptions.set([]);
      this.existingOptionIds.set([]);
      this.form.reset({ label: '', fieldType: 'TEXT', placeholder: '', isRequired: true, displayOrder: this.nextOrder });
    }
  }

  get isSelectType(): boolean {
    const t = this.form.get('fieldType')?.value;
    return t === 'SELECT' || t === 'RADIO';
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
    const fieldObs = this.editingId ? this.svc.update(this.editingId, req) : this.svc.create(req);

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
        this.toast.success(this.editingId ? 'Campo actualizado' : 'Campo creado');
        this.saving.set(false);
        this.saved.emit();
      },
      error: () => this.saving.set(false),
    });
  }
}
