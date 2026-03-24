import { Component, inject, OnInit, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { DocumentRequirementsService } from '../../../application/documentRequirements.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { DocumentRequirement } from '../../../domain/documentRequirements';
import { DocumentRequirementsFormsComponent } from '../forms/documentRequirements-forms.component';

@Component({
  selector: 'app-document-requirements-list',
  imports: [NgClass, DocumentRequirementsFormsComponent],
  templateUrl: './documentRequirements-list.component.html',
  styleUrl: './documentRequirements-list.component.css',
})
export class DocumentRequirementsListComponent implements OnInit {
  private readonly svc = inject(DocumentRequirementsService);
  private readonly toast = inject(ToastService);

  requirements = signal<DocumentRequirement[]>([]);
  loading = signal(false);
  showModal = signal(false);
  editingRequirement = signal<DocumentRequirement | null>(null);
  deletingId = signal<number | null>(null);

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
    this.editingRequirement.set(null);
    this.showModal.set(true);
  }

  openEdit(req: DocumentRequirement): void {
    this.editingRequirement.set(req);
    this.showModal.set(true);
  }

  delete(id: number): void {
    if (!confirm('¿Seguro que quieres eliminar este requisito de documento?')) return;
    this.deletingId.set(id);
    this.svc.delete(id).subscribe({
      next: () => { this.toast.success('Requisito eliminado'); this.load(); this.deletingId.set(null); },
      error: () => this.deletingId.set(null),
    });
  }
}
