import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AcessTokensService } from '../../application/acessTokens.service';
import { DataFieldsService } from '../../../dataFields/application/dataFields.service';
import { DataAnswersService } from '../../../dataAnswers/application/dataAnswers.service';
import { OnboardingProcessesService } from '../../../onboardingProcesses/application/onboardingProcesses.service';
import { ToastService } from '../../../../core/services/toast.service';
import { DataField } from '../../../dataFields/domain/dataFields';
import { ValidateTokenResponse } from '../../domain/acessTokens';

@Component({
  selector: 'app-access-tokens-form',
  imports: [ReactiveFormsModule],
  templateUrl: './acessTokens-forms.component.html',
  styleUrl: './acessTokens-forms.component.css',
})
export class AcessTokensFormsComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly tokenSvc = inject(AcessTokensService);
  private readonly fieldsSvc = inject(DataFieldsService);
  private readonly answersSvc = inject(DataAnswersService);
  private readonly processSvc = inject(OnboardingProcessesService);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  step = signal<'validating' | 'form' | 'submitted' | 'error'>('validating');
  candidate = signal<ValidateTokenResponse | null>(null);
  fields = signal<DataField[]>([]);
  form!: FormGroup;
  saving = signal(false);
  submitting = signal(false);

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token')!;
    this.tokenSvc.validateToken(token).subscribe({
      next: (res) => {
        if (res.status === 'SUBMITTED' || res.status === 'VALIDATED' || res.status === 'REJECTED') {
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
    this.fieldsSvc.findAllForCandidate().subscribe({
      next: (fields) => {
        this.fields.set(fields.sort((a, b) => a.displayOrder - b.displayOrder));
        this.buildForm(fields);
        this.step.set('form');
      },
      error: () => this.step.set('error'),
    });
  }

  buildForm(fields: DataField[]): void {
    const controls: Record<string, any> = {};
    for (const f of fields) {
      controls[`field_${f.id}`] = ['', f.isRequired ? Validators.required : []];
    }
    this.form = this.fb.group(controls);
  }

  getFieldControl(id: number) {
    return this.form?.get(`field_${id}`);
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
  }

  private buildAnswers() {
    return this.fields()
      .map((f) => ({ dataFieldId: f.id, answerValue: this.form.get(`field_${f.id}`)?.value || '' }))
      .filter((a) => a.answerValue !== '');
  }
}
