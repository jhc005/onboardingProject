import { Component } from '@angular/core';
import { AdminUsersResetPasswordComponent } from '../../modules/adminUsers/infrastructure/components/forms/reset-password/adminUsers-reset-password.component';

@Component({
  selector: 'app-reset-password-page',
  imports: [AdminUsersResetPasswordComponent],
  template: '<app-admin-users-reset-password />',
})
export class ResetPasswordPageComponent {}
