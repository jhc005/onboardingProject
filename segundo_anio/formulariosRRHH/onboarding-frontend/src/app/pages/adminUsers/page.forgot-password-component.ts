import { Component } from '@angular/core';
import { AdminUsersForgotPasswordComponent } from '../../modules/adminUsers/infrastructure/components/forms/forgot-password/adminUsers-forgot-password.component';

@Component({
  selector: 'app-forgot-password-page',
  imports: [AdminUsersForgotPasswordComponent],
  template: '<app-admin-users-forgot-password />',
})
export class ForgotPasswordPageComponent {}
