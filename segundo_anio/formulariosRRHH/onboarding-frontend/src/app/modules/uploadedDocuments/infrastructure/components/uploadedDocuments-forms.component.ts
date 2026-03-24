import { Component, inject, Input, OnInit, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { UploadedDocumentsService } from '../../application/uploadedDocuments.service';
import { ToastService } from '../../../../core/services/toast.service';
import { DocumentRequirement } from '../../../documentRequirements/domain/documentRequirements';
import { DocumentRequirementsService } from '../../../documentRequirements/application/documentRequirements.service';
import { UploadedDocument } from '../../domain/uploadedDocuments';

@Component({
  selector: 'app-uploaded-documents-form',
  imports: [NgClass],
  templateUrl: './uploadedDocuments-forms.component.html',
  styleUrl: './uploadedDocuments-forms.component.css',
})
export class UploadedDocumentsFormsComponent implements OnInit {
  @Input() processId!: string;
  @Input() requirements: DocumentRequirement[] = [];

  private readonly svc = inject(UploadedDocumentsService);
  private readonly toast = inject(ToastService);
  private readonly docReqSvc = inject(DocumentRequirementsService);

  uploadedMap = signal<Map<number, UploadedDocument>>(new Map());
  uploadingId = signal<number | null>(null);

  ngOnInit(): void {
    this.svc.findByProcessId(this.processId).subscribe({
      next: (docs) => {
        const map = new Map<number, UploadedDocument>();
        docs.forEach(d => map.set(d.documentRequirementId, d));
        this.uploadedMap.set(map);
      },
    });
  }

  getUploaded(requirementId: number): UploadedDocument | undefined {
    return this.uploadedMap().get(requirementId);
  }

  onFileSelected(requirementId: number, event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.uploadingId.set(requirementId);
    this.svc.upload(this.processId, requirementId, file).subscribe({
      next: (doc) => {
        this.uploadedMap.update(map => {
          const newMap = new Map(map);
          newMap.set(requirementId, doc);
          return newMap;
        });
        this.toast.success('Documento subido correctamente');
        this.uploadingId.set(null);
        input.value = '';
      },
      error: () => {
        this.toast.error('Error al subir el documento. Verifica el formato.');
        this.uploadingId.set(null);
        input.value = '';
      },
    });
  }

  downloadTemplate(id: number, name: string): void {
    this.docReqSvc.downloadTemplateForCandidate(id).subscribe(blob => {
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = name;
      a.click();
      URL.revokeObjectURL(url);
    });
  }

  formatFileSize(bytes: number): string {
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
  }
}
