import { Component, inject, Input, OnInit, signal } from '@angular/core';
import { Location, DatePipe, NgClass } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { OnboardingProcessesService } from '../../../application/onboardingProcesses.service';
import { DataAnswersService } from '../../../../dataAnswers/application/dataAnswers.service';
import { ToastService } from '../../../../../core/services/toast.service';
import { OnboardingProcess, OnboardingStatus } from '../../../domain/onboardingProcesses';
import { DataAnswer } from '../../../../dataAnswers/domain/dataAnswers';

@Component({
  selector: 'app-onboarding-processes-form',
  imports: [DatePipe, NgClass, ReactiveFormsModule],
  templateUrl: './onboardingProcesses-forms.component.html',
  styleUrl: './onboardingProcesses-forms.component.css',
})
export class OnboardingProcessesFormsComponent implements OnInit {
  @Input() id!: string;

  private readonly processSvc = inject(OnboardingProcessesService);
  private readonly answersSvc = inject(DataAnswersService);
  private readonly toast = inject(ToastService);
  private readonly location = inject(Location);
  private readonly fb = inject(FormBuilder);

  process = signal<OnboardingProcess | null>(null);
  answers = signal<DataAnswer[]>([]);
  loading = signal(true);
  showRejectModal = signal(false);
  actionLoading = signal(false);

  rejectForm = this.fb.group({
    notes: ['', Validators.required],
  });

  ngOnInit(): void {
    this.loadProcess();
  }

  loadProcess(): void {
    this.loading.set(true);
    this.processSvc.findById(this.id).subscribe({
      next: (p) => {
        this.process.set(p);
        this.loading.set(false);
        if (p.status === 'SUBMITTED' || p.status === 'VALIDATED' || p.status === 'REJECTED') {
          this.loadAnswers();
        }
      },
      error: () => this.loading.set(false),
    });
  }

  loadAnswers(): void {
    this.answersSvc.findByProcessAdmin(this.id).subscribe({
      next: (data) => this.answers.set(data),
    });
  }

  validate(): void {
    this.actionLoading.set(true);
    this.processSvc.validate(this.id).subscribe({
      next: (p) => {
        this.process.set(p);
        this.toast.success('Proceso validado. Se ha notificado al candidato por email.');
        this.actionLoading.set(false);
      },
      error: () => this.actionLoading.set(false),
    });
  }

  openReject(): void {
    this.rejectForm.reset();
    this.showRejectModal.set(true);
  }

  confirmReject(): void {
    if (this.rejectForm.invalid) { this.rejectForm.markAllAsTouched(); return; }
    this.actionLoading.set(true);
    const notes = this.rejectForm.value.notes!;
    this.processSvc.reject(this.id, notes).subscribe({
      next: (p) => {
        this.process.set(p);
        this.toast.info('Proceso rechazado. Se ha notificado al candidato.');
        this.showRejectModal.set(false);
        this.actionLoading.set(false);
      },
      error: () => this.actionLoading.set(false),
    });
  }

  goBack(): void {
    this.location.back();
  }

  statusLabel(s: OnboardingStatus): string {
    const map: Record<OnboardingStatus, string> = {
      PENDING_ACCESS: 'Pendiente acceso', IN_PROGRESS: 'En progreso',
      SUBMITTED: 'Enviado', VALIDATED: 'Validado', REJECTED: 'Rechazado',
    };
    return map[s];
  }

  statusClass(s: OnboardingStatus): string {
    const map: Record<OnboardingStatus, string> = {
      PENDING_ACCESS: 'badge-pending', IN_PROGRESS: 'badge-in-progress',
      SUBMITTED: 'badge-submitted', VALIDATED: 'badge-validated', REJECTED: 'badge-rejected',
    };
    return map[s];
  }
}
