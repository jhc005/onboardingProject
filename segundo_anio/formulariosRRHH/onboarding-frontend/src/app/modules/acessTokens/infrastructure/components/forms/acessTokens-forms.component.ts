import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AcessTokensService } from '../../../application/acessTokens.service';
import { DataFieldsService } from '../../../../dataFields/application/dataFields.service';
import { DataAnswersService } from '../../../../dataAnswers/application/dataAnswers.service';
import { OnboardingProcessesService } from '../../../../onboardingProcesses/application/onboardingProcesses.service';
import { DocumentRequirementsService } from '../../../../documentRequirements/application/documentRequirements.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { DataField } from '../../../../dataFields/domain/dataFields';
import { ValidateTokenResponse } from '../../../domain/acessTokens';
import { DocumentRequirement } from '../../../../documentRequirements/domain/documentRequirements';
import { UploadedDocumentsFormsComponent } from '../../../../uploadedDocuments/infrastructure/components/uploadedDocuments-forms.component';
import { DataAnswersFormsComponent } from '../../../../dataAnswers/infrastructure/components/forms/dataAnswers-forms.component';
import { UploadedDocumentsService } from '../../../../uploadedDocuments/application/uploadedDocuments.service';

@Component({
  selector: 'app-access-tokens-form',
  imports: [UploadedDocumentsFormsComponent, DataAnswersFormsComponent],
  templateUrl: './acessTokens-forms.component.html',
  styleUrl: './acessTokens-forms.component.css',
})
export class AcessTokensFormsComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly tokenSvc = inject(AcessTokensService);
  private readonly fieldsSvc = inject(DataFieldsService);
  private readonly answersSvc = inject(DataAnswersService);
  private readonly processSvc = inject(OnboardingProcessesService);
  private readonly docReqSvc = inject(DocumentRequirementsService);
  private readonly toast = inject(ToastService);
  private readonly uploadedDocsSvc = inject(UploadedDocumentsService)
  private readonly fb = inject(FormBuilder);

  step = signal<'validating' | 'form' | 'submitted' | 'error'>('validating');
  candidate = signal<ValidateTokenResponse | null>(null);
  fields = signal<DataField[]>([]);
  documentRequirements = signal<DocumentRequirement[]>([]);
  form!: FormGroup;
  saving = signal(false);
  submitting = signal(false);

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token')!;
    this.tokenSvc.validateToken(token).subscribe({
      next: (res) => {
        if (res.status === 'SUBMITTED' || res.status === 'VALIDATED') {
          this.candidate.set(res);
          this.step.set('submitted');
          return;
        }
        this.candidate.set(res);
        this.loadFields();
      },
      error: () => this.step.set('error'),
    });
  }

  loadFields(): void {
    const processId = this.candidate()!.processId;
    this.fieldsSvc.findAllForCandidate().subscribe({
      next: (fields) => {
        this.fields.set(fields.sort((a, b) => a.displayOrder - b.displayOrder));
        this.buildForm(fields);
        this.answersSvc.findByProcess(processId).subscribe({
          next: (answers) => {
            const patch: Record<string, string> = {};
            answers.forEach(a => { patch[`field_${a.dataFieldId}`] = a.answerValue; });
            this.form.patchValue(patch);
          },
        });
        this.docReqSvc.findAllForCandidate().subscribe({
          next: (reqs) => {
            this.documentRequirements.set(reqs.sort((a, b) => a.displayOrder - b.displayOrder));
            this.step.set('form');
          },
          error: () => this.step.set('form'),
        });
      },
      error: () => this.step.set('error'),
    });
  }

  buildForm(fields: DataField[]): void {
    const controls: Record<string, any> = {};
    for (const f of fields) {
      const validators = [];
      if (f.isRequired) validators.push(Validators.required);
      if (f.fieldType === 'NUMBER') validators.push(Validators.pattern(/^\d+$/));
      controls[`field_${f.id}`] = ['', validators];
    }
    this.form = this.fb.group(controls);
  }

  saveProgress(): void {
    const processId = this.candidate()!.processId;
    const answers = this.buildAnswers();
    if (!answers.length) return;
    this.saving.set(true);
    this.answersSvc.save(processId, { answers }).subscribe({
      next: () => { this.toast.success('Progreso guardado'); this.saving.set(false); },
      error: () => this.saving.set(false),
    });
  }

  submitForm(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); this.toast.warning('Por favor, completa todos los campos obligatorios.'); return; }
    const processId = this.candidate()!.processId;

    this.uploadedDocsSvc.findByProcessId(processId).subscribe({
      next: (uploadedDocs) => {
        const uploadedReqIds = new Set(uploadedDocs.map(d => d.documentRequirementId));
        const missing = this.documentRequirements()
          .filter(r => r.isRequired && !uploadedReqIds.has(r.id));

        if (missing.length > 0) {
          this.toast.warning(`Faltan documentos obligatorios: ${missing.map(r => r.name).join(', ')}`);
          return;
        }

        const answers = this.buildAnswers();
        this.submitting.set(true);
        this.answersSvc.save(processId, { answers }).subscribe({
          next: () => {
            this.processSvc.submit(processId).subscribe({
              next: () => { this.toast.success('Formulario enviado correctamente. ¡Gracias!'); this.step.set('submitted'); this.submitting.set(false); },
              error: () => this.submitting.set(false),
            });
          },
          error: () => this.submitting.set(false),
        });
      },
      error: () => this.submitting.set(false),
    });
  }

  private buildAnswers() {
    return this.fields()
      .map((f) => ({ dataFieldId: f.id, answerValue: this.form.get(`field_${f.id}`)?.value || '' }))
      .filter((a) => a.answerValue !== '');
  }
}
