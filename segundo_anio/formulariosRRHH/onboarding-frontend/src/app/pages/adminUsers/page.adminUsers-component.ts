import { Component } from '@angular/core';
import { AdminUsersFormsComponent } from '../../modules/adminUsers/infrastructure/components/adminUsers-forms.component';

@Component({
  selector: 'app-login-page',
  imports: [AdminUsersFormsComponent],
  template: '<app-admin-users-form />',
})
export class LoginPageComponent {}
