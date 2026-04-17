import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AdminUsersService } from '../../../../application/adminUsers.service';

function passwordsMatch(control: AbstractControl): ValidationErrors | null {
  const newPassword = control.get('newPassword')?.value;
  const confirmPassword = control.get('confirmPassword')?.value;
  return newPassword === confirmPassword ? null : { passwordsMismatch: true };
}

@Component({
  selector: 'app-admin-users-reset-password',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './adminUsers-reset-password.component.html',
  styleUrl: './adminUsers-reset-password.component.css',
})
export class AdminUsersResetPasswordComponent {
  private readonly fb = inject(FormBuilder);
  private readonly adminService = inject(AdminUsersService);
  private readonly route = inject(ActivatedRoute);

  loading = signal(false);
  success = signal(false);
  errorMessage = signal<string | null>(null);

  private readonly token = this.route.snapshot.queryParamMap.get('token') ?? '';

  form = this.fb.group(
    {
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
    },
    { validators: passwordsMatch }
  );

  submit(): void {
    if (this.form.invalid) return;
    this.loading.set(true);
    this.errorMessage.set(null);
    const { newPassword } = this.form.value;
    this.adminService.resetPassword({ token: this.token, newPassword: newPassword! }).subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set(true);
      },
      error: () => {
        this.loading.set(false);
        this.errorMessage.set('El enlace es inválido o ha expirado. Solicitá uno nuevo.');
      },
    });
  }
}
