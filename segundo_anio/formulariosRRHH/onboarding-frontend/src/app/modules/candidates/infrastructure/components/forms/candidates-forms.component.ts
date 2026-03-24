import { Component, inject, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CandidatesService } from '../../../application/candidates.service';
import { ToastService } from '../../../../../core/services/toast.service';

@Component({
  selector: 'app-candidates-form',
  imports: [ReactiveFormsModule],
  templateUrl: './candidates-forms.component.html',
})
export class CandidatesFormsComponent {
  private readonly svc = inject(CandidatesService);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  saved = output<void>();
  cancelled = output<void>();

  saving = signal(false);

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
  });

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const { email, firstName, lastName } = this.form.value;
    this.svc.create({ email: email!, firstName: firstName!, lastName: lastName! }).subscribe({
      next: () => {
        this.toast.success('Candidato creado. Se ha enviado el enlace de acceso por email.');
        this.saving.set(false);
        this.saved.emit();
      },
      error: () => this.saving.set(false),
    });
  }
}
