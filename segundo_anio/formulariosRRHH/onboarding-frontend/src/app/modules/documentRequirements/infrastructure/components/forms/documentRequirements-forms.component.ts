import { Component, inject, Input, OnInit, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { DocumentRequirementsService } from '../../../application/documentRequirements.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { DocumentRequirement } from '../../../domain/documentRequirements';

@Component({
  selector: 'app-document-requirements-form',
  imports: [ReactiveFormsModule],
  templateUrl: './documentRequirements-forms.component.html',
  styleUrl: './documentRequirements-forms.component.css',
})
export class DocumentRequirementsFormsComponent implements OnInit {
  readonly svc = inject(DocumentRequirementsService);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  @Input() requirement: DocumentRequirement | null = null;
  @Input() nextOrder: number = 1;

  saved = output<void>();
  cancelled = output<void>();

  saving = signal(false);
  uploadingTemplate = signal(false);
  editingId = signal<number | null>(null);
  editingReq = signal<DocumentRequirement | null>(null);

  readonly formatOptions = ['PDF', 'JPG', 'PNG', 'PDF,JPG,PNG', 'PDF,JPG', 'JPG,PNG'];

  form = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    allowedFormats: ['PDF', Validators.required],
    isRequired: [true],
    displayOrder: [1, [Validators.required, Validators.min(1)]],
  });

  ngOnInit(): void {
    if (this.requirement) {
      this.editingId.set(this.requirement.id);
      this.editingReq.set(this.requirement);
      this.form.patchValue({
        name: this.requirement.name,
        description: this.requirement.description || '',
        allowedFormats: this.requirement.allowedFormats,
        isRequired: this.requirement.isRequired,
        displayOrder: this.requirement.displayOrder,
      });
    } else {
      this.editingId.set(null);
      this.editingReq.set(null);
      this.form.reset({ name: '', description: '', allowedFormats: 'PDF', isRequired: true, displayOrder: this.nextOrder });
    }
  }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const val = this.form.value;
    const request = {
      name: val.name!,
      description: val.description || undefined,
      allowedFormats: val.allowedFormats!,
      isRequired: val.isRequired!,
      isConditional: false,
      displayOrder: val.displayOrder!,
    };
    const id = this.editingId();
    const obs = id ? this.svc.update(id, request) : this.svc.create(request);
    obs.subscribe({
      next: (saved) => {
        this.toast.success(id ? 'Requisito actualizado' : 'Requisito creado. Ahora puedes subir una plantilla.');
        this.editingId.set(saved.id);
        this.editingReq.set(saved);
        this.saving.set(false);
        this.saved.emit();
      },
      error: () => this.saving.set(false),
    });
  }

  downloadTemplate(id: number, filename: string): void {
    this.svc.downloadTemplate(id).subscribe(blob => {
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = filename;
      a.click();
      URL.revokeObjectURL(url);
    });
  }

  onTemplateSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    const id = this.editingId();
    if (!file || !id) return;

    this.uploadingTemplate.set(true);
    this.svc.uploadTemplate(id, file).subscribe({
      next: (updated) => {
        this.editingReq.set(updated);
        this.toast.success('Plantilla subida correctamente');
        this.uploadingTemplate.set(false);
        input.value = '';
        this.saved.emit();
      },
      error: () => {
        this.toast.error('Error al subir la plantilla');
        this.uploadingTemplate.set(false);
        input.value = '';
      },
    });
  }
}
