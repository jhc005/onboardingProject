import { Component, inject, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AdminUsersService } from '../../../../application/adminUsers.service';
import { ToastService } from '../../../../../../core/services/toast.service';

@Component({
  selector: 'app-admin-users-create-form',
  imports: [ReactiveFormsModule],
  templateUrl: './adminUsers-create-form.component.html',
})
export class AdminUsersCreateFormComponent {
  private readonly svc = inject(AdminUsersService);
  private readonly toast = inject(ToastService);
  private readonly fb = inject(FormBuilder);

  saved = output<void>();
  cancelled = output<void>();

  saving = signal(false);

  form = this.fb.group({
    fullName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });

  save(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.saving.set(true);
    const { fullName, email, password } = this.form.value;
    this.svc.create({ fullName: fullName!, email: email!, password: password! }).subscribe({
      next: () => {
        this.toast.success('Administrador creado correctamente.');
        this.saving.set(false);
        this.saved.emit();
      },
      error: () => this.saving.set(false),
    });
  }
}
