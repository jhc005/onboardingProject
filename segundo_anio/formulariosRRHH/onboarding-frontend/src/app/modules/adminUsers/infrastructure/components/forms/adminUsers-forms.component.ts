import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminUsersService } from '../../../application/adminUsers.service';
import { ToastService } from '../../../../../core/services/toast.service';

@Component({
  selector: 'app-admin-users-form',
  imports: [ReactiveFormsModule],
  templateUrl: './adminUsers-forms.component.html',
  styleUrl: './adminUsers-forms.component.css',
})
export class AdminUsersFormsComponent {
  private readonly fb = inject(FormBuilder);
  private readonly adminService = inject(AdminUsersService);
  private readonly toast = inject(ToastService);
  private readonly router = inject(Router);

  loading = signal(false);

  form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', Validators.required],
  });

  submit(): void {
    if (this.form.invalid) return;
    this.loading.set(true);
    const { email, password } = this.form.value;
    this.adminService.login({ email: email!, password: password! }).subscribe({
      next: () => {
        this.toast.success('Bienvenido al panel de administración');
        this.router.navigate(['/admin/candidates']);
      },
      error: () => this.loading.set(false),
    });
  }
}
