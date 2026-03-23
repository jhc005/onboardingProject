import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { CandidatesService } from '../../application/candidates.service';
import { ToastService } from '../../../../core/services/toast.service';
import { Candidate } from '../../domain/candidates';

@Component({
  selector: 'app-candidates-list',
  imports: [ReactiveFormsModule, RouterLink, DatePipe],
  templateUrl: './candidates-list.component.html',
})
export class CandidatesListComponent implements OnInit {
  private readonly svc = inject(CandidatesService);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  candidates = signal<Candidate[]>([]);
  loading = signal(false);
  showModal = signal(false);
  saving = signal(false);

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
  });

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.svc.findAll().subscribe({
      next: (data) => { this.candidates.set(data); this.loading.set(false); },
      error: () => this.loading.set(false),
    });
  }

  openModal(): void {
    this.form.reset();
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const { email, firstName, lastName } = this.form.value;
    this.svc.create({ email: email!, firstName: firstName!, lastName: lastName! }).subscribe({
      next: () => {
        this.toast.success('Candidato creado. Se ha enviado el enlace de acceso por email.');
        this.closeModal();
        this.load();
        this.saving.set(false);
      },
      error: () => this.saving.set(false),
    });
  }
}
