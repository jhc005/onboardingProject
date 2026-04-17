import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AdminUsersService } from '../../../../application/adminUsers.service';

@Component({
  selector: 'app-admin-users-forgot-password',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './adminUsers-forgot-password.component.html',
  styleUrl: './adminUsers-forgot-password.component.css',
})
export class AdminUsersForgotPasswordComponent {
  private readonly fb = inject(FormBuilder);
  private readonly adminService = inject(AdminUsersService);

  loading = signal(false);
  submitted = signal(false);

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
  });

  submit(): void {
    if (this.form.invalid) return;
    this.loading.set(true);
    const { email } = this.form.value;
    this.adminService.forgotPassword({ email: email! }).subscribe({
      next: () => {
        this.loading.set(false);
        this.submitted.set(true);
      },
      error: () => {
        this.loading.set(false);
        this.submitted.set(true);
      },
    });
  }
}
