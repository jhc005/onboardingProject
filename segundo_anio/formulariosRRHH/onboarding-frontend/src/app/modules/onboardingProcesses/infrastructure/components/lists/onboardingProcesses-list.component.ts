import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe, NgClass } from '@angular/common';
import { OnboardingProcessesService } from '../../../application/onboardingProcesses.service';
import { OnboardingProcess, OnboardingStatus } from '../../../domain/onboardingProcesses';

@Component({
  selector: 'app-onboarding-processes-list',
  imports: [RouterLink, DatePipe, NgClass],
  templateUrl: './onboardingProcesses-list.component.html',
  styleUrl: './onboardingProcesses-list.component.css',
})
export class OnboardingProcessesListComponent implements OnInit {
  private readonly svc = inject(OnboardingProcessesService);

  processes = signal<OnboardingProcess[]>([]);
  loading = signal(false);
  filterStatus = signal<OnboardingStatus | 'ALL'>('ALL');

  statusOptions: { value: OnboardingStatus | 'ALL'; label: string }[] = [
    { value: 'ALL', label: 'Todos' },
    { value: 'PENDING_ACCESS', label: 'Pendiente acceso' },
    { value: 'IN_PROGRESS', label: 'En progreso' },
    { value: 'SUBMITTED', label: 'Enviado' },
    { value: 'VALIDATED', label: 'Validado' },
    { value: 'REJECTED', label: 'Rechazado' },
  ];

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.svc.findAll().subscribe({
      next: (data) => { this.processes.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  filtered(): OnboardingProcess[] {
    const s = this.filterStatus();
    return s === 'ALL' ? this.processes() : this.processes().filter((p) => p.status === s);
  }

  statusLabel(s: OnboardingStatus): string {
    const map: Record<OnboardingStatus, string> = {
      PENDING_ACCESS: 'Pendiente acceso',
      IN_PROGRESS: 'En progreso',
      SUBMITTED: 'Enviado',
      VALIDATED: 'Validado',
      REJECTED: 'Rechazado',
    };
    return map[s];
  }

  statusClass(s: OnboardingStatus): string {
    const map: Record<OnboardingStatus, string> = {
      PENDING_ACCESS: 'badge-pending',
      IN_PROGRESS: 'badge-in-progress',
      SUBMITTED: 'badge-submitted',
      VALIDATED: 'badge-validated',
      REJECTED: 'badge-rejected',
    };
    return map[s];
  }
}
