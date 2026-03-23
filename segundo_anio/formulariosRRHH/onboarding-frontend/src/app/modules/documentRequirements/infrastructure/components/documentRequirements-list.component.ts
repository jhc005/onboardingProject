import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgClass } from '@angular/common';
import { DocumentRequirementsService } from '../../application/documentRequirements.service';
import { ToastService } from '../../../../core/services/toast.service';
import { DocumentRequirement } from '../../domain/documentRequirements';

@Component({
  selector: 'app-document-requirements-list',
  imports: [ReactiveFormsModule, NgClass],
  templateUrl: './documentRequirements-list.component.html',
  styleUrl: './documentRequirements-list.component.css',
})
export class DocumentRequirementsListComponent implements OnInit {
  readonly svc = inject(DocumentRequirementsService);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  requirements = signal<DocumentRequirement[]>([]);
  loading = signal(false);
  showModal = signal(false);
  editingId = signal<number | null>(null);
  editingReq = signal<DocumentRequirement | null>(null);
  saving = signal(false);
  deletingId = signal<number | null>(null);
  uploadingTemplate = signal(false);

  form = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    allowedFormats: ['PDF', Validators.required],
    isRequired: [true],
    displayOrder: [1, [Validators.required, Validators.min(1)]],
  });

  readonly formatOptions = ['PDF', 'JPG', 'PNG', 'PDF,JPG,PNG', 'PDF,JPG', 'JPG,PNG'];

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading.set(true);
    this.svc.findAll().subscribe({
      next: (data) => {
        this.requirements.set(data.sort((a, b) => a.displayOrder - b.displayOrder));
        this.loading.set(false);
      },
      error: () => this.loading.set(false),
    });
  }

  openCreate(): void {
    this.editingId.set(null);
    this.editingReq.set(null);
    this.form.reset({ name: '', description: '', allowedFormats: 'PDF', isRequired: true, displayOrder: this.requirements().length + 1 });
    this.showModal.set(true);
  }

  openEdit(req: DocumentRequirement): void {
    this.editingId.set(req.id);
    this.editingReq.set(req);
    this.form.patchValue({
      name: req.name,
      description: req.description || '',
      allowedFormats: req.allowedFormats,
      isRequired: req.isRequired,
      displayOrder: req.displayOrder,
    });
    this.showModal.set(true);
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
        this.load();
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }

  delete(id: number): void {
    if (!confirm('¿Seguro que quieres eliminar este requisito de documento?')) return;
    this.deletingId.set(id);
    this.svc.delete(id).subscribe({
      next: () => { this.toast.success('Requisito eliminado'); this.load(); this.deletingId.set(null); },
      error: () => this.deletingId.set(null),
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
        this.requirements.update(reqs => reqs.map(r => r.id === id ? updated : r));
        this.toast.success('Plantilla subida correctamente');
        this.uploadingTemplate.set(false);
        input.value = '';
      },
      error: () => {
        this.toast.error('Error al subir la plantilla');
        this.uploadingTemplate.set(false);
        input.value = '';
      },
    });
  }
}
